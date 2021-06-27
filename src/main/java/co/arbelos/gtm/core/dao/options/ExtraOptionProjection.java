package co.arbelos.gtm.core.dao.options;

public interface ExtraOptionProjection {
    Long getOptionId();
    String getOptionType();
    String getOptionCode();
    String getDescription();
    Float getPrice();
}
