package co.arbelos.gtm.core.domain.marketobservation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "market_master_type")
@Data
@NoArgsConstructor
@ToString
public class MarketMasterType {

    @Id
    @GeneratedValue
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "body")
    private String body;

    @Column(name = "engine_hp")
    private Integer engineHp;

    @Column(name = "engine_litres")
    private Float engineLitres;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "registered_vehicles")
    private Integer registeredVehicles;

    @Column(name = "year")
    private Integer year;

    @PrePersist
    private void prePersist() {
        id = UUID.randomUUID();
    }
}
