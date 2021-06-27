package co.arbelos.gtm.core.repository.jato.germandb;

import co.arbelos.gtm.core.dao.jato.MarkModelProjection;
import co.arbelos.gtm.core.dao.valuation.PredictionVersionProjection;
import co.arbelos.gtm.core.dao.wizard.VehicleProjection;
import co.arbelos.gtm.core.dao.wizard.germandb.BodyProjectionDE;
import co.arbelos.gtm.core.dao.wizard.germandb.GenerationProjectionDE;
import co.arbelos.gtm.core.dao.wizard.germandb.ModelProjectionDE;
import co.arbelos.gtm.core.domain.jato.Version;
import co.arbelos.gtm.core.domain.jato.germandb.VersionDE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionDERepository extends JpaRepository<VersionDE, Long> {

    VersionDE findOneByVehicleId(Long jatoVehicleId);

    /*
    * id_111 'make' - this is the name of the vehicle manufacturer responsible for national distribution within the
    * country of research. Vehicle names can be translated in the schema_text table.
    * */
    @Query(value = "select distinct id_111 from version_de", nativeQuery = true)
    List<String> findManufacturers();

    /*
    * id_112 'model' - the name under which the vehicle is officially marketed.
    * The model name should not be confused with the version name or description.
    * */
    @Query(value =
        "select distinct id_112 as model, id_111 as manufacturer, min(id_903) as minPrice, max(id_903) as maxPrice, id_901 as currency " +
        "from version_de where id_111 = :manufacturer group by model, currency order by model", nativeQuery = true)
    List<ModelProjectionDE> findModels(@Param("manufacturer") String manufacturer);

    @Query(value =
        "select distinct id_112 as model, id_111 as manufacturer, min(id_903) as minPrice, max(id_903) as maxPrice, id_901 as currency " +
            "from version_de where id_111 = :manufacturer and (id_104 <= :endDate AND id_113 >= :startDate) group by model, currency order by model", nativeQuery = true)
    List<ModelProjectionDE> findModels(@Param("manufacturer") String manufacturer,
                                       @Param("startDate")     String startDate,
                                       @Param("endDate")       String endDate);

    /*
     * Needed for VIN Query
     * */
    @Query(value = "select distinct id_111 from version_de where id_111 like CONCAT('%',:make,'%')", nativeQuery = true)
    String findManufacturer(@Param("make") String make);

    @Query(value = "select distinct id_112 from version_de where :model like CONCAT('%',id_112,'%') order by CHAR_LENGTH(id_112) DESC limit 1", nativeQuery = true)
    String findModel(@Param("model") String model);

    @Query(value =
        "select " +
            "distinct id_603 as body, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
        "from version_de where id_111 = :manufacturer AND id_112 = :model " +
        "group by id_603, id_602, id_901", nativeQuery = true)
    List<BodyProjectionDE> findBodies(@Param("manufacturer")  String manufacturer,
                                      @Param("model")         String model);

    @Query(value =
        "select " +
            "distinct id_603 as body, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
            "from version_de where id_111 = :manufacturer AND id_112 = :model AND (id_104 <= :endDate AND id_113 >= :startDate) " +
            "group by id_603, id_602, id_901", nativeQuery = true)
    List<BodyProjectionDE> findBodies(@Param("manufacturer")  String manufacturer,
                                    @Param("model")         String model,
                                    @Param("startDate")     String startDate,
                                    @Param("endDate")       String endDate);


    @Query(value =
        "select " +
            "distinct id_603 as body, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
            "from version_de where id_111 = :manufacturer AND id_112 = :model AND (id_104 <= :endDate AND id_113 >= :startDate) " +
            "AND id_8702    in :fuelTypes "         +
            "AND id_20602   in :transmissionTypes "  +
            "AND id_6502    in :drivenWheels "       +
            "group by id_603, id_602, id_901", nativeQuery = true)
    List<BodyProjectionDE> findBodies(@Param("manufacturer")      String manufacturer,
                                    @Param("model")             String model,
                                    @Param("startDate")         String startDate,
                                    @Param("endDate")           String endDate,
                                    @Param("fuelTypes")         List<String> fuelTypes,
                                    @Param("transmissionTypes") List<String> transmissionTypes,
                                    @Param("drivenWheels")      List<String> drivenWheels);
    @Query(value =
        "select " +
            "distinct id_307 as generation, " +
            "id_108 as modelYear, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_603 as body, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
        "from version_de where id_111 = :manufacturer AND id_112 = :model AND id_603 = :body AND id_602 = :doorsNo " +
        "group by id_307, id_108, id_603, id_602, id_901", nativeQuery = true)
    List<GenerationProjectionDE> findGenerations(
        @Param("manufacturer")  String manufacturer,
        @Param("model")         String model,
        @Param("body")          String body,
        @Param("doorsNo")       Integer doorsNo
        );

    @Query(value =
        "select " +
            "distinct id_307 as generation, " +
            "id_108 as modelYear, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_603 as body, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
            "from version_de where id_111 = :manufacturer AND id_112 = :model AND id_603 = :body AND id_602 = :doorsNo " +
            "and (id_104 <= :endDate AND id_113 >= :startDate) " +
            "group by id_307, id_108, id_603, id_602, id_901", nativeQuery = true)
    List<GenerationProjectionDE> findGenerations(
        @Param("manufacturer")  String manufacturer,
        @Param("model")         String model,
        @Param("body")          String body,
        @Param("doorsNo")       Integer doorsNo,
        @Param("startDate")     String startDate,
        @Param("endDate")       String endDate
    );

    @Query(value =
        "select " +
            "distinct id_307 as generation, " +
            "id_108 as modelYear, " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_603 as body, " +
            "id_602 as doorsNo, " +
            "min(id_903) as minPrice, " +
            "max(id_903) as maxPrice, " +
            "id_901 as currency " +
            "from version_de where id_111 = :manufacturer AND id_112 = :model AND id_603 = :body AND id_602 = :doorsNo " +
            "and (id_104 <= :endDate AND id_113 >= :startDate) " +
            "and id_8702    in :fuelTypes "         +
            "and id_20602   in :transmissionTypes "  +
            "and id_6502    in :drivenWheels "       +
            "group by id_307, id_108, id_603, id_602, id_901", nativeQuery = true)
    List<GenerationProjectionDE> findGenerations(
        @Param("manufacturer")  String manufacturer,
        @Param("model")         String model,
        @Param("body")          String body,
        @Param("doorsNo")       Integer doorsNo,
        @Param("startDate")     String startDate,
        @Param("endDate")       String endDate,
        @Param("fuelTypes")         List<String> fuelTypes,
        @Param("transmissionTypes") List<String> transmissionTypes,
        @Param("drivenWheels")      List<String> drivenWheels
    );


    @Query(value =
        "select " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_603 as body, " +
            "id_131 as version, " +
            "id_402 as trimLevel, " +
            "id_20602 as transmission, " +
            "id_8702 as fuelType, " +
            "id_602 as noOfDoors, " +
            "id_15304 as powerHP, " +
            "id_108 as modelYear, " +
            "id_903 as basePrice, " +
            "id_104 as startSellingDate, " +
            "id_113 as endSellingDate, " +
            "vehicle_id as vehicleId " +
            "from version_de " +
            "where vehicle_id = :vehicleId order by powerHP asc", nativeQuery = true)
    VehicleProjection findVehicle(@Param("vehicleId") Long vehicleId);

    @Query(value =
        "select " +
            "id_111 as manufacturer, " +
            "id_112 as model, " +
            "id_131 as version, " +
            "id_402 as trimLevel, " +
            "id_20602 as transmission, " +
            "id_8702 as fuelType, " +
            "id_602 as noOfDoors, " +
            "id_15304 as powerHP, " +
            "id_108 as modelYear, " +
            "id_903 as basePrice, " +
            "id_104 as startSellingDate, " +
            "id_113 as endSellingDate, " +
            "vehicle_id as vehicleId " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model AND id_603 = :body AND id_602 = :doorsNo AND id_307 = :generation " +
            "and (id_104 <= :endDate AND id_113 >= :startDate) " +
            "and id_8702    in :fuelTypes "         +
            "and id_20602   in :transmissionTypes "  +
            "and id_6502    in :drivenWheels " +
            "order by powerHP asc", nativeQuery = true)
    List<VehicleProjection> findVehicles(
        @Param("manufacturer")  String manufacturer,
        @Param("model")         String model,
        @Param("body")          String body,
        @Param("doorsNo")       Integer doorsNo,
        @Param("generation")    Integer generation,
        @Param("startDate")     String startDate,
        @Param("endDate")       String endDate,
        @Param("fuelTypes")         List<String> fuelTypes,
        @Param("transmissionTypes") List<String> transmissionTypes,
        @Param("drivenWheels")      List<String> drivenWheels
    );
    @Query(value =
        "select " +
            "            v.vehicle_id as vehicleId, " +
            "            v.id_111 as manufacturer, " +
            "            v.id_112 as model, " +
            "            v.id_131 as version, " +
            "            v.id_402 as trimLevel, " +
            "            v.id_20602 as transmissionDescription, " +
            "            v.id_8702 as fuelType, " +
            "            v.id_602 as noOfDoors, " +
            "            v.id_15304 as powerHP, " +
            "            v.id_108 as modelYear, " +
            "            v.id_6502 as drivenWheels, " +
            "            v.id_903 as basePrice, " +
            "            v.id_104 as startSellingDate, " +
            "            v.id_113 as endSellingDate " +
            "from version_de v " +
            "where id_111 = :manufacturer " +
            "AND id_112 = :model " +
            "AND id_603 = :body " +
            "AND id_602 = :doorsNo " +
            "AND id_307 = :generation " +
            "and (id_104 <= :endDate AND id_113 >= :startDate) " +
            "order by powerHP asc", nativeQuery = true)
    List<VehicleProjection> findVehicles(
        @Param("manufacturer")  String manufacturer,
        @Param("model")         String model,
        @Param("body")          String body,
        @Param("doorsNo")       Integer doorsNo,
        @Param("generation")    Integer generation,
        @Param("startDate")     String startDate,
        @Param("endDate")       String endDate
    );

    @Query(value =
        "select distinct id_8702 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model", nativeQuery = true)
    List<String> findFuelTypes(@Param("manufacturer")  String manufacturer,
                               @Param("model")         String model);

    @Query(value =
        "select distinct id_8702 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model AND (id_104 <= :endDate AND id_113 >= :startDate)", nativeQuery = true)
    List<String> findFuelTypes(@Param("manufacturer")  String manufacturer,
                               @Param("model")         String model,
                               @Param("startDate")     String startDate,
                               @Param("endDate")       String endDate);

    @Query(value =
        "select distinct id_20602 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model AND upper(id_8702) = :fuelType", nativeQuery = true)
    List<String> findTransmissions(@Param("manufacturer")  String manufacturer,
                                   @Param("model")         String model,
                                   @Param("fuelType")      String fuelType);

    @Query(value =
        "select distinct id_20602 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model AND upper(id_8702) = :fuelType AND (id_104 <= :endDate AND id_113 >= :startDate)", nativeQuery = true)
    List<String> findTransmissions(@Param("manufacturer")  String manufacturer,
                                   @Param("model")         String model,
                                   @Param("fuelType")      String fuelType,
                                   @Param("startDate")     String startDate,
                                   @Param("endDate")       String endDate);

    @Query(value =
        "select distinct id_6502 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model and upper(id_8702) = :fuelType and upper(id_20602) = :transmissionType", nativeQuery = true)
    List<String> findDrivenWheels(@Param("manufacturer")     String manufacturer,
                                  @Param("model")            String model,
                                  @Param("fuelType")         String fuelType,
                                  @Param("transmissionType") String transmissionType);

    @Query(value =
        "select distinct id_6502 " +
            "from version_de " +
            "where id_111 = :manufacturer AND id_112 = :model and upper(id_8702) = :fuelType and upper(id_20602) = :transmissionType AND (id_104 <= :endDate AND id_113 >= :startDate)", nativeQuery = true)
    List<String> findDrivenWheels(@Param("manufacturer")     String manufacturer,
                                  @Param("model")            String model,
                                  @Param("fuelType")         String fuelType,
                                  @Param("transmissionType") String transmissionType,
                                  @Param("startDate")       String startDate,
                                  @Param("endDate")         String endDate);
    @Query(value =
        "select distinct id_6502 as drivenWheels " +
        "from version_de " +
        "where id_111 = :manufacturer and id_112 = :model and id_104 like CONCAT('',:refYear,'%')", nativeQuery = true)
    List<String> findDrivenWheels(@Param("manufacturer")     String manufacturer,
                                  @Param("model")            String model,
                                  @Param("refYear")          String year);

    @Query(value =
        "select distinct CONCAT(id_602, id_603) " +
        "from version_de " +
        "where id_111 = :manufacturer and id_112 = :model and id_104 like CONCAT('',:refYear,'%')", nativeQuery = true)
    List<String> findDoorsNumberAndBody(@Param("manufacturer")     String manufacturer,
                                        @Param("model")            String model,
                                        @Param("refYear")          String year);

    @Query(value = "select distinct id_111 as manufacturer, id_112 as model " +
        "from version_de " +
        "order by manufacturer asc", nativeQuery = true)
    List<MarkModelProjection> findDistinctMarksModels();


    @Query(value =  "select distinct id_111 as manufacturer " +
                    "from version_de " +
                    "order by manufacturer asc", nativeQuery = true)
    List<String> findDistinctManufacturers();

    @Query(value =  "select distinct id_112 as model " +
                    "from version_de " +
                    "where id_111 = :manufacturer " +
                    "order by model asc;", nativeQuery = true)
    List<String> findDistinctModels(@Param("manufacturer") String manufacturer);


    @Query(value =
    "select * from version_de " +
        "where id_174 = :segmentCode and id_903 between :refPrice - 5000 and :refPrice + 5000", nativeQuery = true)
    List<VersionDE> findAllBySegmentCode(@Param("segmentCode") Integer segmentCode, @Param("refPrice") Float refPrice);

    @Query(value =
        "select id_111 as make, id_112 as model, id_15304 as hp, id_108 as year, id_20602 as transmissionType, id_8702 as fuelType, id_903 as price from version_de " +
            "where id_111 = :manufacturer and id_112 = :model and id_15304 between :powerHp - 30 and :powerHp + 30 and id_108 = :refYear and id_8702 = :fuelType " +
            "limit 1", nativeQuery = true)
    PredictionVersionProjection findOneForPredict(@Param("manufacturer") String manufacturer,
                                                  @Param("model") String model,
                                                  @Param("powerHp") Integer powerHp,
                                                  @Param("refYear") Integer year,
                                                  @Param("fuelType") String fuelType);
}

