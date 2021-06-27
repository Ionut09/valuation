package co.arbelos.gtm.core.dto.web.valuation;

import co.arbelos.gtm.core.dao.valuation.ValuationInfoProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValuationInfoDTO {
    private String collectionDate;
    private Integer adsNumber;
    private Float averagePrice;
    private Float standardDeviation;

    public ValuationInfoDTO(ValuationInfoProjection valuationInfoProjection) {
        this.collectionDate = valuationInfoProjection.getAdMonth();
        this.adsNumber = valuationInfoProjection.getAdsNumber();
        this.averagePrice = valuationInfoProjection.getAvgPrice();
        this.standardDeviation = valuationInfoProjection.getStandardDeviation();
    }
}
