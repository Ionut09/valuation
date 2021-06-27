package co.arbelos.gtm.core.repository.jato;

import co.arbelos.gtm.core.domain.jato.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>  {

    @Query(value = "select data_value from equipment where schema_id = '7402' and vehicle_id  = :vehicleId limit 1", nativeQuery = true)
    Integer getCC(@Param("vehicleId") String vehicleId);

}
