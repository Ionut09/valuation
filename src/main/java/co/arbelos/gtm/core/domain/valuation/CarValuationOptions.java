package co.arbelos.gtm.core.domain.valuation;

import co.arbelos.gtm.core.dto.web.options.ExtraOptionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "car_valuation_x_extra_options")
@Data
@NoArgsConstructor
public class CarValuationOptions {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "option_code")
    private String optionCode;

    @Column(name = "option_text")
    private String optionText;

    @Column(name = "option_price")
    private Float optionPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private CarValuation carValuation;

    public static CarValuationOptions from(ExtraOptionDTO extraOptionDTO, CarValuation carValuation) {
        CarValuationOptions cvo = new CarValuationOptions();

        cvo.setOptionId(extraOptionDTO.getOptionId());
        cvo.setOptionCode(extraOptionDTO.getOptionCode());
        cvo.setOptionText(extraOptionDTO.getDescription());
        cvo.setOptionPrice(extraOptionDTO.getPrice());
        cvo.setCarValuation(carValuation);

        return cvo;
    }
}
