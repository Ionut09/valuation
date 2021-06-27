package co.arbelos.gtm.valuation.web.rest.wizard;


import co.arbelos.gtm.core.dto.web.wizard.*;
import co.arbelos.gtm.core.repository.jato.EquipmentRepository;
import co.arbelos.gtm.core.util.JatoUtil;
import co.arbelos.gtm.valuation.config.AuditEventPublisher;
import co.arbelos.gtm.valuation.service.wizard.WizardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/wizard")
public class WizardResource {

    private final Logger log = LoggerFactory.getLogger(WizardResource.class);

    private final AuditEventPublisher auditPublisher;
    private final WizardService wizardService;
    private final EquipmentRepository equipmentRepository;

    public WizardResource(AuditEventPublisher auditPublisher,
                          WizardService wizardService,
                          EquipmentRepository equipmentRepository) {
        this.auditPublisher = auditPublisher;
        this.wizardService = wizardService;
        this.equipmentRepository = equipmentRepository;
    }

    @ApiOperation(
        value = "Get all manufacturers.",
        response = ManufacturerDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all manufacturers.")
    })
    @GetMapping(path = "/manufacturers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getManufacturers() {
        log.debug("Fetching all manufacturers...");
        return ResponseEntity.ok(wizardService.getManufacturers());
    }

    @ApiOperation(
        value = "Get all models.",
        response = ModelDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all models for a specific manufacturer.")
    })
    @GetMapping(
        path = "/models",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModels(@RequestParam(name = "manufacturer") String manufacturer,
                                    @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                    @ApiIgnore Principal principal) {
        log.debug("Fetching models for {} ...", manufacturer);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_WIZARD", "info=Fetched vehicle models for manufacturer: " + manufacturer));
        return ResponseEntity.ok(wizardService.getModelsFor(manufacturer, date));
    }

    @ApiOperation(
        value = "Get all bodies.",
        response = BodyDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all bodies for a specific manufacturer/model.")
    })
    @GetMapping(
        path = "/bodies",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBodies(@RequestParam(name = "manufacturer")        String manufacturer,
                                    @RequestParam(name = "model")               String model,
                                    @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                    @RequestParam(name = "fuelTypes",           required = false) List<String> fuelTypes,
                                    @RequestParam(name = "transmissionTypes",   required = false) List<String> transmissionTypes,
                                    @RequestParam(name = "drivenWheels",        required = false) List<String> drivenWheels,
                                    @ApiIgnore Principal principal) {
        log.debug("Fetching bodies for {} - {}  ...", manufacturer, model);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_WIZARD", "info=Fetched vehicle bodies for: " + manufacturer + " / " + model));
        return ResponseEntity.ok(wizardService.getBodiesFor(manufacturer, model, date, fuelTypes, transmissionTypes, drivenWheels));
    }

    @ApiOperation(
        value = "Get all generations.",
        response = GenerationDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all generations for a specific manufacturer/model/body/doorsNo.")
    })
    @GetMapping(
        path = "/generations",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getGenerations(@RequestParam(name = "manufacturer")   String manufacturer,
                                         @RequestParam(name = "model")          String model,
                                         @RequestParam(name = "body")           String body,
                                         @RequestParam(name = "doorsNo")        Integer doorsNo,
                                         @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                         @RequestParam(name = "fuelTypes",           required = false) List<String> fuelTypes,
                                         @RequestParam(name = "transmissionTypes",   required = false) List<String> transmissionTypes,
                                         @RequestParam(name = "drivenWheels",        required = false) List<String> drivenWheels,
                                         @ApiIgnore Principal principal) {
        log.debug("Fetching generations for {} - {} - {} ...", manufacturer, model, body);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_WIZARD", "info=Fetched vehicle generations for: "+manufacturer+ " / " + model + " of body: " + body + " with doors: " + doorsNo));
        return ResponseEntity.ok(wizardService.getGenerationsFor(manufacturer, model, body, doorsNo, date, fuelTypes, transmissionTypes, drivenWheels));
    }

    @ApiOperation(
        value = "Get all versions.",
        response = String.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all versions for a specific manufacturer/model")
    })
    @GetMapping(
        path = "/versions",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVersions(@RequestParam(name = "manufacturer") String manufacturer,
                                      @RequestParam(name = "model") String model,
                                      @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                      @ApiIgnore Principal principal) {
        log.debug("Fetching vehicles for {} - {} - {} - {} ...", manufacturer, model);
        auditPublisher
            .publish(new AuditEvent(principal.getName(), "API_WIZARD", "Fetched all versions for: " + manufacturer + " / " + model + " / "));
        return ResponseEntity.ok(
            wizardService.getDistinctVersionsForModel(
                manufacturer,
                model,
                date));
    }


    @ApiOperation(
        value = "Get all vehicles.",
        response = VehicleDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all vehicles for a specific manufacturer/model/body/doorsNo/generation")
    })
    @GetMapping(
        path = "/vehicles",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVehicles(@RequestParam(name = "manufacturer")   String manufacturer,
                                      @RequestParam(name = "model")          String model,
                                      @RequestParam(name = "body")           String body,
                                      @RequestParam(name = "doorsNo")        Integer doorsNo,
                                      @RequestParam(name = "generation")     Integer generations,
                                      @RequestParam(name = "date",                required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                      @RequestParam(name = "fuelTypes",           required = false) List<String> fuelTypes,
                                      @RequestParam(name = "transmissionTypes",   required = false) List<String> transmissionTypes,
                                      @RequestParam(name = "drivenWheels",        required = false) List<String> drivenWheels,
                                      @RequestParam(name = "version",        required = false) String version,
                                      @ApiIgnore Principal principal) {
        log.debug("Fetching vehicles for {} - {} - {} - {} ...", manufacturer, model, body, generations);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_WIZARD", "info=[LAST STEP]Fetched all vehicles of: " + manufacturer + " / " + model + " / "));
        return ResponseEntity.ok(
            wizardService.getVehiclesFor(
                manufacturer,
                model,
                body,
                doorsNo,
                generations,
                date,
                fuelTypes,
                transmissionTypes,
                drivenWheels,
                version
            )
            .stream()
            .peek(el -> el.setCc(equipmentRepository.getCC(el.getVehicleId().toString())))
            .collect(toList())
        );
    }

    @ApiOperation(
        value = "Get all vehicles through VIN.",
        response = VehicleDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "[VinQuery FLOW] Returns the list of all vehicles for a specific manufacturer/model/body/doorsNo/generation")
    })
    @GetMapping(
        path = "/vehicles-vin",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getVehiclesFromVin(@RequestParam(name = "manufacturer")   String manufacturer,
                                             @RequestParam(name = "model")          String model,
                                             @RequestParam(name = "body",       required = false)   String body,
                                             @RequestParam(name = "doorsNo",    required = false)   Integer doorsNo,
                                             @RequestParam(name = "date",       required = false)   @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                             @RequestParam(name = "registration",required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date registration,
                                             @RequestParam(name = "fuelType",   required = false)   String fuelType,
                                             @RequestParam(name = "horsePower", required = false)   Integer horsePower,
                                             @RequestParam(name = "engineKw",   required = false)   Integer engineKw,
                                             @RequestParam(name = "ampatament",   required = false)  String ampatament,
                                             @RequestParam(name = "engineManufactureCode",   required = false)  String engineManufactureCode,
                                             @RequestParam(name = "gearbox",   required = false)  String gearbox,
                                             @RequestParam(name = "cc",   required = false)  Integer cc,
                                             @ApiIgnore Principal principal) {
        log.debug("[VinQuery] Fetching vehicles for {} - {} - {} ...", manufacturer, model, body);
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_WIZARD", "info=[LAST STEP]Fetched all vehicles of: " + manufacturer + " / " + model + " / "));
        if (ampatament == null) {
            if (engineManufactureCode == null) {
                return ResponseEntity.ok(
                    wizardService.getVehiclesFor(
                        manufacturer,
                        model,
                        registration
                    )
                    .stream()
                    .filter(el -> {
                        if (doorsNo == null) {
                            return true;
                        } else {
                            return el.getNoOfDoors().equals(doorsNo);
                        }
                    })
                    .filter(el -> {
                        if(fuelType == null) {
                            return true;
                        } else {
                            return fuelType.equals(el.getFuelType());
                        }
                    })
                    .filter(el -> {
                        if (cc == null) {
                            return true;
                        } else {
                            final Integer vehicleCC = equipmentRepository.getCC(el.getVehicleId().toString());
                            if (cc > (vehicleCC - 10) && cc < (vehicleCC + 10)) {
                                el.setCc(vehicleCC);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    })
                    .filter(el -> {
                        if (engineKw == null) {
                            return true;
                        } else {
                            return (el.getKw() > engineKw - 2) && (el.getKw() < engineKw + 2);
                        }
                    })
                    .filter(el -> {
                        if (gearbox == null) {
                            return true;
                        } else {
                            if (gearbox.equalsIgnoreCase("MANUAL")) {
                                return el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL");
                            } else {
                                return !el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL");
                            }
                        }
                    })
                    .filter(el -> {
                        if (el.getConvertedStartSellingDate().after(registration)) {
                            return false;
                        } else {
                            return true;
                        }
                    })
                    .filter(el -> {
                        if (body != null) {
                            if (!el.getBody().equals("UNDEFINED")) {
                                return el.getBody().equals(body);
                            } else {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    })
                    .collect(toList())
                );
            } else {
                return ResponseEntity.ok(
                    wizardService.getVehiclesFor(
                        manufacturer,
                        model,
                        registration
                    )
                    .stream()
                    .filter(el -> {
                        if (doorsNo == null) {
                            return true;
                        } else {
                            return el.getNoOfDoors().equals(doorsNo);
                        }
                    })
                    .filter(el -> {
                        if(fuelType == null) {
                            return true;
                        } else {
                            return JatoUtil.getFuelTypeCode(fuelType).equals(el.getFuelType());
                        }
                    })
                    .filter(el -> {
                        if (cc == null) {
                            return true;
                        } else {
                            final Integer vehicleCC = equipmentRepository.getCC(el.getVehicleId().toString());
                            return (cc > (vehicleCC - 10) && cc < (vehicleCC + 10));
                        }
                    })
                    .filter(el -> {
                        if (engineKw == null) {
                            return true;
                        } else {
                            return (el.getKw() > engineKw - 2) && (el.getKw() < engineKw + 2);
                        }
                    })
                    .filter(el -> {
                        if (gearbox == null) {
                            return true;
                        } else {
                            if (gearbox.equalsIgnoreCase("MANUAL")) {
                                if (el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL")) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                if (el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL")) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                    })
                    .filter(vehicle -> {
                        if (vehicle.getEngineCode() != null) {
                            if (vehicle.getEngineCode().equals(engineManufactureCode)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    })
                    .filter(el -> {
                        if (el.getConvertedStartSellingDate().after(registration)) {
                            return false;
                        } else {
                            return true;
                        }
                    })
                    .filter(el -> {
                        if (!el.getBody().equals("UNDEFINED")) {
                            if (el.getBody().equals(body)) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    })
                    .collect(toList())
                );
            }
        } else {
            return ResponseEntity.ok(
                wizardService.getVehiclesFor(
                    manufacturer,
                    model,
                    registration
                )
                .stream()
                .filter(el -> {
                    if (doorsNo == null) {
                        return true;
                    } else {
                        return el.getNoOfDoors().equals(doorsNo);
                    }
                })
                .filter(el -> {
                    if(fuelType == null) {
                        return true;
                    } else {
                        return JatoUtil.getFuelTypeCode(fuelType).equals(el.getFuelType());
                    }
                })
                .filter(el -> {
                    if (cc == null) {
                        return true;
                    } else {
                        final Integer vehicleCC = equipmentRepository.getCC(el.getVehicleId().toString());
                        return (cc > (vehicleCC - 10) && cc < (vehicleCC + 10));
                    }
                })
                .filter(el -> {
                    if (engineKw == null) {
                        return true;
                    } else {
                        return (el.getKw() > engineKw - 2) && (el.getKw() < engineKw + 2);
                    }
                })
                .filter(el -> {
                    if (gearbox == null) {
                        return true;
                    } else {
                        if (gearbox.equalsIgnoreCase("MANUAL")) {
                            if (el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL")) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            if (el.getTransmissionDescription().replaceAll("(\\r|\\n)", "").equalsIgnoreCase("MANUAL")) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                })
                .filter(el -> {
                    if (el.getConvertedStartSellingDate().after(registration)) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .filter(el -> {
                    if (!el.getBody().equals("UNDEFINED")) {
                        if (el.getBody().equals(body)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                })
                .collect(toList())
            );
        }
    }

    @ApiOperation(
        value = "Get all fuels.",
        response = String.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all fuel types for a specific manufacturer/model")
    })
    @GetMapping(
        path = "/fuels",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFuelTypes(@RequestParam(name = "manufacturer")   String manufacturer,
                                       @RequestParam(name = "model")          String model,
                                       @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.debug("Fetching fuel types for {} - {}  ...", manufacturer, model);
        return ResponseEntity.ok(wizardService.getFuelTypes(manufacturer, model, date));
    }

    @ApiOperation(
        value = "Get all transmissions.",
        response = String.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all transmission types for a specific manufacturer/model/fuel")
    })
    @GetMapping(
        path = "/transmissions",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTransmissions(@RequestParam(name = "manufacturer")   String manufacturer,
                                           @RequestParam(name = "model")          String model,
                                           @RequestParam(name = "fuel")           String fuel,
                                           @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.debug("Fetching transmission types for {} - {} - {} ...", manufacturer, model, fuel);
        return ResponseEntity.ok(wizardService.getTransmissionTypes(manufacturer, model, fuel, date));
    }

    @ApiOperation(
        value = "Get all driven-wheels.",
        response = String.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all driven-wheels types for a specific manufacturer/model/fuel/transmission")
    })
    @GetMapping(
        path = "/driven-wheels",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDrivenWheels(@RequestParam(name = "manufacturer")   String manufacturer,
                                          @RequestParam(name = "model")          String model,
                                          @RequestParam(name = "fuel")           String fuel,
                                          @RequestParam(name = "transmission")   String transmission,
                                          @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        log.debug("Fetching driven wheel types for {} - {} - {} - {} ...", manufacturer, model, fuel, transmission);
        return ResponseEntity.ok(wizardService.getDrivenWheels(manufacturer, model, fuel, transmission, date));
    }

    @Deprecated
    @GetMapping("/transmission-description/{vehicleId}")
    public ResponseEntity getTransmissionDescription(@PathVariable Long vehicleId) {
        log.debug("Fetching tranmission description for: {} vehicle", vehicleId);
        return ResponseEntity.ok(wizardService.getTransmissionDescription(vehicleId));
    }
}
