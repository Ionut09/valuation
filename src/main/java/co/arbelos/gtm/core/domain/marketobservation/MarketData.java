package co.arbelos.gtm.core.domain.marketobservation;

import co.arbelos.gtm.core.domain.jato.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "market_data")
@Data
@NoArgsConstructor
@ToString
public class MarketData implements Serializable {

    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "vehicle_manufacturer")
    private String manufacturer;

    @Column(name = "vehicle_model")
    private String model;

    @Column(name = "transmission_type")
    private String transmissionType;

    @Column(name = "engine_hp")
    private Integer engineHp;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "manufacturer_year")
    private Integer manufacturerYear;

    @Column(name = "km_no")
    private Integer kmNo;

    @Column(name = "price")
    private Float price;

    @Column(name = "vin")
    private String vin;

    @Column(name = "source")
    private String source;

    @Column(name = "collection_date")
    private LocalDate collectionDate;

    @Column(name = "ingest_file_id")
    private Long ingestFileId;

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID();
    }

    public boolean equalsWith(Version version) {
        if (version.getVehicleManufacturerName() != null &&
            version.getModelName() != null &&
            version.getTransmissionType() != null &&
            version.getMaximumPowerHP() != null &&
            version.getFuelType() != null &&
            version.getModelYear() != null &&
            this.manufacturer != null &&
            this.model != null &&
            this.transmissionType != null &&
            this.engineHp != null &&
            this.fuelType != null &&
            this.manufacturerYear != null) {
            return this.manufacturer.equals(version.getVehicleManufacturerName()) &&
                this.model.equals(version.getModelName()) &&
                this.transmissionType.equals(version.getTransmissionType()) &&
                this.engineHp.equals(version.getMaximumPowerHP()) &&
                this.fuelType.equals(version.getFuelType()) &&
                this.manufacturerYear.equals(Integer.valueOf(version.getModelYear()));
        } else {
            return false;
        }
    }
}
