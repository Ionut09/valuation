package co.arbelos.gtm.core.dto.web.vin;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecodedVinDTO {

    private String manufacturer;
    private String model;
    private String body;
    private Number doorsNo;
    private LocalDate date;
    private String fuelType;
    private Integer horsePower;
    private Integer engineKw;

    private String ampatament;
    private String engineManufactureCode;

    private String gearbox;

    private Number cc;

    private List<String> extraOptionCodes;
}
