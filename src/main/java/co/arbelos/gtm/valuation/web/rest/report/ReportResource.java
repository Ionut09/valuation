package co.arbelos.gtm.valuation.web.rest.report;

import co.arbelos.gtm.valuation.service.report.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/report")
public class ReportResource {

    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{valuationId}")
    public ResponseEntity getReport(@PathVariable Long valuationId, Principal principal) {

        return ResponseEntity.ok(reportService.getValuation(valuationId, principal));
    }
}
