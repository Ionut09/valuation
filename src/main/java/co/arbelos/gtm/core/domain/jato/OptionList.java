package co.arbelos.gtm.core.domain.jato;

import co.arbelos.gtm.core.domain.jato.primarykeys.OptionListPK;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "option_list")
@Data
@NoArgsConstructor
public class OptionList {

    @EmbeddedId
    private OptionListPK optionListPK;

    @Column(name = "option_type")
    private String optionType;

    @Column(name = "option_code")
    private String optionCode;

    @Column(name = "manuf_name")
    private String manufacturerName;

    /*
     * MSRP ­ The Manufacturers Suggested Retail Price
     **/
    @Column(name = "id_902")
    private Float retailPrice;

    /*
     * Base Price ­ This is the official published price for the customer, excluding all taxes and other charges
     * which apply to the consumer (e.g. VAT, Special Car Tax). It does include any import duties payable.
     * */
    @Column(name = "id_903")
    private Float basePrice;

    /*
     * Country Specific Price This field is used to store prices that occur in a single country
     * e.g. on the road price in the U.K.
     * */
    @Column(name = "id_904")
    private Float countryPrice;

    /*
     *  Wholesale Price The same as id_904
     * */
    @Column(name = "id_905")
    private Float wholeSalePrice;

    /*
     *  MSRP The Secondary Manufactures Suggested Retail Price
     * */
    @Column(name = "id_100902")
    private Float secondaryManufacturesPrice;

    /*
     *  The Secondary Base Price ­ This is the official published price for the customer,
     *  excluding all taxes and other charges which apply to the consumer (e.g. VAT, Special Car Tax).
     * */
    @Column(name = "id_100903")
    private Float secondaryBasePrice;

    /*
     *  The Secondary Country Specific Price This field is used to store prices that occur in a single country
     *  e.g. on the road price in the U.K.
     * */
    @Column(name = "id_100904")
    private Float secondaryCountryPrice;

    /*
     *  The Secondary Country Specific Price The same as id_100904
     * */
    @Column(name = "id_100905")
    private Float secondaryCountrySpecificPrice;
}
