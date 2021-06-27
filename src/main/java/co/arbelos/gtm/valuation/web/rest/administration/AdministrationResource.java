package co.arbelos.gtm.valuation.web.rest.administration;

import co.arbelos.gtm.core.domain.configuration.CarSegmentDepreciationConfig;
import co.arbelos.gtm.core.dto.web.administration.CarSegmentsDepreciationDTO;
import co.arbelos.gtm.core.repository.configuration.CarSegmentDepreciationConfigRepository;
import co.arbelos.gtm.modules.dataingestor.tools.csv.CSVService;
import co.arbelos.gtm.valuation.config.AuditEventPublisher;
import co.arbelos.gtm.valuation.service.observationmarket.ObservationMarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/administration")
public class AdministrationResource {

    private final Logger log = LoggerFactory.getLogger(AdministrationResource.class);

    private final AuditEventPublisher auditPublisher;
    private final CSVService csvService;
    private final ObservationMarketService observationMarketService;

    private final CarSegmentDepreciationConfigRepository carSegmentDepreciationConfigRepository;

    public AdministrationResource(AuditEventPublisher auditPublisher,
                                  CSVService csvService,
                                  ObservationMarketService observationMarketService,
                                  CarSegmentDepreciationConfigRepository carSegmentDepreciationConfigRepository) {
        this.auditPublisher = auditPublisher;
        this.csvService = csvService;
        this.observationMarketService = observationMarketService;
        this.carSegmentDepreciationConfigRepository = carSegmentDepreciationConfigRepository;
    }

    @PostMapping("/ingest/autovit")
    public ResponseEntity uploadAutovitCSV(@RequestParam("csvFile") MultipartFile file,
                                           Principal principal) {
        log.debug("Ingesting autovit : {} ...", file.getOriginalFilename());

        auditPublisher.publish(new AuditEvent(principal.getName(), "API_ADMINISTRATION", "Info=Autovit CSV file ingestion!"));

        return ResponseEntity.ok(csvService.handleFile(file));
    }
    @GetMapping("/ingest/autovit/lock")
    public ResponseEntity getAutovitLock() {
        log.debug("Checking if autovit uploading is locked...");
        return ResponseEntity.ok(csvService.getLock());
    }

    @PostMapping("/ingest/tradein")
    public ResponseEntity uploadTradeInCSV(@RequestParam("csvFile2") MultipartFile file,
                                           Principal principal) {
        log.debug("Ingesting tradeIn: {} ...", file.getOriginalFilename());

        auditPublisher.publish(new AuditEvent(principal.getName(), "API_ADMINISTRATION", "Info=TradeIn CSV file ingestion!"));

        return ResponseEntity.ok(csvService.handleFileTradeIn(file));
    }
    @GetMapping("/ingest/tradein/lock")
    public ResponseEntity getTradeInLock() {
        log.debug("Checking if tradeIn uploading is locked...");
        return ResponseEntity.ok(csvService.getLock2());
    }

    @PostMapping("/ingest/autovit-excel")
    public ResponseEntity uploadAutovitExcel(@RequestParam("csvFile3") MultipartFile file,
                                           Principal principal) {
        log.debug("Ingesting autovit excel: {} ...", file.getOriginalFilename());

        auditPublisher.publish(new AuditEvent(principal.getName(), "API_ADMINISTRATION", "Info=Autovit EXCEL file ingestion!"));

        return ResponseEntity.ok(csvService.handleAutovitExcelFile(file));
    }
    @GetMapping("/ingest/autovit-excel/lock")
    public ResponseEntity getAutovitExcelLock() {
        log.debug("Checking if autovit excel uploading is locked...");
        return ResponseEntity.ok(csvService.getLock3());
    }

    @PostMapping("/observation-market")
    public ResponseEntity triggerObservationMarketCreation(Principal principal) {
        log.debug("Triggering observation market creation...");
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_ADMINISTRATION", "Info=Observation market creation trigger!"));

        observationMarketService.triggerObservationMarketCreation();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/observation-market/lock")
    public ResponseEntity getObservationMarketLock() {
        log.debug("Checking if observation market data computations are locked...");

        return ResponseEntity.ok(observationMarketService.getObservationMarketLock());
    }

    @GetMapping("/master-types")
    public ResponseEntity getMasterTypes(@RequestParam(name = "manufacturer") String manufacturer,
                                         @RequestParam(name = "model") String model) {
        log.debug("Trying to get master type for: {} - {}", manufacturer, model);

        return ResponseEntity.ok(observationMarketService.getMasterTypes(manufacturer,model));
    }
    @PostMapping("/master-types")
    public ResponseEntity triggerMasterTypeCreation(Principal principal) {
        log.debug("Triggering master type creation...");
        auditPublisher.publish(new AuditEvent(principal.getName(), "API_ADMINISTRATION", "Info=Master type creation trigger!"));

        observationMarketService.triggerMasterTypeCreations();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/master-types/lock")
    public ResponseEntity getMasterTypeLock() {
        log.debug("Checking if master type data computations are locked...");

        return ResponseEntity.ok(observationMarketService.getMasterTypeLock());
    }

    @GetMapping("/segment-depreciations")
    public ResponseEntity getSegmentsDepreciation() {
        log.debug("Fetching segments depreciations...");

        return ResponseEntity.ok(carSegmentDepreciationConfigRepository.findDepreciations());
    }
    @PostMapping("/segment-depreciations")
    public ResponseEntity updateSegmentsDepreciation(@RequestBody List<CarSegmentsDepreciationDTO> data) {
        log.debug("Updating segments depreciations...");

        data.forEach(el -> {
            CarSegmentDepreciationConfig carSegmentDepreciationConfig =
                carSegmentDepreciationConfigRepository.findById(el.getId()).get();

            carSegmentDepreciationConfig.setFirstYear(el.getFirstYear());
            carSegmentDepreciationConfig.setSecondYear(el.getSecondYear());
            carSegmentDepreciationConfig.setThirdYear(el.getThirdYear());
            carSegmentDepreciationConfig.setFourthYear(el.getFourthYear());
            carSegmentDepreciationConfig.setFifthYear(el.getFifthYear());

            carSegmentDepreciationConfigRepository.save(carSegmentDepreciationConfig);
        });

        return ResponseEntity.ok(carSegmentDepreciationConfigRepository.findDepreciations());
    }
}
