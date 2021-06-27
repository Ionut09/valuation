package co.arbelos.gtm.core.repository.valuation;

import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.core.domain.valuation.CarValuationOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarValuationOptionsRepository extends JpaRepository<CarValuationOptions, Long> {

    List<CarValuationOptions> findAllByCarValuation(CarValuation carValuation);
}
