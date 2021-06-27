package co.arbelos.gtm.core.repository.jato;

import co.arbelos.gtm.core.dao.jato.ExtraOptionRuleProjection;
import co.arbelos.gtm.core.domain.jato.OptionBuild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface OptionBuildRepository extends JpaRepository<OptionBuild, Long>  {

    @Query(value =
        "SELECT `condition`, option_rule as rule " +
            "FROM option_build " +
            "WHERE vehicle_id = :vehicleId " +
            "       AND rule_type = 100003 " +
            "       AND option_id = :optionId ", nativeQuery = true)
    ExtraOptionRuleProjection getInclusionRule(@Param("optionId") final BigInteger optionId, @Param("vehicleId") final BigInteger vehicleId);

    @Query(value =
        "SELECT `condition`, option_rule as rule " +
            "FROM option_build " +
            "WHERE vehicle_id = :vehicleId " +
            "       AND rule_type = 100007 " +
            "       AND option_id = :optionId ", nativeQuery = true)
    ExtraOptionRuleProjection getExclusionRule(@Param("optionId") final BigInteger optionId, @Param("vehicleId") final BigInteger vehicleId);

    @Query(value =
        "SELECT `condition`, option_rule as rule " +
            "FROM option_build " +
            "WHERE vehicle_id = :vehicleId " +
            "       AND rule_type = 100006 " +
            "       AND option_id = :optionId ", nativeQuery = true)
    ExtraOptionRuleProjection getRequirementRule(@Param("optionId") final BigInteger optionId, @Param("vehicleId") final BigInteger vehicleId);
}
