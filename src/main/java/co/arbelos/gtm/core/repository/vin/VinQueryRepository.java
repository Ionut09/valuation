package co.arbelos.gtm.core.repository.vin;

import co.arbelos.gtm.core.domain.vin.VinQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VinQueryRepository extends JpaRepository<VinQuery, Long> {

    VinQuery findOneByVin(String vin);
}
