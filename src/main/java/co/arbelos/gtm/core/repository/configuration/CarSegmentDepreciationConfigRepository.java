package co.arbelos.gtm.core.repository.configuration;

import co.arbelos.gtm.core.dao.administration.CarSegmentsDepreciationProjection;
import co.arbelos.gtm.core.domain.configuration.CarSegmentDepreciationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSegmentDepreciationConfigRepository extends JpaRepository<CarSegmentDepreciationConfig, Long> {

    @Query(value =
        "select distinct " +
            "       a.id, " +
            "       a.data_value dataValue, " +
            "       a.first_year firstYear, " +
            "       a.second_year secondYear, " +
            "       a.third_year thirdYear, " +
            "       a.fourth_year fourthYear, " +
            "       a.fifth_year fifthYear, " +
            "       t.full_text text " +
            "from car_segment_depreciation_config a, schema_text t " +
            "where a.data_value = t.data_value " +
            "and t.schema_id = 174 " +
            "and t.language_id = 19 " +
            "order by a.data_value asc;", nativeQuery = true)
    List<CarSegmentsDepreciationProjection> findDepreciations();

    CarSegmentDepreciationConfig findByDataValue(Integer dataValue);
}
