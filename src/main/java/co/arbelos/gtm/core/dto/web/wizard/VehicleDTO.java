package co.arbelos.gtm.core.dto.web.wizard;

import co.arbelos.gtm.core.dao.wizard.VehicleProjection;
import co.arbelos.gtm.core.util.JatoUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private String manufacturer;
    private String model;
    private String body;
    private String version;
    private String trimLevel;
    private String transmission;
    private String fuelType;
    private Integer noOfDoors;
    private Integer powerHP;
    private Integer modelYear;
    private Long basePrice;

    private String startSellingDate;
    private String endSellingDate;

    private BigInteger vehicleId;

    private String transmissionDescription;
    private String drivenWheels;

    private String engineCode;

    private Integer kw;
    private Integer cc;

    public VehicleDTO(VehicleProjection vehicleProjection) {
        this.manufacturer       = vehicleProjection.getManufacturer();
        this.model              = vehicleProjection.getModel();
        this.body               = vehicleProjection.getBody();
        this.version            = vehicleProjection.getVersion();
        this.trimLevel          = vehicleProjection.getTrimLevel();
        this.transmission       = JatoUtil.getTransmission(vehicleProjection.getTransmission());
        this.fuelType           = JatoUtil.getFuelType(vehicleProjection.getFuelType());
        this.noOfDoors          = vehicleProjection.getNoOfDoors();
        this.powerHP            = vehicleProjection.getPowerHP();
        this.modelYear          = vehicleProjection.getModelYear();
        this.basePrice          = vehicleProjection.getBasePrice();

        this.startSellingDate   = vehicleProjection.getStartSellingDate();
        this.endSellingDate = vehicleProjection.getEndSellingDate();

        this.vehicleId          = vehicleProjection.getVehicleId();

        this.transmissionDescription = vehicleProjection.getTransmissionDescription();
        this.drivenWheels = JatoUtil.getDrivenWheels(vehicleProjection.getDrivenWheels());

        this.engineCode = vehicleProjection.getEngineCode();

        this.kw = vehicleProjection.getKw();
    }

    public Date getConvertedEndSellingDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(this.endSellingDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Date getConvertedStartSellingDate() {
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(this.startSellingDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
