package co.arbelos.gtm.core.repository.marketobservation;

import co.arbelos.gtm.core.dao.valuation.TimePointProjection;
import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, UUID> {

    List<MarketData> findAllByManufacturerAndModelAndTransmissionTypeAndEngineHpAndFuelTypeAndManufacturerYear(
        String manufacturer,
        String model,
        String transmissionType,
        Integer engineHp,
        String fuelType,
        Integer manufacturerYear
    );

    @Query(value =
    "SELECT collection_date as date, avg(t.price) as value FROM " +
        "  market_data AS t " +
        "  CROSS JOIN ( " +
        "    SELECT AVG(price) avgr, STD(price) stdr " +
        "    FROM market_data " +
        "    WHERE vehicle_manufacturer = :manufacturer and vehicle_model = :model and engine_hp = :engineHp and fuel_type = :fuelType and manufacturer_year in (:refYear - 1, :refYear) and km_no < 50000 AND collection_date > DATE_SUB(now(), INTERVAL 12 MONTH) " +
        "  ) AS stats " +
        "WHERE t.vehicle_manufacturer = :manufacturer and t.vehicle_model = :model and t.engine_hp = :engineHp and t.fuel_type = :fuelType and t.manufacturer_year in (:refYear - 1, :refYear) and km_no < 50000 AND collection_date > DATE_SUB(now(), INTERVAL 12 MONTH) " +
        "  AND t.price BETWEEN (stats.avgr - (0.8 *stats.stdr)) AND (stats.avgr +  stats.stdr) " +
        "group by collection_date " +
        "order by extract(year from collection_date) desc, extract(month from collection_date) desc", nativeQuery = true)
    List<TimePointProjection> findMasterTypeData(@Param("manufacturer") String manufacturer,
                                                 @Param("model") String model,
                                                 @Param("engineHp") Integer engineHp,
                                                 @Param("fuelType") String fuelType,
                                                 @Param("refYear") Integer refYear);
}
