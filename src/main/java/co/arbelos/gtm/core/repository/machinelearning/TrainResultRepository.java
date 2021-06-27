package co.arbelos.gtm.core.repository.machinelearning;

import co.arbelos.gtm.core.domain.machinelearning.TrainResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainResultRepository extends JpaRepository<TrainResult, Long> {

    @Modifying
    @Query(
        value = "truncate table train_result",
        nativeQuery = true
    )
    void truncateTable();

    @Query(value =  "select * from train_result " +
                    "where manufacturer = :manufacturer and model = :model " +
                    "order by training_date desc " +
                    "limit 1", nativeQuery = true)
    TrainResult findLastOne(@Param("manufacturer") String manufacturer,
                            @Param("model") String model);

    TrainResult findByManufacturerAndModel(String manufacturer, String model);

    @Query(value =
    "select * from train_result " +
        "where manufacturer in :manufacturers " +
        "and model in :models", nativeQuery = true)
    List<TrainResult> findInListOfMakeAndModel(@Param("manufacturers") List<String> manufacturers,
                                               @Param("models") List<String> models);
}
