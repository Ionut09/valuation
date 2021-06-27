package co.arbelos.gtm.core.dto.web.valuation;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutovitParamsDTO {
    private String manufacturer;
    private String model;
    private Integer modelYear;

    private Integer numberOfKilometers;

    private String transmission;
}
