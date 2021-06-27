package co.arbelos.gtm.core.dao.wizard;

public interface GenerationProjection {
    Integer getGeneration();
    Integer  getModelYear();
    String  getManufacturer();
    String  getModel();
    String  getBody();
    Integer getDoorsNo();

    Long    getMinPrice();
    Long    getMaxPrice();
    String  getCurrency();
}
