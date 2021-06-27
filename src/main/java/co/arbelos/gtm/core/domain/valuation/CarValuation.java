package co.arbelos.gtm.core.domain.valuation;


import co.arbelos.gtm.core.dto.web.valuation.CreateValuationDTO;
import co.arbelos.gtm.valuation.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "car_valuation", indexes = { @Index(name = "car_valuation_index", columnList = "id,jato_vehicle_id,user_id") })
@Data
@NoArgsConstructor
public class CarValuation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jato_vehicle_id")
    private Long vehicleId;

    @Column(name = "kilometers_number")
    private Integer numberOfKilometers;

    @Column(name = "attrition_value")
    private Float attritionValue;

    @Column(name = "body_damage_value")
    private Float bodyDamageValue;

    @Column(name = "gpl")
    private boolean gpl;

    @Column(name = "fabrication_date")
    private Date fabricationDate;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "valuation_date")
    private Instant valuationDate = Instant.now();

    @Column(name = "price_new_car")
    private Float priceAsNew;

    @Column(name = "price_extra_options")
    private Float extraOptionsPrice;

    @Column(name = "valuation_price")
    private Float valuationPrice;

    @Column(name = "price_depreciation_extra_options")
    private Float extraOptionsDepreciationPrice;

    @Column(name = "vin")
    private String vin;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @JsonIgnore
    @OneToMany(
        mappedBy = "carValuation",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private List<CarValuationOptions> options = new ArrayList<>();

    public static CarValuation from(CreateValuationDTO dto, User user) {
        CarValuation carValuation = new CarValuation();

        carValuation.setVehicleId(dto.getVehicleId());
        carValuation.setNumberOfKilometers(dto.getNumberOfKilometers());
        carValuation.setAttritionValue(dto.getAttritionValue());
        carValuation.setBodyDamageValue(dto.getBodyDamageValue());
        carValuation.setGpl(dto.getGpl());
        carValuation.setFabricationDate(dto.getFabricationDate());
        carValuation.setRegistrationDate(dto.getRegistrationDate());
        carValuation.setPriceAsNew(dto.getPriceAsNew());
        carValuation.setExtraOptionsPrice(dto.getExtraOptionsPrice());

        carValuation.setUser(user);

        return carValuation;
    }
}
