package co.arbelos.gtm.core.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "app_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppConfiguration {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matching_dispersion")
    private Float matchingDispersion;

    @Column(name = "mileage_depreciation_percentage")
    private Float mileageDepreciationPercentage;


}
