package co.arbelos.gtm.valuation.service.valuation;

import co.arbelos.gtm.core.dao.wizard.VehicleProjection;
import co.arbelos.gtm.core.domain.jato.Version;
import co.arbelos.gtm.core.domain.jato.germandb.VersionDE;
import co.arbelos.gtm.core.domain.marketobservation.MarketMasterType;
import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.core.domain.valuation.CarValuationOptions;
import co.arbelos.gtm.core.dto.web.valuation.AutovitParamsDTO;
import co.arbelos.gtm.core.dto.web.valuation.CreateValuationDTO;
import co.arbelos.gtm.core.dto.web.valuation.TimePointDTO;
import co.arbelos.gtm.core.dto.web.valuation.ValuationHistoryDTO;
import co.arbelos.gtm.core.dto.web.wizard.VehicleDTO;
import co.arbelos.gtm.core.repository.jato.VersionRepository;
import co.arbelos.gtm.core.repository.jato.germandb.VersionDERepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketDataRepository;
import co.arbelos.gtm.core.repository.marketobservation.MarketMasterTypeRepository;
import co.arbelos.gtm.core.repository.valuation.CarValuationOptionsRepository;
import co.arbelos.gtm.core.repository.valuation.CarValuationRepository;
import co.arbelos.gtm.core.util.AutovitUtil;
import co.arbelos.gtm.core.util.JatoUtil;
import co.arbelos.gtm.modules.valuation.engine.ValuationEngine;
import co.arbelos.gtm.valuation.config.AuditEventPublisher;
import co.arbelos.gtm.valuation.domain.User;
import co.arbelos.gtm.valuation.repository.UserRepository;
import co.arbelos.gtm.valuation.service.MailService;
import co.arbelos.gtm.valuation.service.UserService;
import co.arbelos.gtm.valuation.service.images.ImageService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ValuationService {

    private final Logger log = LoggerFactory.getLogger(ValuationService.class);

    private final CarValuationRepository carValuationRepository;
    private final CarValuationOptionsRepository carValuationOptionsRepository;
    private final MarketDataRepository marketDataRepository;
    private final MarketMasterTypeRepository marketMasterTypeRepository;
    private final UserRepository userRepository;
    private final VersionRepository versionRepository;
    private final VersionDERepository versionDERepository;

    private final ImageService imageService;
    private final ValuationEngine valuationEngine;

    private final AuditEventPublisher auditPublisher;
    private final MailService mailService;
    private final UserService userService;


    public ValuationService(CarValuationRepository carValuationRepository,
                            CarValuationOptionsRepository carValuationOptionsRepository,
                            MarketDataRepository marketDataRepository,
                            MarketMasterTypeRepository marketMasterTypeRepository,
                            UserRepository userRepository,
                            VersionRepository versionRepository,
                            VersionDERepository versionDERepository, ImageService imageService,
                            ValuationEngine valuationEngine,
                            AuditEventPublisher auditPublisher,
                            MailService mailService,
                            UserService userService) {
        this.carValuationRepository = carValuationRepository;
        this.carValuationOptionsRepository = carValuationOptionsRepository;
        this.marketDataRepository = marketDataRepository;
        this.marketMasterTypeRepository = marketMasterTypeRepository;
        this.userRepository = userRepository;
        this.versionRepository = versionRepository;
        this.versionDERepository = versionDERepository;
        this.imageService = imageService;
        this.valuationEngine = valuationEngine;
        this.auditPublisher = auditPublisher;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Transactional
    public CarValuation createValuation(CreateValuationDTO createValuationDTO, Principal principal) {
        log.debug("Trying to create valuation for: {} vehicleId", createValuationDTO.getVehicleId());
        Date thresholdDE = new GregorianCalendar(2008, Calendar.JUNE, 1).getTime();
        if (createValuationDTO.getFabricationDate().before(thresholdDE)) {
            return createValuationDE(createValuationDTO, principal);
        }

        User user = userRepository
            .findOneByLogin(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        Version carVersion  = Optional
                .ofNullable(versionRepository.findOneByVehicleId(createValuationDTO.getVehicleId()))
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + createValuationDTO.getVehicleId() + " doesn't exists!"));


        CarValuation carValuation = CarValuation.from(createValuationDTO, user);
        List<CarValuationOptions> carValuationOptions = createValuationDTO
            .getOptions()
            .stream()
            .map(element -> CarValuationOptions.from(element,carValuation))
            .collect(Collectors.toList());
        carValuation.setOptions(carValuationOptions);

        carValuation.setValuationPrice(valuationEngine.createEstimation(carVersion, createValuationDTO.getNumberOfKilometers()));
        carValuation.setExtraOptionsDepreciationPrice(valuationEngine.getExtraOptionsPrice(carVersion, carValuationOptions));

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(carValuation.getFabricationDate());

        CarValuation savedCarValuation = carValuationRepository.save(carValuation);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_VALUATION",
            "valuation=[VALUATION CREATED] " + carVersion.getVehicleManufacturerName() + " - " + carVersion.getModelName() + " - " + JatoUtil.getFuelType(carVersion.getFuelType()) + " - " +
            JatoUtil.getTransmission(carVersion.getTransmissionType()) + " - " + carValuation.getNumberOfKilometers() + " km " + "-" + savedCarValuation.getId()));

        mailService.senValuationCreatedMail(user, savedCarValuation);
        return savedCarValuation;
    }

    public CarValuation createValuationDE(CreateValuationDTO createValuationDTO, Principal principal) {
        log.debug("Trying to create valuation for: {} vehicleId", createValuationDTO.getVehicleId());

        User user = userRepository
            .findOneByLogin(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        VersionDE carVersion  = Optional
            .ofNullable(versionDERepository.findOneByVehicleId(createValuationDTO.getVehicleId()))
            .orElseThrow(() -> new RuntimeException("Vehicle with id: " + createValuationDTO.getVehicleId() + " doesn't exists!"));

        CarValuation carValuation = CarValuation.from(createValuationDTO, user);
        List<CarValuationOptions> carValuationOptions = new ArrayList<>();
        carValuation.setOptions(carValuationOptions);

        carValuation.setValuationPrice(valuationEngine.createEstimationDE(carVersion, createValuationDTO.getNumberOfKilometers()));
        carValuation.setExtraOptionsDepreciationPrice(0.0f);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(carValuation.getFabricationDate());

        CarValuation savedCarValuation = carValuationRepository.save(carValuation);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_VALUATION",
            "valuation=[VALUATION CREATED] " + carVersion.getVehicleManufacturerName() + " - " + carVersion.getModelName() + " - " + JatoUtil.getFuelType(carVersion.getFuelType()) + " - " +
                JatoUtil.getTransmission(carVersion.getTransmissionType()) + " - " + carValuation.getNumberOfKilometers() + " km " + "-" + savedCarValuation.getId()));

        mailService.senValuationCreatedMail(user, savedCarValuation);
        return savedCarValuation;
    }


        public List<ValuationHistoryDTO> getValuations(Principal principal) {
        log.debug("Trying to get the valuations for user: {}", principal.getName());

        User user = userRepository
            .findOneByLogin(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        List<ValuationHistoryDTO> valuationHistoryDTOS = new ArrayList<>();
        List<CarValuation> valuations = carValuationRepository.findAllByUser(user);

        valuations = Lists.reverse(valuations);
        valuations.forEach(valuation -> {
            VehicleProjection projection = versionRepository.findVehicle(valuation.getVehicleId());
            if (projection == null) {
                projection = versionDERepository.findVehicle(valuation.getVehicleId());

                VehicleDTO vehicleDTO = new VehicleDTO(projection);
                valuationHistoryDTOS.add(new ValuationHistoryDTO(valuation, vehicleDTO));
            } else {
                VehicleDTO vehicleDTO = new VehicleDTO(projection);
                valuationHistoryDTOS.add(new ValuationHistoryDTO(valuation, vehicleDTO));
            }
        });

        return valuationHistoryDTOS;
    }

    @Transactional
    public ValuationHistoryDTO getValuation(Long valuationId, Principal principal) {
        log.debug("Trying to get the valuation with id: {}", valuationId);

        CarValuation valuation = Optional
            .ofNullable(carValuationRepository.findOneById(valuationId))
            .orElseThrow(() -> new RuntimeException("Car valuation not found!"));

        VehicleProjection projection = versionRepository.findVehicle(valuation.getVehicleId());
        VehicleDTO vehicleDTO;

        if (projection == null) {
            projection = versionDERepository.findVehicle(valuation.getVehicleId());
            vehicleDTO = new VehicleDTO(projection);
        } else {
            vehicleDTO = new VehicleDTO(projection);
        }

        if (!valuation.getUser().getLogin().equals(principal.getName())) {
            AtomicBoolean isAdmin = new AtomicBoolean(false);
            User _user = userRepository.findOneByLogin(principal.getName()).get();

            _user.getAuthorities().forEach(authority -> {
                if (authority.getName().equals("ROLE_ADMIN")) {
                    isAdmin.set(true);
                }
            });

            if (!isAdmin.get()) {
                throw new RuntimeException("Not authorized for this valuation!");
            }
        }

        return new ValuationHistoryDTO(valuation, vehicleDTO, imageService.getImageFor(vehicleDTO));
    }

    public List<CarValuationOptions> getValuationOptions(Long valuationId) {
        log.debug("Trying to get extra options for the valuation with id: {}", valuationId);

        CarValuation valuation = Optional
            .ofNullable(carValuationRepository.findOneById(valuationId))
            .orElseThrow(() -> new RuntimeException("Car valuation not found!"));

        return carValuationOptionsRepository.findAllByCarValuation(valuation);
    }

    public List<TimePointDTO> getMasterTypeTimePoints(Long valuationId) {
        CarValuation valuation = Optional
            .ofNullable(carValuationRepository.findOneById(valuationId))
            .orElseThrow(() -> new RuntimeException("Car valuation not found!"));
        VehicleDTO vehicleDTO = new VehicleDTO(versionRepository.findVehicle(valuation.getVehicleId()));

        if (vehicleDTO.getModelYear() < 2014) {
            return new ArrayList<>();
        }

        // find masterType
        MarketMasterType marketMasterType = marketMasterTypeRepository
            .findByManufacturerAndModelAndYear(vehicleDTO.getManufacturer(), vehicleDTO.getModel(), vehicleDTO.getModelYear());


        if (marketMasterType != null) {
            List<TimePointDTO> points = marketDataRepository.findMasterTypeData(
                marketMasterType.getManufacturer(),
                marketMasterType.getModel(),
                marketMasterType.getEngineHp(),
                marketMasterType.getFuelType(),
                vehicleDTO.getModelYear())
                .stream()
                .map(TimePointDTO::new)
                .collect(Collectors.toList());

            return points;
        } else {
            return new ArrayList<>();
        }
    }

    public List<TimePointDTO> getForecastTimePoints(Long valuationId, Float offerPrice) {
        CarValuation valuation = Optional
            .ofNullable(carValuationRepository.findOneById(valuationId))
            .orElseThrow(() -> new RuntimeException("Car valuation not found!"));
        VehicleDTO vehicleDTO = new VehicleDTO(versionRepository.findVehicle(valuation.getVehicleId()));

        if (vehicleDTO.getModelYear() < 2014) {
            return new ArrayList<>();
        }

        List<TimePointDTO> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        float depreciationPercentage = 0.0f;
        float lastValue = offerPrice;

        for (int i=0; i < 12; i++) {
            lastValue = offerPrice - ((offerPrice * depreciationPercentage)/100);

            result.add(new TimePointDTO(
                c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                lastValue
            ));

            c.add(Calendar.MONTH, 1);
            if (i > 8) {
                depreciationPercentage += 0.2;
            } else {
                depreciationPercentage += 0.7;
            }
        }

        return result;
    }

    public AutovitParamsDTO getAutovitParams(Long valuationId) {
        CarValuation valuation = Optional
            .ofNullable(carValuationRepository.findOneById(valuationId))
            .orElseThrow(() -> new RuntimeException("Car valuation not found!"));
        VehicleProjection vehicleProjection = versionRepository.findVehicle(valuation.getVehicleId());

        AutovitParamsDTO autovitParamsDTO = new AutovitParamsDTO();

        autovitParamsDTO.setManufacturer(AutovitUtil.getManufacturerForAutovit(vehicleProjection.getManufacturer()));
        autovitParamsDTO.setModel(AutovitUtil.getModelForAutovit(vehicleProjection.getModel()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(valuation.getFabricationDate());
        autovitParamsDTO.setModelYear(calendar.get(Calendar.YEAR));

        autovitParamsDTO.setNumberOfKilometers(valuation.getNumberOfKilometers());
        autovitParamsDTO.setTransmission(AutovitUtil.getTransmissionTypeForAutovit(vehicleProjection.getTransmission()));

        return autovitParamsDTO;
    }

}
