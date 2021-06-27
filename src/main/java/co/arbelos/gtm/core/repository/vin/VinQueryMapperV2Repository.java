package co.arbelos.gtm.core.repository.vin;

import co.arbelos.gtm.core.domain.vin.VinQueryMapperV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VinQueryMapperV2Repository extends JpaRepository<VinQueryMapperV2, String> {

    List<VinQueryMapperV2> findAllByUmc(String umc);

}
