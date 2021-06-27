package co.arbelos.gtm.core.repository.jato;

import co.arbelos.gtm.core.dao.options.StandardOptionProjection;
import co.arbelos.gtm.core.domain.jato.StandardText;
import co.arbelos.gtm.core.domain.jato.primarykeys.StandardTextPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface StandardTextRepository extends JpaRepository<StandardText, StandardTextPK> {

    @Query(value =
        "select schema_id as schemaId, language_id as languageId, content " +
        "from standard_text " +
        "where vehicle_id = :vehicleId", nativeQuery = true)
    List<StandardOptionProjection> findStandardOptions(@Param("vehicleId") BigInteger vehicleId);

}
