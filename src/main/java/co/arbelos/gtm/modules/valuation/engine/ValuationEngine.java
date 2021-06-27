package co.arbelos.gtm.modules.valuation.engine;

import co.arbelos.gtm.core.dao.jato.MarkModelProjection;
import co.arbelos.gtm.core.dao.valuation.PredictionVersionProjection;
import co.arbelos.gtm.core.domain.configuration.CarSegmentDepreciationConfig;
import co.arbelos.gtm.core.domain.jato.Version;
import co.arbelos.gtm.core.domain.jato.germandb.VersionDE;
import co.arbelos.gtm.core.domain.machinelearning.TrainResult;
import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import co.arbelos.gtm.core.domain.marketobservation.MarketMasterType;
import co.arbelos.gtm.core.domain.marketobservation.MarketObservation;
import co.arbelos.gtm.core.domain.registrations.CarRegistration;
import co.arbelos.gtm.core.domain.valuation.CarValuationOptions;
import co.arbelos.gtm.core.dto.web.wizard.TransmissionDescriptionDTO;
import co.arbelos.gtm.core.repository.configuration.AppConfigurationRepository;
import co.arbelos.gtm.core.repository.configuration.CarSegmentDepreciationConfigRepository;
import co.arbelos.gtm.core.repository.jato.VersionRepository;
import co.arbelos.gtm.core.repository.machinelearning.TrainResultRepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketDataRepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketMasterTypeRepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketObservationRepository;
import co.arbelos.gtm.core.repository.registrations.CarRegistrationRepository;
import co.arbelos.gtm.core.util.JatoUtil;
import co.arbelos.gtm.modules.valuation.tools.ValuationMLService;
import co.arbelos.gtm.valuation.service.wizard.WizardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ValuationEngine {

    private final Logger log = LoggerFactory.getLogger(ValuationEngine.class);

    private final VersionRepository versionRepository;
    private final MarketObservationRepository marketObservationRepository;
    private final MarketDataRepository marketDataRepository;
    private final MarketMasterTypeRepository marketMasterTypeRepository;
    private final CarRegistrationRepository carRegistrationRepository;
    private final TrainResultRepository trainResultRepository;
    private final AppConfigurationRepository appConfigurationRepository;
    private final CarSegmentDepreciationConfigRepository carSegmentDepreciationConfigRepository;

    private final ValuationMLService valuationMLService;
    private final WizardService wizardService;

    public ValuationEngine(VersionRepository versionRepository,
                           MarketObservationRepository marketObservationRepository,
                           MarketDataRepository marketDataRepository,
                           MarketMasterTypeRepository marketMasterTypeRepository,
                           CarRegistrationRepository carRegistrationRepository,
                           TrainResultRepository trainResultRepository,
                           AppConfigurationRepository appConfigurationRepository,
                           CarSegmentDepreciationConfigRepository carSegmentDepreciationConfigRepository,
                           ValuationMLService valuationMLService,
                           WizardService wizardService) {
        this.versionRepository = versionRepository;
        this.marketObservationRepository = marketObservationRepository;
        this.marketDataRepository = marketDataRepository;
        this.marketMasterTypeRepository = marketMasterTypeRepository;
        this.carRegistrationRepository = carRegistrationRepository;
        this.trainResultRepository = trainResultRepository;
        this.appConfigurationRepository = appConfigurationRepository;
        this.carSegmentDepreciationConfigRepository = carSegmentDepreciationConfigRepository;
        this.valuationMLService = valuationMLService;
        this.wizardService = wizardService;
    }

    public void createMatchings() {
        log.info("Creating matchings between jato and market data!");

        List<Version> versions = versionRepository.findAll();

        AtomicInteger counter = new AtomicInteger(1);
        List<MarketData> marketData = marketDataRepository.findAll();
        List<MarketObservation> marketObservations = new ArrayList<>();

        log.debug("Found {} available market items for matching!", marketData.size());

        versions.parallelStream().forEach(version -> {
            List<MarketData> foundMatchings = marketData
                .parallelStream()
                .filter(el -> el.equalsWith(version))
                .collect(Collectors.toList());

            if (foundMatchings.size() > 0) {
                log.debug("Found {} matchings for jato's vehicleId: {}", foundMatchings.size(), version.getVehicleId());
                foundMatchings.forEach(md -> {
                    MarketObservation marketObservation = new MarketObservation();
                    marketObservation.setVehicleId(version.getVehicleId());
                    marketObservation.setMarketId(md.getId());

                    marketObservations.add(marketObservation);
                });
            } else {
                log.error("No matching found in market data for jato's vehicleId: {}", version.getVehicleId());
            }

            log.info("[MATCHING] Completed {}/{}", counter.getAndIncrement(), versions.size());
        });

        if (marketObservations.size() > 0) {
            log.info("Cleaning up old market observation data!");
            marketObservationRepository.truncateTable();

            log.info("Adding new observation market of size: {} items", marketObservations.size());
            marketObservationRepository.saveAll(marketObservations);
        }
    }

    @Transactional
    public void createMasterTypes() {
        log.info("Creating master types!");

        List<MarkModelProjection> results = versionRepository.findDistinctMarksModels();

        AtomicInteger count = new AtomicInteger(1);
        if (results.size() > 0) {
            marketMasterTypeRepository.truncateTable();

            results
                .parallelStream()
                .forEach(markModel -> {
                    List<CarRegistration> carRegistrations = carRegistrationRepository.findMasterTypes(markModel.getManufacturer(), markModel.getModel());

                    // get the years
                    List<Integer> years = new ArrayList<>();

                    carRegistrations.forEach(cr -> {
                        if (!years.contains(cr.getDate().getYear())) {
                            years.add(cr.getDate().getYear());
                        }
                    });

                    // init map per year
                    Map<Integer, Map<String, Integer>> yearMap= new HashMap<>();
                    years.forEach(y -> {
                        yearMap.put(y, new HashMap<>());
                    });

                    // iterate carRegistrations
                    carRegistrations.forEach(cr -> {
                        int existingValue = yearMap.get(cr.getDate().getYear()).getOrDefault(cr.getBody() + "-" + cr.getEngineHP() + "-" + cr.getFuel() + "-" + cr.getEngineLitres(), 0);
                        yearMap.get(cr.getDate().getYear()).put(cr.getBody() + "-" + cr.getEngineHP() + "-" + cr.getFuel() + "-" + cr.getEngineLitres(), existingValue + cr.getAmount());
                    });

                    // create master-type
                    years.forEach(y -> {
                        String[] values = Collections.max(yearMap.get(y).entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey().split("-");

                        MarketMasterType marketMasterType = new MarketMasterType();

                        marketMasterType.setYear(y);
                        marketMasterType.setManufacturer(markModel.getManufacturer());
                        marketMasterType.setModel(markModel.getModel());
                        marketMasterType.setRegisteredVehicles(Collections.max(yearMap.get(y).entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue());
                        marketMasterType.setBody(values[0]);
                        try {
                            marketMasterType.setEngineHp(Integer.valueOf(values[1]));
                        } catch (Exception e) {
                            log.error("[MASTER-TYPE] Error when parsing engine hp for {}/{}, error: {}", markModel.getManufacturer(), markModel.getModel(), e.getMessage());
                        }
                        marketMasterType.setFuelType(values[2]);
                        try {
                            marketMasterType.setEngineLitres(Float.parseFloat(values[3]));
                        } catch (Exception e) {
                            log.error("[MASTER-TYPE] Error when parsing engine litres for {}/{}, error: {}", markModel.getManufacturer(), markModel.getModel(), e.getMessage());
                        }

                        marketMasterTypeRepository.save(marketMasterType);
                    });

                    log.info("[MASTER-TYPE] Completed {}/{}", count.getAndIncrement(), results.size());
                });
        } else {
            log.error("[MASTER-TYPE] There are no available marks/models for computing master types!");
        }
    }

    public List<MarketMasterType> findMasterTypes(String manufacturer, String model) {
        return marketMasterTypeRepository
            .findAllByManufacturerAndModelOrderByYearDesc(manufacturer, model)
            .stream()
            .peek(el -> {
                el.setBody(JatoUtil.getBodyType(el.getBody()));
                el.setFuelType(JatoUtil.getFuelType(el.getFuelType()));
            })
            .collect(Collectors.toList());
    }

    public Float createEstimation(Version carVersion, Integer kmNo) {
        TrainResult trainResult = trainResultRepository.findLastOne(carVersion.getVehicleManufacturerName(), carVersion.getModelName());

        // verifica daca transmisison type != manual
        List<TransmissionDescriptionDTO> transmissionDescriptionDTOS = wizardService.getTransmissionDescription(carVersion.getVehicleId());
        if (transmissionDescriptionDTOS.size() > 0 && !transmissionDescriptionDTOS.get(0).getText().toLowerCase().equals("manual") && !transmissionDescriptionDTOS.get(0).getText().toLowerCase().equals("manual\r")) {
            carVersion.setTransmissionType("A");
        }

        if (carVersion.getFuelType().equals("P")) {
            carVersion.setFuelType("U");
        }

        float predictedPrice = -1.0f;
        boolean succes = false;

        if (trainResult != null && trainResult.getVarianceScore() > 0.9) {
            try {
                predictedPrice = valuationMLService.estimateValue(carVersion, kmNo);
                succes = true;
            } catch (Exception e) {
                log.error("Error predicting price -> {}", e.getMessage());
                succes = false;
            }
        }

        if (!succes || predictedPrice > carVersion.getBasePrice() * 0.95) {
            List<Version> carsInSegment = versionRepository.findAllBySegmentCode(carVersion.getSegmentCode(), carVersion.getBasePrice());
            log.debug("Cars in segment: {}", carsInSegment.size());

            List<String> uniqueMakes = new ArrayList<>();
            List<String> uniqueModels = new ArrayList<>();
            carsInSegment.forEach(car -> {
                if (!uniqueMakes.contains(car.getVehicleManufacturerName())) {
                    uniqueMakes.add(car.getVehicleManufacturerName());
                }

                if (!uniqueModels.contains(car.getModelName())) {
                    uniqueModels.add(car.getModelName());
                }
            });

            List<TrainResult> trainResults = trainResultRepository.findInListOfMakeAndModel(uniqueMakes, uniqueModels);
            List<TrainResult> validatedTrainResults = trainResults.stream().filter(el -> el.getVarianceScore() > 0.9).collect(Collectors.toList());

            log.debug("Non vs Validated train results: {}/{}", trainResults.size(), validatedTrainResults.size());
            if (validatedTrainResults.size() > 0) {
                List<MarketMasterType> marketMasterTypes = new ArrayList<>();

                validatedTrainResults.forEach(el -> {
                    marketMasterTypeRepository
                        .findAllByManufacturerAndModelOrderByYearDesc(el.getManufacturer(), el.getModel())
                        .stream()
                        .limit(1)
                        .map(marketMasterTypes::add)
                        .findAny();
                });

                Map<PredictionVersionProjection, Float> data = new HashMap<>();
                List<Float> percentages = new ArrayList<>();

                List<String> info = new ArrayList<>();

                marketMasterTypes
                    .stream()
                    .limit(100)
                    .forEach(marketMasterType -> {
                        PredictionVersionProjection tempVersion = versionRepository.findOneForPredict(
                            marketMasterType.getManufacturer(),
                            marketMasterType.getModel(),
                            marketMasterType.getEngineHp(),
                            Integer.valueOf(carVersion.getModelYear()),
                            marketMasterType.getFuelType()
                        );

                        try {
                            data.put(tempVersion, valuationMLService.estimateValue(tempVersion, kmNo));
                            log.debug("{} {} {} - {}", tempVersion.getMake(), tempVersion.getModel(), tempVersion.getPrice(), data.get(tempVersion));
                        } catch (Exception e) {}
                    });


                for (Map.Entry<PredictionVersionProjection, Float> entry : data.entrySet()) {
                    percentages.add(-((entry.getKey().getPrice() - entry.getValue())/entry.getKey().getPrice() * 100));
                }

                percentages = percentages.stream().filter(el -> el < 0).collect(Collectors.toList());

                Collections.sort(percentages);
                int middle = percentages.size() / 2;
                middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;

                float decreasePercentege = percentages.get(middle);

                float diff = decreasePercentege / 100 * carVersion.getBasePrice();

                log.debug("{} % - {} EUR", decreasePercentege, diff);

                predictedPrice = carVersion.getBasePrice() - Math.abs(diff);
            }
        }

        return predictedPrice;
    }

    public Float createEstimationDE(VersionDE carVersion, Integer kmNo) {
        TrainResult trainResult = trainResultRepository.findLastOne(carVersion.getVehicleManufacturerName(), carVersion.getModelName());

        if (carVersion.getFuelType().equals("P")) {
            carVersion.setFuelType("U");
        }

        float predictedPrice = -1.0f;
        boolean succes = false;

        if (trainResult != null && trainResult.getVarianceScore() > 0.9) {
            try {
                predictedPrice = valuationMLService.estimateValueDE(carVersion, kmNo);
                succes = true;
            } catch (Exception e) {
                log.error("Error predicting price -> {}", e.getMessage());
                succes = false;
            }
        }

        if (!succes || predictedPrice > carVersion.getBasePrice() * 0.95) {
            List<Version> carsInSegment = versionRepository.findAllBySegmentCode(carVersion.getSegmentCode(), carVersion.getBasePrice());
            log.debug("Cars in segment: {}", carsInSegment.size());

            List<String> uniqueMakes = new ArrayList<>();
            List<String> uniqueModels = new ArrayList<>();
            carsInSegment.forEach(car -> {
                if (!uniqueMakes.contains(car.getVehicleManufacturerName())) {
                    uniqueMakes.add(car.getVehicleManufacturerName());
                }

                if (!uniqueModels.contains(car.getModelName())) {
                    uniqueModels.add(car.getModelName());
                }
            });

            List<TrainResult> trainResults = trainResultRepository.findInListOfMakeAndModel(uniqueMakes, uniqueModels);
            List<TrainResult> validatedTrainResults = trainResults.stream().filter(el -> el.getVarianceScore() > 0.9).collect(Collectors.toList());

            log.debug("Non vs Validated train results: {}/{}", trainResults.size(), validatedTrainResults.size());
            if (validatedTrainResults.size() > 0) {
                List<MarketMasterType> marketMasterTypes = new ArrayList<>();

                validatedTrainResults.forEach(el -> {
                    marketMasterTypeRepository
                        .findAllByManufacturerAndModelOrderByYearDesc(el.getManufacturer(), el.getModel())
                        .stream()
                        .limit(1)
                        .map(marketMasterTypes::add)
                        .findAny();
                });

                Map<PredictionVersionProjection, Float> data = new HashMap<>();
                List<Float> percentages = new ArrayList<>();

                List<String> info = new ArrayList<>();

                marketMasterTypes
                    .stream()
                    .limit(100)
                    .forEach(marketMasterType -> {
                        PredictionVersionProjection tempVersion = versionRepository.findOneForPredict(
                            marketMasterType.getManufacturer(),
                            marketMasterType.getModel(),
                            marketMasterType.getEngineHp(),
                            Integer.valueOf(carVersion.getModelYear()),
                            marketMasterType.getFuelType()
                        );

                        try {
                            data.put(tempVersion, valuationMLService.estimateValue(tempVersion, kmNo));
                            log.debug("{} {} {} - {}", tempVersion.getMake(), tempVersion.getModel(), tempVersion.getPrice(), data.get(tempVersion));
                        } catch (Exception e) {}
                    });


                for (Map.Entry<PredictionVersionProjection, Float> entry : data.entrySet()) {
                    percentages.add(-((entry.getKey().getPrice() - entry.getValue())/entry.getKey().getPrice() * 100));
                }

                percentages = percentages.stream().filter(el -> el < 0).collect(Collectors.toList());

                Collections.sort(percentages);
                int middle = percentages.size() / 2;
                middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;

                float decreasePercentege = percentages.get(middle);

                float diff = decreasePercentege / 100 * carVersion.getBasePrice();

                log.debug("{} % - {} EUR", decreasePercentege, diff);

                predictedPrice = carVersion.getBasePrice() - Math.abs(diff);
            }
        }

        return predictedPrice;
    }

    public Float getExtraOptionsPrice(Version carVersion, List<CarValuationOptions> carValuationOptions) {
        if (carValuationOptions.size() == 0) {
            return 0.0f;
        } else {
            float optionalsValue = 0;
            for (CarValuationOptions carOpt : carValuationOptions) {
                optionalsValue += carOpt.getOptionPrice();
            }

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int carYear = Integer.valueOf(carVersion.getModelYear());

            CarSegmentDepreciationConfig carSegmentDepreciationConfig = carSegmentDepreciationConfigRepository.findByDataValue(carVersion.getSegmentCode());

            if (currentYear == carYear) {
                return (optionalsValue - (optionalsValue * carSegmentDepreciationConfig.getFirstYear() / 100));
            } else if (carYear == currentYear - 1) {
                float total = carSegmentDepreciationConfig.getFirstYear() + carSegmentDepreciationConfig.getSecondYear();
                return (optionalsValue - (optionalsValue * total / 100));
            } else if (carYear == currentYear - 2) {
                float total = carSegmentDepreciationConfig.getFirstYear() + carSegmentDepreciationConfig.getSecondYear() + carSegmentDepreciationConfig.getThirdYear();
                return (optionalsValue - (optionalsValue * total / 100));
            } else if (carYear == currentYear - 3) {
                float total = carSegmentDepreciationConfig.getFirstYear() + carSegmentDepreciationConfig.getSecondYear() + carSegmentDepreciationConfig.getThirdYear() + carSegmentDepreciationConfig.getFourthYear();
                return (optionalsValue - (optionalsValue * total / 100));
            } else {
                float total = carSegmentDepreciationConfig.getFirstYear() + carSegmentDepreciationConfig.getSecondYear() + carSegmentDepreciationConfig.getThirdYear() + carSegmentDepreciationConfig.getFourthYear() + carSegmentDepreciationConfig.getFifthYear();
                return (optionalsValue - (optionalsValue * total / 100));
            }
        }

    }
}
