package co.arbelos.gtm.valuation.web.rest.options;

import co.arbelos.gtm.core.dto.web.options.ExtraOptionDTO;
import co.arbelos.gtm.core.dto.web.options.StandardOptionDTO;
import co.arbelos.gtm.core.dto.web.wizard.ModelDTO;
import co.arbelos.gtm.valuation.service.options.OptionsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/options")
public class OptionsResource {

    private final Logger log = LoggerFactory.getLogger(OptionsResource.class);

    private final OptionsService optionsService;

    public OptionsResource(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    @ApiOperation(
        value = "Get all standard options.",
        response = StandardOptionDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all standard options for a specific vehicleId.")
    })
    @GetMapping(
        path = "/standard/{vehicleId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStandardOptions(@PathVariable BigInteger vehicleId) {
        log.debug("Fetching standard options for vehicle: {}", vehicleId);

        return ResponseEntity.ok(optionsService.getStandardOptions(vehicleId));
    }

    @ApiOperation(
        value = "Get all extra options.",
        response = ExtraOptionDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all extra options for a specific vehicleId.")
    })
    @GetMapping(
        path = "/extra/{vehicleId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExtraOptions(@PathVariable BigInteger vehicleId) {
        log.debug("Fetching extra options for vehicle: {}", vehicleId);

        return ResponseEntity.ok(optionsService.getExtraOptions(vehicleId));
    }

    @ApiOperation(
        value = "Get included extra options for a specific extra option ID.",
        response = ExtraOptionDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all the included extra options for a specific extra option ID.")
    })
    @GetMapping(
        path = "/includes/{optionId}/{vehicleId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getIncludedExtraOptions(@PathVariable BigInteger optionId, @PathVariable BigInteger vehicleId) {
        log.debug("Fetching included extra options for vehicle: {}", vehicleId);

        return ResponseEntity.ok(optionsService.getIncludedExtraOptions(optionId, vehicleId));
    }

    @ApiOperation(
        value = "Get excluded extra options for a specific extra option ID.",
        response = ExtraOptionDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all the excluded extra options for a specific extra option ID.")
    })
    @GetMapping(
        path = "/excludes/{optionId}/{vehicleId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExcludedExtraOptions(@PathVariable BigInteger optionId, @PathVariable BigInteger vehicleId) {
        log.debug("Fetching excluded extra options for vehicle: {}", vehicleId);

        return ResponseEntity.ok(optionsService.getExcludedExtraOptions(optionId, vehicleId));
    }

    @ApiOperation(
        value = "Get required extra options for a specific extra option ID.",
        response = ExtraOptionDTO.class,
        responseContainer = "List"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Returns the list of all the required extra options for a specific extra option ID.")
    })
    @GetMapping(
        path = "/requires/{optionId}/{vehicleId}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRequiredExtraOptions(@PathVariable BigInteger optionId, @PathVariable BigInteger vehicleId) {
        log.debug("Fetching excluded extra options for vehicle: {}", vehicleId);

        return ResponseEntity.ok(optionsService.getRequiredExtraOptions(optionId, vehicleId));
    }
}
