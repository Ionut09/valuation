package co.arbelos.gtm.core.dto.web.valuation;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimateCarDTO {
    private String manufacturer;
    private String model;
    private Integer engine_hp;
    private Integer manufacturer_year;
    private Integer km_no;
    private String transmission_type;
    private String fuel_type;
}
