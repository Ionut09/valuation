package co.arbelos.gtm.core.dto.web.wizard;

import co.arbelos.gtm.core.dao.wizard.BodyProjection;
import co.arbelos.gtm.core.dao.wizard.germandb.BodyProjectionDE;
import co.arbelos.gtm.core.util.JatoUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BodyDTO {
    private String body;
    private String manufacturer;
    private String model;
    private Integer doorsNo;

    private Long minPrice;
    private Long maxPrice;
    private String currency;

    private String imageUrl;

    public BodyDTO(BodyProjection bodyProjection) {
        this.body           = JatoUtil.getBodyType(bodyProjection.getBody());
        this.manufacturer   = bodyProjection.getManufacturer();
        this.model          = bodyProjection.getModel();
        this.doorsNo        = bodyProjection.getDoorsNo();

        this.minPrice       = bodyProjection.getMinPrice();
        this.maxPrice       = bodyProjection.getMaxPrice();
        this.currency       = bodyProjection.getCurrency();
    }

    public BodyDTO(BodyProjectionDE bodyProjection) {
        this.body           = JatoUtil.getBodyType(bodyProjection.getBody());
        this.manufacturer   = bodyProjection.getManufacturer();
        this.model          = bodyProjection.getModel();
        this.doorsNo        = bodyProjection.getDoorsNo();

        this.minPrice       = bodyProjection.getMinPrice();
        this.maxPrice       = bodyProjection.getMaxPrice();
        this.currency       = bodyProjection.getCurrency();
    }
}
