package co.arbelos.gtm.core.dto.web.wizard;

import co.arbelos.gtm.core.dao.wizard.GenerationProjection;
import co.arbelos.gtm.core.dao.wizard.germandb.GenerationProjectionDE;
import co.arbelos.gtm.core.util.JatoUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerationDTO {
    private Integer generation;
    private Integer modelYear;
    private String manufacturer;
    private String model;
    private String body;
    private Integer doorsNo;

    private Long minPrice;
    private Long maxPrice;
    private String currency;

    public GenerationDTO(GenerationProjection generationProjection) {
        this.generation     = generationProjection.getGeneration();
        this.modelYear      = generationProjection.getModelYear();
        this.manufacturer   = generationProjection.getManufacturer();
        this.model          = generationProjection.getModel();
        this.body           = JatoUtil.getBodyType(generationProjection.getBody());
        this.doorsNo        = generationProjection.getDoorsNo();

        this.minPrice       = generationProjection.getMinPrice();
        this.maxPrice       = generationProjection.getMaxPrice();
        this.currency       = generationProjection.getCurrency();
    }

    public GenerationDTO(GenerationProjectionDE generationProjection) {
        this.generation     = generationProjection.getGeneration();
        this.modelYear      = generationProjection.getModelYear();
        this.manufacturer   = generationProjection.getManufacturer();
        this.model          = generationProjection.getModel();
        this.body           = JatoUtil.getBodyType(generationProjection.getBody());
        this.doorsNo        = generationProjection.getDoorsNo();

        this.minPrice       = generationProjection.getMinPrice();
        this.maxPrice       = generationProjection.getMaxPrice();
        this.currency       = generationProjection.getCurrency();
    }
}
