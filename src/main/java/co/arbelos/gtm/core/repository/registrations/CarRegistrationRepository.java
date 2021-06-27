package co.arbelos.gtm.core.repository.registrations;

import co.arbelos.gtm.core.domain.registrations.CarRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRegistrationRepository extends JpaRepository<CarRegistration, Long> {

    @Query(value = "select * from car_registrations s " +
        "join " +
        "    ( " +
        "        select max(registration_amount) as amount " +
        "        from car_registrations " +
        "        where manufacturer = :manufacturer and model = :model " +
        "        group by registration_date " +
        "    ) as st " +
        "on s.registration_amount = st.amount " +
        "where manufacturer = :manufacturer and model = :model " +
        "order by registration_date desc", nativeQuery = true)
    List<CarRegistration> findMasterTypes(@Param("manufacturer") String manufacturer, @Param("model") String model);
}
