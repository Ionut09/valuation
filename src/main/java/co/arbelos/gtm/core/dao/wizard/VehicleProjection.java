package co.arbelos.gtm.core.dao.wizard;

import java.math.BigInteger;

public interface VehicleProjection {
    String  getManufacturer();
    String  getModel();
    String  getBody();
    String  getVersion();
    String  getTrimLevel();
    String  getTransmission();
    String  getFuelType();
    Integer getNoOfDoors();
    Integer getPowerHP();
    Integer getModelYear();
    Long    getBasePrice();

    String  getStartSellingDate();
    String  getEndSellingDate();

    BigInteger getVehicleId();

    String getTransmissionDescription();
    String getDrivenWheels();

    String getEngineCode();

    Integer getKw();
}
