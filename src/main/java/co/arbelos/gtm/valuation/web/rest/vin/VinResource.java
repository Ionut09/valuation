package co.arbelos.gtm.valuation.web.rest.vin;

import co.arbelos.gtm.valuation.service.vin.VinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vin")
public class VinResource {

    private final Logger log = LoggerFactory.getLogger(VinResource.class);

    private final VinService vinService;

    public VinResource(VinService vinService) {
        this.vinService = vinService;
    }

    @PostMapping("/{value}")
    public ResponseEntity decodeVin(@PathVariable(value="value") String vin) {
        return ResponseEntity.ok(vinService.decodeVIN(vin));
    }


}
