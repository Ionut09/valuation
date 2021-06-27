package co.arbelos.gtm.core.domain.registrations;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "car_registrations")
@Data
@NoArgsConstructor
@ToString
public class CarRegistration {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "engine_cc")
    private Integer engineCC;

    @Column(name = "engine_litres")
    private Float engineLitres;

    @Column(name = "fuel_type")
    private String fuel;

    @Column(name = "body")
    private String body;

    @Column(name = "engine_hp")
    private Integer engineHP;

    @Column(name = "registration_date")
    private LocalDate date;

    // number of cars registered during the date above
    @Column(name = "registration_amount")
    private Integer amount;

    public static CarRegistration from(CarRegistration carRegistration, LocalDate date, Integer amount) {
        CarRegistration newObject = new CarRegistration();

        newObject.manufacturer = carRegistration.getManufacturer();
        newObject.model = carRegistration.getModel();
        newObject.engineCC = carRegistration.getEngineCC();
        newObject.engineLitres = carRegistration.getEngineLitres();
        newObject.fuel = carRegistration.getFuel();
        newObject.body = carRegistration.getBody();
        newObject.engineHP = carRegistration.getEngineHP();
        newObject.date = date;
        newObject.amount = amount;

        return newObject;
    }
}
