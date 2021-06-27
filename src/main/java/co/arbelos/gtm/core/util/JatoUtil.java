package co.arbelos.gtm.core.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JatoUtil {

    private static Map<String, String> bodyTypes = new HashMap<String, String>() {
        {
            put("BH", "Chassis Cab");
            put("BP", "Single cab pick-up");
            put("BT", "Cutaway");
            put("BU", "Passenger Van");
            put("CA", "Convertible");
            put("CC", "Combi");
            put("CE", "Commercial Wagon");
            put("CH", "Commercial Hatch");
            put("CO", "Coupe");
            put("CP", "Pick-up");
            put("CV", "Car Van");
            put("CX", "Chassis Only");
            put("DH", "Double Chassis Cab");
            put("DP", "Double Cab Pick Up");
            put("ES", "Wagon");
            put("FW", "Minivan");
            put("HA", "Hatchback");
            put("MC", "Micro Car");
            put("MM", "Mini MPV");
            put("OC", "Off Road Commercial");
            put("OD", "Sport Utility Vehicle");
            put("OM", "Omnibus");
            put("PU", "Pick-up");
            put("PV", "Cargo Van");
            put("RV", "Parcel Van");
            put("SA", "Sedan");
            put("SH", "Sedan Hard Top");
            put("TA", "Targa");
            put("TC", "Triple Cab");
            put("TR", "Heavy Truck");
            put("VA", "Van");
            put("WH", "Chassis Cowl");
            put("WT", "Platform Cowl");
            put("WV", "Window Van");

            // comercial cars
            put("CM", "Campervan");
            put("DS", "Drop Side");
            put("TI", "Tipper");
        }
    };

    private static Map<String, String> bodyTypeCodes = new HashMap<String, String>() {
        {
            put("Chassis Cab", "BH");
            put("Chassis cab", "BH");
            put("Single cab pick-up", "BP");
            put("Cutaway", "BT");
            put("Passenger Van", "BU");
            put("Passenger van", "BU");
            put("Convertible", "CA");
            put("Combi", "CC");
            put("Commercial Wagon", "CE");
            put("Commercial wagon", "CE");
            put("Commercial Hatch", "CH");
            put("Commercial hatch", "CH");
            put("Coupe", "CO");
            put("Car Van", "CV");
            put("Car van", "CV");
            put("Chassis Only", "CX");
            put("Chassis only", "CX");
            put("Double Chassis Cab", "DH");
            put("Double chassis cab", "DH");
            put("Double Cab Pick Up", "DP");
            put("Double cab pick up", "DP");
            put("Wagon", "ES");
            put("Minivan", "FW");
            put("Hatchback", "HA");
            put("Micro Car", "MC");
            put("Micro car", "MC");
            put("Mini MPV", "MM");
            put("Mini mpv", "MM");
            put("Off Road Commercial", "OC");
            put("Off road commercial", "OC");
            put("Sport Utility Vehicle", "OD");
            put("Sport utility vehicle", "OD");
            put("Omnibus", "OM");
            put("Pick-up", "PU");
            put("Cargo Van", "PV");
            put("Cargo van", "PV");
            put("Parcel Van", "RV");
            put("Parcel van", "RV");
            put("Sedan", "SA");
            put("Sedan Hard Top", "SH");
            put("Sedan hard top", "SH");
            put("Targa", "TA");
            put("Triple Cab", "TC");
            put("Triple cab", "TC");
            put("Heavy Truck", "TR");
            put("Heavy truck", "TR");
            put("Van", "VA");
            put("Chassis Cowl", "WH");
            put("Chassis cowl", "WH");
            put("Platform Cowl", "WT");
            put("Platform cowl", "WT");
            put("Window Van", "WV");
            put("Window van", "WV");

            // comercial cars
            put("Campervan", "CM");
            put("Drop Side", "DS");
            put("Drop side", "DS");
            put("Tipper", "TI");
        }
    };


    private static Map<String, String> transmissions = new HashMap<String, String>() {
        {
            put("M", "Manual");
            put("A", "Automatic");
        }
    };

    private static Map<String, String> transmissionCodes = new HashMap<String, String>() {
        {
            put("Manual", "M");
            put("Automatic", "A");
        }
    };

    private static Map<String, String> fuelTypes = new HashMap<String, String>() {
        {
            put("A", "Methanol");
            put("B", "Biodiesel");
            put("D", "Diesel");
            put("E", "Electric");
            put("F", "E85");
            put("G", "LPG");
            put("L", "Leaded");
            put("M", "M85");
            put("N", "Compressed Natural Gas");
            put("P", "Premium Unleaded");
            put("U", "Unleaded");
        }
    };

    private static Map<String, String> fuelTypeCodes = new HashMap<String, String>() {
        {
            put("Methanol",                 "A");
            put("Biodiesel",                "B");
            put("Diesel",                   "D");
            put("Electric",                 "E");
            put("E85",                      "F");
            put("LPG",                      "G");
            put("Leaded",                   "L");
            put("M85",                      "M");
            put("Compressed Natural Gas",   "N");
            put("Premium Unleaded",         "P");
            put("Unleaded",                 "U");
            put("Gasoline",                 "U");
        }
    };


    private static Map<String, String> drivenWheels = new HashMap<String, String>() {
        {
            put("4",        "4X4");
            put("D",        "Direct");
            put("F",        "Front");
            put("R",        "Rear");
        }
    };

    private static Map<String, String> drivenWheelCodes = new HashMap<String, String>() {
        {
            put("4X4",      "4");
            put("Direct",   "D");
            put("Front",    "F");
            put("Rear",     "R");
        }
    };

    // body type
    public static String getBodyType(String abbreviation) {
        return bodyTypes.getOrDefault(abbreviation, "UNDEFINED");
    }
    public static String getBodyTypeCode(String abbreviation) {
        return bodyTypeCodes.getOrDefault(abbreviation, "UNDEFINED");
    }

    // transmission
    public static String getTransmission(String abbreviation) {
        return transmissions.getOrDefault(abbreviation, "UNDEFINED");
    }
    public static String getTransmissionCode(String fullVersion) {
        return transmissionCodes.getOrDefault(fullVersion, "UNDEFINED");
    }

    // fuel type
    public static String getFuelType(String abbreviation) {
        return fuelTypes.getOrDefault(abbreviation, "UNDEFINED");
    }
    public static String getFuelTypeCode(String fullVersion) {
        return fuelTypeCodes.getOrDefault(fullVersion, "UNDEFINED");
    }

    // driven wheels
    public static String getDrivenWheels(String abbreviation) {
        return drivenWheels.getOrDefault(abbreviation, "UNDEFINED");
    }
    public static String getDrivenWheelCodes(String fullVersion) {
        return drivenWheelCodes.getOrDefault(fullVersion, "UNDEFINED");
    }
}
