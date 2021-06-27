package co.arbelos.gtm.core.repository.marketobservation;

import co.arbelos.gtm.core.domain.marketobservation.MarketDataIngestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketDataIngestHistoryRepository extends JpaRepository<MarketDataIngestHistory, Long> {
}
