package co.arbelos.gtm.core.repository.jato;

import co.arbelos.gtm.core.dao.options.ExtraOptionProjection;
import co.arbelos.gtm.core.domain.jato.OptionList;
import co.arbelos.gtm.core.domain.jato.primarykeys.OptionListPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface OptionListRepository extends JpaRepository<OptionList, OptionListPK> {

    @Query(value =
        "select option_id as optionId, option_type as optionType, option_code as optionCode, manuf_name as description, id_903 as price " +
        "from   option_list " +
        "where  vehicle_id = :vehicleId", nativeQuery = true)
    List<ExtraOptionProjection> findExtraOptions(@Param("vehicleId") BigInteger vehicleId);

    @Query(value =
    "select distinct option_code as optionCode, option_id as optionId, option_type as optionType, manuf_name as description, id_903 as price " +
        "from   option_list " +
        "where  option_id in :optionIdList " +
        "  and option_code in :optionCodeList " +
        "  and option_type = 'O'", nativeQuery = true)
    List<ExtraOptionProjection> findExtraOption(@Param("optionIdList") List<Long> optionIds,
                                                @Param("optionCodeList") List<String> optionCodes);
}
