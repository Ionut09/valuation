package co.arbelos.gtm.core.repository.marketobservation;

import co.arbelos.gtm.core.domain.marketobservation.MarketObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MarketObservationRepository extends JpaRepository<MarketObservation, Long> {

    MarketObservation findOneByVehicleIdAndMarketId(Long vehicleId, UUID marketId);

    @Modifying
    @Query(
        value = "truncate table market_observation",
        nativeQuery = true
    )
    void truncateTable();

}
