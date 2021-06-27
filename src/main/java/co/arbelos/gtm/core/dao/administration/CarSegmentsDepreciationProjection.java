package co.arbelos.gtm.core.dao.administration;

public interface CarSegmentsDepreciationProjection {
    Long getId();
    Integer getDataValue();
    Float getFirstYear();
    Float getSecondYear();
    Float getThirdYear();
    Float getFourthYear();
    Float getFifthYear();
    String getText();
}
