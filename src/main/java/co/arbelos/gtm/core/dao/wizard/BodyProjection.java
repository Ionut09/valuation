package co.arbelos.gtm.core.dao.wizard;

public interface BodyProjection {
    String getBody();
    String getManufacturer();
    String getModel();
    Integer getDoorsNo();

    Long getMinPrice();
    Long getMaxPrice();
    String getCurrency();
}
