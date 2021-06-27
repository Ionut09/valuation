package co.arbelos.gtm.core.domain.jato.germandb;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "version_de")
@Data
@NoArgsConstructor
public class VersionDE {

    /**
     * Vehicle Identity ­ The unique number that identifies the vehicle;
     * the vehicle_id number is derived from the data_date (id_104) of the manufacturers price list/brochure
     * and JATO's 6­ or 7­digit persistent vehicle number (id_101).
     */
    @Id
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    /**
     * Unique Identity (UID) The unique identity of the vehicle: the persistent vehicle number
     */
    @Column(name = "id_101")
    private Integer uid;

    /**
     * Researcher Initials The JATO researchers initials
     */
    @Column(name = "id_102")
    private String researcherInitials;

    /**
     * The date the new prices and specifications become effective in the market place.
     * If no date is specified in the documentation, we first seek verification of the date the vehicle became active
     * from the importer/manufacturer. If not, the first day of the month is coded.
     */
    @Column(name = "id_104")
    private Integer pricesDate;

    /**
     * Date of Change - the date the change was announced by the manufacturer.
     */
    @Column(name = "id_120")
    private Integer dateOfChange;

    /**
     * Research Date - the date the vehicle was last researched.
     */
    @Column(name = "id_103")
    private Integer researchDate;

    /**
     * Conclude Date The date a version was withdrawn or replaced, which is useful when querying version history.
     * Version history is only available in full­history databases.
     * Current vehicles are identified by an open conclude date of 99991231
     */
    @Column(name = "id_113")
    private Integer concludeDate;

    /**
     * Data Status - the status of the data in the database as it relates to the actual situation in the market.
     */
    @Column(name = "id_106")
    private String dataStatus;

    /**
     * Model Year ­ The model year of the vehicle
     * Each year, manufacturers may update vehicles for the forthcoming year.
     * The differences between one model year and another can be the introduction of a new model,
     * or revised trims levels or when no changes at all have occurred.
     * In some countries model year changes are governed by law.
     */
    @Column(name = "id_108")
    private String modelYear;

    /**
     * Country The country code is defined as follows
     */
    @Column(name = "id_109")
    private String countryCode;

    /**
     *  Responsible Market UID Identifies the vehicle in the home market that acts as the specification benchmark
     *  for quality assurance of this vehicle
     */
    @Column(name = "id_107")
    private Integer responsibleMarketUid;

    /**
     * Date Physically Checked - the date the vehicle was physically checked
     */
    @Column(name = "id_26801")
    private Integer physicallyCheckDate;

    /**
     * Make Group This selection field allows selection of the parent company for the Make, if any.
     * For example, the Ford make group currently includes Aston Martin, Ford, Jaguar, Land Rover, Lincoln, Mercury and Volvo
     */
    @Column(name = "id_117")
    private String parentCompany;

    /**
     *  This is the name of the vehicle manufacturer responsible for national distribution within the country of research.
     *  Vehicle names can be translated in the schema_text table.
     */
    @Column(name = "id_111")
    private String vehicleManufacturerName;

    /**
     * Local Make Name - the make name in the local language of the country.
     * These are contained in the schema_text table where available.
     */
    @Column(name = "id_128")
    private String localManufacturerName;

    /**
     * Model - t­he name under which the vehicle is officially marketed.
     * The model name should not be confused with the version name or description.
     * e.g. The Pontiac Firebird Formula and the Firebird Trans Am are different versions of the model Firebird.
     * Model names can be translated in the schema_text table.
     */
    @Column(name = "id_112")
    private String modelName;

    /**
     * Local Make Name The make name in the local language of the country.
     * These are contained in the schema_text table where available.
     */
    @Column(name = "id_129")
    private String localModelName;

    /**
     * Model Descriptor - the identity of different models in order to differentiate between them.
     * A selection list field is used to describe the differences between models.
     */
    @Column(name = "id_130")
    private String modelDescriptor;

    /**
     * Version Name The name that is used to uniquely define the vehicle,
     * it will usually include the name given for the version in the manufactures/importers price list.
     * It is usually a combination of transmission, trim level and engine features.
     * It excludes the make and model name, e.g. 1.9 TDI LX or 2.0 16v Elegance
     */
    @Column(name = "id_302")
    private String versionName;

    /**
     * Local Version Name - the version name in the local language of the country.
     */
    @Column(name = "id_131")
    private String localVersionName;

    /**
     * Special/Limited Edition - defines if the vehicle is a special/limited edition
     * - = NO
     * Y = YES
     */
    @Column(name = "id_403")
    private String limitedEdition;

    /**
     * Trim Level The trim level is the comfort or equipment level, defined by letters or words (for example: GLX, Ghia, and GTI).
     * The trim level is used by the manufacturer to differentiate between differing levels of equipment on the same model.
     * Trim level names can be translated in the schema_text table.
     */
    @Column(name = "id_402")
    private String trimLevel;

    /**
     * Local Trim Level - the Trim Level in the local language of the country
     */
    @Column(name = "id_404")
    private String localTrimLevel;

    /**
     * Trim Classification - the trim classification exists to identify between similar trim levels across different countries.
     * Trim Classification is separate for each body type.
     */
    @Column(name = "id_405")
    private String trimClassification;

    /**
     * Pricing Descriptor ­ This helps distinguish vehicles that have the same trim level and version name.
     * It describes why the version exists, or what the difference is between the versions.
     */
    @Column(name = "id_114")
    private String pricingDescriptor;

    /**
     * Number of Doors By door it means an entry for a person to enter the passenger compartment,
     * or to access the load compartment in the case of hatchbacks, station wagons and trucks.
     * For passenger cars, the load compartment door will only be taken in account if the load compartment creates
     * one space with the passenger compartment: as in a hatchback, a station wagon or a sport utility.
     * In the case of a sedan body type, the trunk is not counted as a door.
     * For a coupe, where the trunk lid is in fact a hatch, it is counted as a door.
     * Note: Pick up cargo doors are not considered as doors.
     * Doors that are made of fabric or vinyl are also considered as doors.
     */
    @Column(name = "id_602")
    private String numberOfDoors;

    /**
     * Body Type ­ The body type describes the shape and/or function of the vehicle's body shell.
     * The body types used by JATO are standard terms accepted across the automotive industry.
     * All available body types are coded as they are listed in the official documentation.
     * JATO is only interested in modifications when they are presented either as stand­alone versions or optional.
     * When a pick­up is offered as an option based on a chassis­cab the pick­up is inserted as an extra version.
     * The price information will include the base version with the price of the optional load platform added.
     */
    @Column(name = "id_603")
    private String bodyType;

    /**
     * Cab Type - the type of driving cab for panel vans, chassis­cabs and pick­ups
     */
    @Column(name = "id_604")
    private String cabType;

    /**
     * Local Number of Doors The number of doors that the country itself says the vehicle has, if this differs from id_602.
     * e.g. in the US a Station Wagon is often called a 4 door even though by JATOs research standards it is a 5 door.
     */
    @Column(name = "id_605")
    private String localNumberOfDoors;

    /**
     *  Local Body Type The body type as defined by the manufacturer or country
     *  e.g. one manufacturer used to refer to their 3 door hatchback a coupe
     */
    @Column(name = "id_606")
    private String localBodyType;

    /**
     * Roof Type - the Roof Type of the vehicle defined as:
     */
    @Column(name = "id_607")
    private String roofType;

    /**
     * Wheelbase - the type of wheelbase fitted to a commercial vehicle.
     */
    @Column(name = "id_608")
    private String wheelbaseType;

    /**
     * Driver location
     */
    @Column(name = "id_609")
    private String driverLocation;

    /**
     * Driven Wheels - indicates which wheels are driven by the engine.
     */
    @Column(name = "id_6502")
    private String driverWheels;

    /**
     * Transmission Type - the Gear Lever or selector of the vehicle
     */
    @Column(name = "id_20602")
    private String transmissionType;

    /**
     * Engine Litres - the engine litres of the vehicle
     */
    @Column(name = "id_7403")
    private Float engineLitres;

    /**
     * Maximum Power kW ­ The maximum power of the engine given in killowatts (kW).
     */
    @Column(name = "id_15303")
    private Integer maximumPowerKW;

    /**
     * Maximum Power HP/PS ­ The maximum engine output given in Horse Power (HP)
     */
    @Column(name = "id_15304")
    private Integer maximumPowerHP;

    /**
     * Fuel Type - the primary fuel type used by the engine, and any alternative fuel type that may be used.
     */
    @Column(name = "id_8702")
    private String fuelType;

    /**
     * Compressor ­- the type of compressor used
     */
    @Column(name = "id_7502")
    private String compressorType;

    /**
     * Seating Capacity - the number of people that the vehicle can carry in standard configuration.
     * This figure excludes optional seats.
     */
    @Column(name = "id_702")
    private String seatingCapacity;

    /**
     * Price Currency ­- the currency in which prices are displayed for this vehicle.
     */
    @Column(name = "id_901")
    private String priceCurrency;

    /**
     * MSRP ­ The Manufacturers Suggested Retail Price
     */
    @Column(name = "id_902")
    private Float retailPrice;

    /**
     * Base Price ­ This is the official published price for the customer,
     * excluding all taxes and other charges which apply to the consumer (e.g. VAT, Special Car Tax).
     * It does include any import duties payable.
     */
    @Column(name = "id_903")
    private Float basePrice;

    /**
     * Country Specific Price This field is used to store prices that occur in a single country
     * e.g. on the road price in the U.K.
     */
    @Column(name = "id_904")
    private Float countrySpecificPrice;

    /**
     * Wholesale Price The same as id_904
     */
    @Column(name = "id_905")
    private Float wholesalePrice;

    /**
     * Manufacturers Suggested Retail Price (MSRP) including delivery charge
     */
    @Column(name = "id_906")
    private Float suggestedRetailPrice;

    /**
     * - = NO
     * Y = YES
     */
    @Column(name = "id_132")
    private String netPrice;

    /**
     * Country of Assembly
     * For a list of country codes refer to id_109
     */
    @Column(name = "id_110")
    private String countryOfAssembly;

    /**
     * Date Version Introduced -­ this is the date when the version was first available to customers with prices or
     * when it received a facelift
     */
    @Column(name = "id_115")
    private Integer versionDate;

    /**
     * Date Model Introduced ­ The date when the whole model range was first introduced or had a facelift
     */
    @Column(name = "id_125")
    private Integer modelIntroductionDate;

    /**
     * Vehicle Type - the Vehicles type.
     */
    @Column(name = "id_116")
    private String vehicleType;

    /**
     * Manufacturers code - this code is used by the manufacturer or importer to identify their vehicles.
     * It is often a combination of body codes and option packages.
     */
    @Column(name = "id_502")
    private String manufacturerCode;

    /**
     * First Research Date - the first Research date
     */
    @Column(name = "id_121")
    private Integer firstResearchDate;

    /**
     * Data Status Research Date - the date the research was conducted
     */
    @Column(name = "id_122")
    private Integer conductedResearchDate;

    /**
     *  Vehicle Identification Number (VIN) - the vehicle identification number or chassis number.
     */
    @Column(name = "id_123")
    private String vehicleVin;

    /**
     * Make Continent Group - the Make continent group is the continent in which the head office of a Make group is situated,
     * useful for identifying concepts such as Japanese cars when in fact most are no longer made in Japan
     */
    @Column(name = "id_28101")
    private String makeContinentGroup;

    /**
     * Model Group ­ Model group names the best­known current model, and contains equivalent models historically and across markets.
     * e.g. the Ford Mondeo group includes the Ford Sierra and Ford Cortina.
     * The Opel Astra model group includes the Opel Kadett, the Vauxhall Astra and the Pontiac Le Mans.
     */
    @Column(name = "id_28001")
    private String modelGroup;

    /**
     * Version information
     */
    @Column(name = "id_301")
    private String versionInformation;

    /**
     * Latest Price list Date/Number - A free text field containing the issue # of the latest price list which may be its date or a
     * reference number.
     */
    @Column(name = "id_303")
    private String latestIssuedPriceList;

    /**
     * Release date of the price list
     */
    @Column(name = "id_304")
    private Integer priceListReleaseDate;

    /**
     * Cash/Net price The Cash/Net price describes the actual price quoted by the manufacturer/importer if the vehicle is offered
     * for a cash or specific non negotiable "net" price. The price entered is as regularly coded in the local market
     */
    @Column(name = "id_318")
    private Float actualPrice;

    /**
     * Finance price The finance price describes the price of the vehicle on the condition that a finance agreement is entered into.
     * This applies to Mexico only.
     */
    @Column(name = "id_319")
    private Float financePrice;

    /**
     * Make Code - manufacturers code for the model
     */
    @Column(name = "id_305")
    private String modelManufacturersCode;

    /**
     * Model Code Manufacturers code for the version
     */
    @Column(name = "id_306")
    private String versionManufacturersCode;

    /**
     * Global segment code
     */
    @Column(name = "id_174")
    private Integer segmentCode;
}
