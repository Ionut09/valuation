package co.arbelos.gtm.core.repository.marketobservation;

import co.arbelos.gtm.core.domain.marketobservation.MarketMasterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketMasterTypeRepository extends JpaRepository<MarketMasterType, UUID> {

    @Modifying
    @Query(
        value = "truncate table market_master_type",
        nativeQuery = true
    )
    void truncateTable();

    MarketMasterType findByManufacturerAndModelAndYear(String manufacturer, String model, Integer year);

    List<MarketMasterType> findAllByManufacturerAndModelOrderByYearDesc(String manufacturer, String model);
}
