package co.arbelos.gtm.core.repository.vin;

import co.arbelos.gtm.core.domain.vin.VinQueryMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VinQueryMapperRepository extends JpaRepository<VinQueryMapper, String> {

    VinQueryMapper findByGtwsMakeAndGtwsModel(String gtwsMake, String gtwsModel);

}
