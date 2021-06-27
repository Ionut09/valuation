package co.arbelos.gtm.valuation.service.report;

import co.arbelos.gtm.core.dto.web.valuation.ValuationHistoryDTO;
import co.arbelos.gtm.valuation.service.valuation.ValuationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class ReportService {

    private final ValuationService valuationService;

    public ReportService(ValuationService valuationService) {
        this.valuationService = valuationService;
    }

    @Transactional
    public ValuationHistoryDTO getValuation(Long valuationId, Principal principal) {
        return valuationService.getValuation(valuationId, principal);
    }
}
