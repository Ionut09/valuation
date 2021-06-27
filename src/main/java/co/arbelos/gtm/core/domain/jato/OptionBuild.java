package co.arbelos.gtm.core.domain.jato;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "option_build")
@Data
@NoArgsConstructor
public class OptionBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "option_id")
    private Integer optionId;

    @Column(name = "rule_type")
    private Integer ruleType;

    @Lob
    @Column(name = "`condition`")
    private String condition;

    @Column(name = "option_rule")
    private String optionRule;

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
