package co.arbelos.gtm.core.dto.web.options;

import co.arbelos.gtm.core.dao.options.ExtraOptionProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExtraOptionDTO {
    private Long optionId;
    private String optionType;
    private String optionCode;
    private String description;
    private Float price;

    public ExtraOptionDTO(ExtraOptionProjection extraOptionProjection) {
        this.optionId = extraOptionProjection.getOptionId();
        this.optionType = extraOptionProjection.getOptionType();
        this.optionCode = extraOptionProjection.getOptionCode();

        this.description = extraOptionProjection.getDescription();
        this.price = extraOptionProjection.getPrice();
    }
}
