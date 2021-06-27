package co.arbelos.gtm.core.repository.valuation;

import co.arbelos.gtm.core.domain.valuation.CarValuation;
import co.arbelos.gtm.valuation.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarValuationRepository extends JpaRepository<CarValuation, Long> {

    String ALL_BY_USER_CACHE = "allByUser";

    List<CarValuation> findAllByUser(User user);

    CarValuation findOneById(Long id);
}
