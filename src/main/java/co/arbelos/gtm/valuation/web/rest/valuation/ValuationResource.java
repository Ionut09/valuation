package co.arbelos.gtm.valuation.web.rest.valuation;


import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.core.domain.valuation.CarValuationOptions;
import co.arbelos.gtm.core.dto.web.valuation.AutovitParamsDTO;
import co.arbelos.gtm.core.dto.web.valuation.CreateValuationDTO;
import co.arbelos.gtm.core.dto.web.valuation.TimePointDTO;
import co.arbelos.gtm.core.dto.web.valuation.ValuationHistoryDTO;
import co.arbelos.gtm.core.dto.web.wizard.BodyDTO;
import co.arbelos.gtm.valuation.config.AuditEventPublisher;
import co.arbelos.gtm.valuation.service.options.OptionsService;
import co.arbelos.gtm.valuation.service.valuation.ValuationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/valuation")
public class ValuationResource {

    private final Logger log = LoggerFactory.getLogger(ValuationResource.class);

    private final ValuationService valuationService;
    private final OptionsService optionsService;

    public ValuationResource(ValuationService valuationService,
                             OptionsService optionsService) {
        this.valuationService = valuationService;
        this.optionsService = optionsService;
    }

    @ApiOperation(
        value = "Create valuation.",
        response = CarValuation.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Create new valuation.")
    })
    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity createEvaluation(@RequestBody CreateValuationDTO createValuationDTO,
                                           @ApiIgnore Principal principal) {
        log.debug("Init valuation creation: {} for: {}", createValuationDTO.getVehicleId(), principal.getName());

        return ResponseEntity.ok(valuationService.createValuation(createValuationDTO, principal));
    }

    @ApiOperation(
        value = "Get all valuations.",
        response = ValuationHistoryDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get all valuations for current user.")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getValuations(@ApiIgnore Principal principal) {
        log.debug("Fetching valuations for: {}", principal.getName());

        return ResponseEntity.ok(valuationService.getValuations(principal));
    }

    @ApiOperation(
        value = "Get specific valuation.",
        response = ValuationHistoryDTO.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get a specific valuation for current user.")
    })
    @GetMapping(path = "{valuationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getValuation(@PathVariable Long valuationId,
                                       @ApiIgnore Principal principal) {
        log.debug("Fetching data for valuation: {}", valuationId);

        return ResponseEntity.ok(valuationService.getValuation(valuationId, principal));
    }

    @ApiOperation(
        value = "Get selected extra options.",
        response = CarValuationOptions.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get selected extra options for a specific valuation.")
    })
    @GetMapping(path = "{valuationId}/extra-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExtraOptions(@PathVariable Long valuationId,
                                          @ApiIgnore Principal principal) {
        log.debug("Fetching extra options for valuation: {}", valuationId);

        return ResponseEntity.ok(valuationService.getValuationOptions(valuationId));
    }

    @ApiOperation(
        value = "Get master type depreciation points.",
        response = TimePointDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get master type depreciation points for a specific valuation.")
    })
    @GetMapping(path = "{valuationId}/master-type", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMasterType(@PathVariable Long valuationId,
                                        @ApiIgnore Principal principal) {
        log.debug("Fetching master-type data for valuation: {}", valuationId);

        return ResponseEntity.ok(valuationService.getMasterTypeTimePoints(valuationId));
    }

    @ApiOperation(
        value = "Get forecast depreciation points.",
        response = TimePointDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get forecast depreciation points for a specific valuation.")
    })
    @GetMapping(path = "{valuationId}/forecast/{offerPrice}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getForecast(@PathVariable Long valuationId,
                                      @PathVariable Float offerPrice,
                                      @ApiIgnore Principal principal) {
        log.debug("Fetching forecast data for valuation: {}", valuationId);

        return ResponseEntity.ok(valuationService.getForecastTimePoints(valuationId, offerPrice));
    }

    @ApiOperation(
        value = "Get autovit link parameters.",
        response = AutovitParamsDTO.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Get autovit link parameters for a specific valuation.")
    })
    @GetMapping(path = "{valuationId}/autovit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAutovitLink(@PathVariable Long valuationId,
                                         @ApiIgnore Principal principal) {
        log.debug("Fetching parameters for autovit similar ads: {}", valuationId);

        return ResponseEntity.ok(valuationService.getAutovitParams(valuationId));
    }
}
