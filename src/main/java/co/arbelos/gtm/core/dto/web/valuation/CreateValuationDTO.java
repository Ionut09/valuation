package co.arbelos.gtm.core.dto.web.valuation;

import co.arbelos.gtm.core.dto.web.options.ExtraOptionDTO;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateValuationDTO {

    private Long vehicleId;
    private Set<ExtraOptionDTO> options;
    private Integer numberOfKilometers;
    private Float attritionValue;
    private Float bodyDamageValue;
    private Boolean gpl;
    private String vin;

    private Date fabricationDate;
    private Date registrationDate;

    private Float priceAsNew;
    private Float extraOptionsPrice;
}
