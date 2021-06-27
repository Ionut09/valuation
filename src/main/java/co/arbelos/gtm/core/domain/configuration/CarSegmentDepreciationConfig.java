package co.arbelos.gtm.core.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "car_segment_depreciation_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarSegmentDepreciationConfig {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_value")
    private Integer dataValue;

    @Column(name = "first_year")
    private Float firstYear;

    @Column(name = "second_year")
    private Float secondYear;

    @Column(name = "third_year")
    private Float thirdYear;

    @Column(name = "fourth_year")
    private Float fourthYear;

    @Column(name = "fifth_year")
    private Float fifthYear;
}
