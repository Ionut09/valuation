package co.arbelos.gtm.valuation.web.rest.jato;

import co.arbelos.gtm.valuation.service.jato.SchemaTextService;
import co.arbelos.gtm.valuation.service.jato.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class JatoResource {

    private final Logger log = LoggerFactory.getLogger(JatoResource.class);

    private final VersionService versionService;
    private final SchemaTextService schemaTextService;

    public JatoResource(VersionService versionService,
                        SchemaTextService schemaTextService) {
        this.versionService = versionService;
        this.schemaTextService = schemaTextService;
    }

    @GetMapping("/manufacturers")
    public ResponseEntity getManufacturers() {
        log.info("Fetching all car manufacturers...");

        return ResponseEntity.ok(versionService.getManufacturers());
    }

    @GetMapping("/models")
    public ResponseEntity getFuelTypes(@RequestParam(name = "manufacturer")   String manufacturer) {
        log.info("Fetching all car models for manufacturer: {} ...", manufacturer);

        return ResponseEntity.ok(versionService.getModels(manufacturer));
    }

    @GetMapping("/car-segments")
    public ResponseEntity getCarSegments() {
        log.info("Fetching all car segments...");

        return ResponseEntity.ok(schemaTextService.getCarSegments());
    }
}
