package co.arbelos.gtm.core.dto.web.administration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarSegmentsDepreciationDTO {
    private Long id;
    private Integer dataValue;
    private Float firstYear;
    private Float secondYear;
    private Float thirdYear;
    private Float fourthYear;
    private Float fifthYear;
    private String text;
}
