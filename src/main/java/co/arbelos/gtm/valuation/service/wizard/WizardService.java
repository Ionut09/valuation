package co.arbelos.gtm.valuation.service.wizard;

import co.arbelos.gtm.core.dao.wizard.VehicleProjection;
import co.arbelos.gtm.core.dto.web.wizard.*;
import co.arbelos.gtm.core.repository.jato.SchemaTextRepository;
import co.arbelos.gtm.core.repository.jato.VersionRepository;
import co.arbelos.gtm.core.repository.jato.germandb.VersionDERepository;
import co.arbelos.gtm.core.util.JatoUtil;
import co.arbelos.gtm.valuation.service.images.ImageService;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Calendar.MONTH;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static java.util.Calendar.JUNE;

@Service
public class WizardService {

    private final Logger log = LoggerFactory.getLogger(WizardService.class);

    private final VersionRepository versionRepository;
    private final VersionDERepository versionDERepository;
    private final SchemaTextRepository schemaTextRepository;
    private final ImageService imageService;

    public WizardService(VersionRepository versionRepository,
                         VersionDERepository versionDERepository,
                         SchemaTextRepository schemaTextRepository,
                         ImageService imageService) {
        this.versionRepository = versionRepository;
        this.versionDERepository = versionDERepository;
        this.schemaTextRepository = schemaTextRepository;
        this.imageService = imageService;
    }

    public List<ManufacturerDTO> getManufacturers() {
        List<String> results = Optional
            .ofNullable(versionRepository.findManufacturers())
            .orElseThrow(() -> new RuntimeException("No manufacturers found in database!"));

        log.debug("Returning {} manufacturers!", results.size());

        return results
            .stream()
            .map(ManufacturerDTO::new)
            .collect(Collectors.toList());
    }

    public List<ModelDTO> getModelsFor(String manufacturerName) {
        if (manufacturerName.length() > 0) {
            final String sanitizedManufacturerName = StringEscapeUtils.escapeSql(manufacturerName).toUpperCase();

            List<ModelDTO> results =  Optional
                .ofNullable(versionRepository.findModels(sanitizedManufacturerName))
                .orElseThrow(() -> new RuntimeException("No models of type " + sanitizedManufacturerName + " found in database!"))
                .stream()
                .map(ModelDTO::new)
                .collect(Collectors.toList());

            results.forEach(result -> {
                result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), YearMonth.now().getYear()));
            });

            log.debug("Returning {} models!", results.size());


            return results;
        }

        return new ArrayList<>();
    }

    /**
     * @param manufacturerName
     * @param date - currently manufacturing date
     */
    public List<ModelDTO> getModelsFor(String manufacturerName, Date date) {
        if (date == null) {
            return getModelsFor(manufacturerName);
        }

        // if the vehicle's manufacturing year is < 2008 go to the german DB
        Date thresholdDE = new GregorianCalendar(2008, JUNE, 1).getTime();
        if (date.before(thresholdDE)) {
            return getModelsForFromGermanDB(manufacturerName, date);
        }

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<ModelDTO> results =  Optional
            .ofNullable(versionRepository.findModels(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(() -> new RuntimeException("No models of type " + manufacturerName + " found in database!"))
            .stream()
            .map(ModelDTO::new)
            .collect(Collectors.toList());

        results.forEach(result -> {
            result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), getYearFrom(date)));
        });

        log.debug("Returning {} models!", results.size());

        return results;
    }

    public List<ModelDTO> getModelsForFromGermanDB(String manufacturerName, Date date) {
        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<ModelDTO> results =  Optional
            .ofNullable(versionDERepository.findModels(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(() -> new RuntimeException("[GermanDB] No models of type " + manufacturerName + " found in database!"))
            .stream()
            .map(ModelDTO::new)
            .collect(Collectors.toList());

        results.forEach(result -> {
            result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), getYearFrom(date)));
        });

        log.debug("[GermanDB] Returning {} models!", results.size());

        return results;
    }

    public List<BodyDTO> getBodiesFor(String manufacturerName, String model) {
        if (manufacturerName.length() >  0 && model.length() >  0) {

            List<BodyDTO> results =  Optional
                .ofNullable(versionRepository.findBodies(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase()
                ))
                .orElseThrow(
                    () -> new RuntimeException("No bodies for manufacturer " + manufacturerName + " of model " + model + " found in database!")
                )
                .stream()
                .map(BodyDTO::new)
                .collect(Collectors.toList());

            results.forEach(result -> {
                result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), YearMonth.now().getYear(), result.getDoorsNo(), result.getBody()));
            });

            log.debug("Returning {} bodies!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<BodyDTO> getBodiesFor(String manufacturerName, String model, Date date) {
        if (date == null) {
            return getBodiesFor(manufacturerName, model);
        }

        // if the vehicle's manufacturing year is < 2008 go to the german DB
        Date thresholdDE = new GregorianCalendar(2008, JUNE, 1).getTime();
        if (date.before(thresholdDE)) {
            return getBodiesForFromGermanDB(
                manufacturerName,
                model,
                date);
        }

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<BodyDTO> results =  Optional
            .ofNullable(versionRepository.findBodies(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(
                () -> new RuntimeException("No bodies for manufacturer " + manufacturerName + " of model " + model + " found in database!")
            )
            .stream()
            .map(BodyDTO::new)
            .collect(Collectors.toList());

        results.forEach(result -> {
            result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), getYearFrom(date), result.getDoorsNo(), result.getBody()));
        });

        log.debug("Returning {} bodies!", results.size());

        return results;
    }

    public List<BodyDTO> getBodiesFor(String manufacturerName,
                                      String model,
                                      Date date,
                                      List<String> fuelTypes,
                                      List<String> transmissionTypes,
                                      List<String> drivenWheels) {
        if (date == null) {
            return getBodiesFor(manufacturerName, model);
        }

        if ((fuelTypes == null && transmissionTypes == null && drivenWheels == null)) {
            return getBodiesFor(manufacturerName, model, date);
        }

        List<String> fTypes = toFuelTypeCode(fuelTypes);
        List<String> tTypes = toTransmissionCode(transmissionTypes);
        List<String> dWheels = toDrivenWheelsCode(drivenWheels);

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<BodyDTO> results =  Optional
            .ofNullable(versionRepository.findBodies(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                startDate,
                endDate,
                fTypes,
                tTypes,
                dWheels
            ))
            .orElseThrow(
                () -> new RuntimeException("No bodies for manufacturer " + manufacturerName + " of model " + model + " found in database!")
            )
            .stream()
            .map(BodyDTO::new)
            .collect(Collectors.toList());

        results.forEach(result -> {
            result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), getYearFrom(date), result.getDoorsNo(), result.getBody()));
        });

        log.debug("Returning {} bodies!", results.size());

        return results;
    }

    public List<BodyDTO> getBodiesForFromGermanDB(String manufacturerName,
                                      String model,
                                      Date date) {
        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<BodyDTO> results =  Optional
            .ofNullable(versionDERepository.findBodies(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(
                () -> new RuntimeException("[German DB] No bodies for manufacturer " + manufacturerName + " of model " + model + " found in database!")
            )
            .stream()
            .map(BodyDTO::new)
            .collect(Collectors.toList());

        results.forEach(result -> {
            result.setImageUrl(imageService.getImageFor(manufacturerName, result.getModel(), getYearFrom(date), result.getDoorsNo(), result.getBody()));
        });

        log.debug("[German DB] Returning {} bodies!", results.size());

        return results;
    }

    public List<GenerationDTO> getGenerationsFor(String manufacturerName, String model, String body, Integer doorsNo) {
        if (manufacturerName.length() >  0 && model.length() >  0 && body.length() > 0) {

            List<GenerationDTO> results =  Optional
                .ofNullable(versionRepository.findGenerations(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    JatoUtil.getBodyTypeCode(body),
                    doorsNo
                ))
                .orElseThrow(
                    () -> new RuntimeException("No generations for manufacturer " + manufacturerName + " of model " + model + " of body " + body + " found in database!")
                )
                .stream()
                .map(GenerationDTO::new)
                .collect(Collectors.toList());

            log.debug("Returning {} generations!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<GenerationDTO> getGenerationsFor(String manufacturerName,
                                                 String model,
                                                 String body,
                                                 Integer doorsNo,
                                                 Date date) {
        if (date == null) {
            return getGenerationsFor(manufacturerName, model, body, doorsNo);
        }

        Date thresholdDE = new GregorianCalendar(2008, JUNE, 1).getTime();
        if (date.before(thresholdDE)) {
            return getGenerationsForFromGermanDB(manufacturerName, model, body, doorsNo, date);
        }

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<GenerationDTO> results =  Optional
            .ofNullable(versionRepository.findGenerations(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                JatoUtil.getBodyTypeCode(body),
                doorsNo,
                startDate,
                endDate
            ))
            .orElseThrow(
                () -> new RuntimeException("No generations for manufacturer " + manufacturerName + " of model " + model + " of body " + body + " found in database!")
            )
            .stream()
            .map(GenerationDTO::new)
            .collect(Collectors.toList());

        log.debug("Returning {} generations!", results.size());

        return results;
    }

    public List<GenerationDTO> getGenerationsForFromGermanDB(String manufacturerName, String model, String body, Integer doorsNo, Date date) {
        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<GenerationDTO> results =  Optional
            .ofNullable(versionDERepository.findGenerations(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                JatoUtil.getBodyTypeCode(body),
                doorsNo,
                startDate,
                endDate
            ))
            .orElseThrow(
                () -> new RuntimeException("[German DB] No generations for manufacturer " + manufacturerName + " of model " + model + " of body " + body + " found in database!")
            )
            .stream()
            .map(GenerationDTO::new)
            .collect(Collectors.toList());

        log.debug("[German DB] Returning {} generations!", results.size());

        return results;
    }
    public List<GenerationDTO> getGenerationsFor(String manufacturerName,
                                                 String model,
                                                 String body,
                                                 Integer doorsNo,
                                                 Date date,
                                                 List<String> fuelTypes,
                                                 List<String> transmissionTypes,
                                                 List<String> drivenWheels) {
        if (date == null) {
            return getGenerationsFor(manufacturerName, model, body, doorsNo);
        }

        if ((fuelTypes == null && transmissionTypes == null && drivenWheels == null)) {
            return getGenerationsFor(manufacturerName, model, body, doorsNo, date);
        }

        List<String> fTypes = toFuelTypeCode(fuelTypes);
        List<String> tTypes = toTransmissionCode(transmissionTypes);
        List<String> dWheels = toDrivenWheelsCode(drivenWheels);

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<GenerationDTO> results =  Optional
            .ofNullable(versionRepository.findGenerations(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                JatoUtil.getBodyTypeCode(body),
                doorsNo,
                startDate,
                endDate,
                fTypes,
                tTypes,
                dWheels
            ))
            .orElseThrow(
                () -> new RuntimeException("No generations for manufacturer " + manufacturerName + " of model " + model + " of body " + body + " found in database!")
            )
            .stream()
            .map(GenerationDTO::new)
            .collect(Collectors.toList());

        log.debug("Returning {} generations!", results.size());

        return results;
    }

    public List<String> getDistinctVersionsForModel(String manufacturer, String model, Date date) {
        return versionRepository.findDistinctVersions(manufacturer, model, convertDate(date));
    }

    public List<VehicleDTO> getVehiclesFor(String manufacturerName, String model, String body, Integer doorsNo, Integer generation, Date date, String version) {
        Date thresholdDE = new GregorianCalendar(2008, Calendar.JUNE, 1).getTime();
        if (date.before(thresholdDE)) {
            return getVehiclesForFromGermanDB(manufacturerName, model, body, doorsNo, generation, date);
        }

        if (manufacturerName.length() > 0 && model.length() > 0 && body.length() > 0 && generation >= 0) {


            String startDate = new SimpleDateFormat("yyyMMdd").format(date);
            String endDate = new SimpleDateFormat("yyyMMdd").format(date);

            Stream<VehicleProjection> stream = Optional
                .ofNullable(versionRepository.findVehicles(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    JatoUtil.getBodyTypeCode(body),
                    doorsNo,
                    generation,
                    startDate,
                    endDate
                ))
                .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                .stream();

//            if (isNotBlank(version)){
//                stream.filter(vehicleProjection -> version.equals(vehicleProjection.getVersion()));
//            }

            List<VehicleDTO> results = stream
                .filter(vehicleProjection -> version.equals(vehicleProjection.getVersion()))
                .map(VehicleDTO::new)
                .collect(Collectors.toList());

            log.debug("Returning {} vehicles!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<VehicleDTO> getVehiclesForFromGermanDB(String manufacturerName, String model, String body, Integer doorsNo, Integer generation, Date date) {
        if (manufacturerName.length() > 0 && model.length() > 0 && body.length() > 0 && generation >= 0) {

            String startDate = new SimpleDateFormat("yyyMMdd").format(date);
            String endDate = new SimpleDateFormat("yyyMMdd").format(date);

            List<VehicleDTO> results = Optional
                .ofNullable(versionDERepository.findVehicles(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    JatoUtil.getBodyTypeCode(body),
                    doorsNo,
                    generation,
                    startDate,
                    endDate
                ))
                .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                .stream()
                .map(VehicleDTO::new)
                .collect(Collectors.toList());

            log.debug("Returning {} vehicles!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<VehicleDTO> getVehiclesFor(String manufacturerName,
                                           String model,
                                           String body,
                                           Integer doorsNo,
                                           Integer generation,
                                           Date date,
                                           List<String> fuelTypes,
                                           List<String> transmissionTypes,
                                           List<String> drivenWheels,
                                           String version) {
        if ((fuelTypes == null) && (transmissionTypes == null) && (drivenWheels == null)) {
            return getVehiclesFor(manufacturerName, model, body, doorsNo, generation, date, version);
        }

        if (manufacturerName.length() > 0 && model.length() > 0 && body.length() > 0 && generation >= 0) {

            List<String> fTypes = toFuelTypeCode(fuelTypes);
            List<String> tTypes = toTransmissionCode(transmissionTypes);
            List<String> dWheels = toDrivenWheelsCode(drivenWheels);

            String startDate = new SimpleDateFormat("yyyMMdd").format(date);
            String endDate = new SimpleDateFormat("yyyMMdd").format(date);

            Stream<VehicleProjection> stream = Optional
                .ofNullable(versionRepository.findVehicles(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    JatoUtil.getBodyTypeCode(body),
                    doorsNo,
                    generation,
                    startDate,
                    endDate,
                    fTypes,
                    tTypes,
                    dWheels
                ))
                .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                .stream();

            if (isNotBlank(version)){
                stream.filter(vehicleProjection -> version.equals(vehicleProjection.getVersion()));
            }
            List<VehicleDTO> results = stream.map(VehicleDTO::new)
                                             .collect(Collectors.toList());

            log.debug("Returning {} vehicles!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<VehicleDTO> getVehiclesFor(
        String manufacturerName,
        String model,
        Date registration) {

        return Optional
            .ofNullable(versionRepository.findVehicles(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                extractMonths(registration, -1),
                extractMonths(registration, 3)
            ))
            .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
            .stream()
            .map(VehicleDTO::new)
            .collect(Collectors.toList());
    }

    public List<VehicleDTO> getVehiclesFor(
        String manufacturerName,
        String model,
        String body,
        Integer doorsNo,
        Date date,
        Date registration,
        String fuelType,
        Integer horsePower,
        Integer engineKw) {
        if (manufacturerName != null && model != null) {
            List<VehicleDTO> results;

            results = Optional
                .ofNullable(versionRepository.findVehicles(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    doorsNo,
                    extractMonths(registration, -1),
                    extractMonths(registration, 3),
                    JatoUtil.getFuelTypeCode(fuelType).equals("UNDEFINED") ? null : JatoUtil.getFuelTypeCode(fuelType),
                    engineKw
                ))
                .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                .stream()
                .map(VehicleDTO::new)
                .collect(Collectors.toList());

            if (results.size() == 0) {
                results = Optional
                    .ofNullable(versionRepository.findVehicles(
                        StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                        StringEscapeUtils.escapeSql(model).toUpperCase(),
                        doorsNo - 1,
                        extractMonths(registration, -1),
                        extractMonths(registration, 3),
                        JatoUtil.getFuelTypeCode(fuelType),
                        engineKw
                    ))
                    .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                    .stream()
                    .map(VehicleDTO::new)
                    .collect(Collectors.toList());
            }

            if (results.size() == 0) {
                results = Optional
                    .ofNullable(versionRepository.findVehicles(
                        StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                        StringEscapeUtils.escapeSql(model).toUpperCase(),
                        doorsNo + 1,
                        extractMonths(registration, -1),
                        extractMonths(registration, 3),
                        JatoUtil.getFuelTypeCode(fuelType),
                        engineKw
                    ))
                    .orElseThrow(() -> new RuntimeException("No vehicles found in database for provided criteria!"))
                    .stream()
                    .map(VehicleDTO::new)
                    .collect(Collectors.toList());
            }

            log.debug("Returning {} vehicles!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<String> getFuelTypes(String manufacturerName, String model) {
        if (manufacturerName.length() > 0 && model.length() > 0) {
            List<String> results = Optional
                .ofNullable(versionRepository.findFuelTypes(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase()
                ))
                .orElseThrow(() -> new RuntimeException("No fuel types found in database for provided criteria!"))
                .stream()
                .map(JatoUtil::getFuelType)
                .filter(item -> !item.equals("Premium Unleaded"))
                .collect(Collectors.toList());

            log.debug("Returning {} fuel types!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<String> getFuelTypes(String manufacturerName, String model, Date date) {
        if (date == null) {
            return getFuelTypes(manufacturerName, model);
        }

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<String> results = Optional
            .ofNullable(versionRepository.findFuelTypes(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(() -> new RuntimeException("No fuel types found in database for provided criteria!"))
            .stream()
            .map(JatoUtil::getFuelType)
            .filter(item -> !item.equals("Premium Unleaded"))
            .collect(Collectors.toList());

        log.debug("Returning {} fuel types!", results.size());

        return results;
    }

    public List<String> getTransmissionTypes(String manufacturerName, String model, String fuelType) {
        if (manufacturerName.length() > 0 && model.length() > 0 && fuelType.length() > 0) {
            final String fuelTypeCode = JatoUtil.getFuelTypeCode(fuelType);

            List<String> results = Optional
                .ofNullable(versionRepository.findTransmissions(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    StringEscapeUtils.escapeSql(fuelTypeCode).toUpperCase()
                ))
                .orElseThrow(() -> new RuntimeException("No transmission types found in database for provided criteria!"))
                .stream()
                .map(JatoUtil::getTransmission)
                .collect(Collectors.toList());

            if (fuelTypeCode.equals("U")) {
                List<String> extraResults = Optional
                    .ofNullable(versionRepository.findTransmissions(
                        StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                        StringEscapeUtils.escapeSql(model).toUpperCase(),
                        StringEscapeUtils.escapeSql("P")
                    ))
                    .orElseThrow(() -> new RuntimeException("No transmission types found in database for provided criteria!"))
                    .stream()
                    .map(JatoUtil::getTransmission)
                    .collect(Collectors.toList());

                results.addAll(extraResults);
            }

            log.debug("Returning {} transmission types!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<String> getTransmissionTypes(String manufacturerName, String model, String fuelType, Date date) {
        if(date == null) {
            return getTransmissionTypes(manufacturerName, model, fuelType);
        }

        final String fuelTypeCode = JatoUtil.getFuelTypeCode(fuelType);

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<String> results = Optional
            .ofNullable(versionRepository.findTransmissions(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                StringEscapeUtils.escapeSql(fuelTypeCode).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(() -> new RuntimeException("No transmission types found in database for provided criteria!"))
            .stream()
            .map(JatoUtil::getTransmission)
            .collect(Collectors.toList());

        if (fuelTypeCode.equals("U")) {
            List<String> extraResults = Optional
                .ofNullable(versionRepository.findTransmissions(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    StringEscapeUtils.escapeSql("P")
                ))
                .orElseThrow(() -> new RuntimeException("No transmission types found in database for provided criteria!"))
                .stream()
                .map(JatoUtil::getTransmission)
                .collect(Collectors.toList());

            results.addAll(extraResults);
        }

        log.debug("Returning {} transmission types!", results.size());

        return results;

    }

    public List<String> getDrivenWheels(String manufacturerName, String model, String fuelType, String transmission) {
        if (manufacturerName.length() > 0 && model.length() > 0 &&
            fuelType.length() > 0 && transmission.length() > 0) {

            final String fuelTypeCode       = JatoUtil.getFuelTypeCode(fuelType);
            final String transmissionCode   = JatoUtil.getTransmissionCode(transmission);

            List<String> results = Optional
                .ofNullable(versionRepository.findDrivenWheels(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    StringEscapeUtils.escapeSql(fuelTypeCode).toUpperCase(),
                    StringEscapeUtils.escapeSql(transmissionCode).toUpperCase()
                ))
                .orElseThrow(() -> new RuntimeException("No driven wheels found in database for provided criteria!"))
                .stream()
                .map(JatoUtil::getDrivenWheels)
                .collect(Collectors.toList());

            if (fuelTypeCode.equals("U")) {
                List<String> extraData = Optional
                    .ofNullable(versionRepository.findDrivenWheels(
                        StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                        StringEscapeUtils.escapeSql(model).toUpperCase(),
                        StringEscapeUtils.escapeSql("P").toUpperCase(),
                        StringEscapeUtils.escapeSql(transmissionCode).toUpperCase()
                    ))
                    .orElseThrow(() -> new RuntimeException("No driven wheels found in database for provided criteria!"))
                    .stream()
                    .map(JatoUtil::getDrivenWheels)
                    .collect(Collectors.toList());

                results.addAll(extraData);
            }

            log.debug("Returning {} driven wheels types!", results.size());

            return results;
        }

        return new ArrayList<>();
    }

    public List<String> getDrivenWheels(String manufacturerName, String model, String fuelType, String transmission, Date date) {
        if (date == null) {
            return getDrivenWheels(manufacturerName, model, fuelType, transmission);
        }

        final String fuelTypeCode       = JatoUtil.getFuelTypeCode(fuelType);
        final String transmissionCode   = JatoUtil.getTransmissionCode(transmission);

        String startDate = new SimpleDateFormat("yyyMMdd").format(date);
        String endDate = new SimpleDateFormat("yyyMMdd").format(date);

        List<String> results = Optional
            .ofNullable(versionRepository.findDrivenWheels(
                StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                StringEscapeUtils.escapeSql(model).toUpperCase(),
                StringEscapeUtils.escapeSql(fuelTypeCode).toUpperCase(),
                StringEscapeUtils.escapeSql(transmissionCode).toUpperCase(),
                startDate,
                endDate
            ))
            .orElseThrow(() -> new RuntimeException("No driven wheels found in database for provided criteria!"))
            .stream()
            .map(JatoUtil::getDrivenWheels)
            .collect(Collectors.toList());

        if (fuelTypeCode.equals("U")) {
            List<String> extraData = Optional
                .ofNullable(versionRepository.findDrivenWheels(
                    StringEscapeUtils.escapeSql(manufacturerName).toUpperCase(),
                    StringEscapeUtils.escapeSql(model).toUpperCase(),
                    StringEscapeUtils.escapeSql("P").toUpperCase(),
                    StringEscapeUtils.escapeSql(transmissionCode).toUpperCase()
                ))
                .orElseThrow(() -> new RuntimeException("No driven wheels found in database for provided criteria!"))
                .stream()
                .map(JatoUtil::getDrivenWheels)
                .collect(Collectors.toList());

            results.addAll(extraData);
        }

        log.debug("Returning {} driven wheels types!", results.size());

        return results;
    }

    public List<TransmissionDescriptionDTO> getTransmissionDescription(Long vehicleId) {
        return Optional
            .ofNullable(schemaTextRepository.findTransmissionDescription(vehicleId))
            .orElseThrow(() -> new RuntimeException("No transmission description found!"))
            .stream()
            .map(TransmissionDescriptionDTO::new)
            .collect(Collectors.toList());
    }

    private List<String> toFuelTypeCode(List<String> fullVersion) {
        return fullVersion.stream()
            .map(JatoUtil::getFuelTypeCode)
            .collect(Collectors.toList());
    }

    private List<String> toTransmissionCode(List<String> fullVersion) {
        return fullVersion.stream()
            .map(JatoUtil::getTransmissionCode)
            .collect(Collectors.toList());
    }

    private List<String> toDrivenWheelsCode(List<String> fullVersion) {
        return fullVersion.stream()
            .map(JatoUtil::getDrivenWheelCodes)
            .collect(Collectors.toList());
    }

    private String extractMonths(Date date, int noOfMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(MONTH, noOfMonths);

        return new SimpleDateFormat("yyyMMdd").format(calendar.getTime());
    }

    private String convertDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMdd");
        return dateFormat.format(calendar.getTime());
    }

    private String getYearFrom(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return String.valueOf(calendar.get(Calendar.YEAR));
    }
}
