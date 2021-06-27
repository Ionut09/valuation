package co.arbelos.gtm.core.repository.jato;

import co.arbelos.gtm.core.dao.jato.CarSegmentProjection;
import co.arbelos.gtm.core.dao.wizard.TransmissionDescriptionProjection;
import co.arbelos.gtm.core.domain.jato.SchemaText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemaTextRepository extends JpaRepository<SchemaText, Long> {

    @Query(
        value =
            "select full_text as text, language_id as languageId " +
                "from schema_text " +
                "where schema_id = '20624' " +
                "and data_value = " +
                "    (select distinct data_value " +
                "    from equipment " +
                "    where vehicle_id = :vehicleId " +
                "      and schema_id = '20624' " +
                "    limit 1)",
        nativeQuery = true
    )
    List<TransmissionDescriptionProjection> findTransmissionDescription(@Param("vehicleId") Long vehicleId);

    @Query(value =
        "select distinct t.full_text as description, t.schema_id as schemaId, t.data_value as id " +
            "from version v, schema_text t " +
            "where t.schema_id = 174 " +
            "  and t.data_value = v.id_174 " +
            "  and t.language_id = 19 " +
            "order by t.full_text asc", nativeQuery = true)
    List<CarSegmentProjection> findCarSegments();
}
