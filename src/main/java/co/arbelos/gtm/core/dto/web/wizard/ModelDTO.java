package co.arbelos.gtm.core.dto.web.wizard;


import co.arbelos.gtm.core.dao.wizard.ModelProjection;
import co.arbelos.gtm.core.dao.wizard.germandb.ModelProjectionDE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModelDTO {
    private String model;
    private String manufacturer;
    private Long minPrice;
    private Long maxPrice;
    private String currency;

    private String imageUrl;

    public ModelDTO(ModelProjection projection) {
        this.model          = projection.getModel();
        this.manufacturer   = projection.getManufacturer();
        this.minPrice       = projection.getMinPrice();
        this.maxPrice       = projection.getMaxPrice();
        this.currency       = projection.getCurrency();
    }

    public ModelDTO(ModelProjectionDE projection) {
        this.model          = projection.getModel();
        this.manufacturer   = projection.getManufacturer();
        this.minPrice       = projection.getMinPrice();
        this.maxPrice       = projection.getMaxPrice();
        this.currency       = projection.getCurrency();
    }
}
