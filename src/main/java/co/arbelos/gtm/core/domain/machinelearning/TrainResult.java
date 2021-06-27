package co.arbelos.gtm.core.domain.machinelearning;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "train_result")
@Data
@NoArgsConstructor
@ToString
public class TrainResult {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "model")
    private String model;

    @Column(name = "rmse")
    private Float rmse;

    @Column(name = "variance_score")
    private Float varianceScore;

    @Column(name = "training_date")
    private LocalDate trainingDate;

    @Lob
    @Column(name = "plot_price_manufacturer_year", length=250000)
    private byte[] plotPriceManufacturerYear;

    @Lob
    @Column(name = "plot_price_kilometers", length=250000)
    private byte[] plotPriceKilometers;

    @Lob
    @Column(name = "plot_fueltype_price", length=250000)
    private byte[] plotFuelTypePrice;

    @Lob
    @Column(name = "plot_fueltype_price_year", length=250000)
    private byte[] plotFuelTypePriceYear;

    @Lob
    @Column(name = "plot_correlation_matrix", length=250000)
    private byte[] plotCorrelationMatrix;

    @Lob
    @Column(name = "plot_distribution", length=250000)
    private byte[] plotDistribuition;

    @Lob
    @Column(name = "plot_comparison", length=250000)
    private byte[] plotComparison;
}
