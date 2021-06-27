package co.arbelos.gtm.valuation.web.rest.administration;

import co.arbelos.gtm.valuation.service.machinelearning.MachineLearningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machine-learning")
public class MachineLearningResource {

    private final Logger log = LoggerFactory.getLogger(MachineLearningResource.class);

    private final MachineLearningService machineLearningService;

    public MachineLearningResource(MachineLearningService machineLearningService) {
        this.machineLearningService = machineLearningService;
    }

    @PostMapping("/training")
    public ResponseEntity triggerTraining() {
        log.info("Trying to trigger training process....");

        return ResponseEntity.ok(machineLearningService.triggerTraining());
    }

    @GetMapping("/training-results")
    public ResponseEntity getTrainingResults(@RequestParam(name = "manufacturer") String manufacturer,
                                             @RequestParam(name = "model") String model) {
        log.info("Trying to find train results for: {} - {}", manufacturer, model);

        return ResponseEntity.ok(machineLearningService.getTrainingResult(manufacturer, model));
    }

    @GetMapping("/lock")
    public ResponseEntity getLock() {
        log.info("Trying to get locking status for machine learning...");

        return ResponseEntity.ok(machineLearningService.getLocking());
    }
}
