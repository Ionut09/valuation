package co.arbelos.gtm.valuation.service.vin.util;

import java.util.HashMap;
import java.util.Map;

final public class VinUtil {

    private static Map<String, String> bodyTypes = new HashMap<String, String>() {
        {
            put("BER", "Sedan");
            put("MON", "Passenger Van");
            put("RAN", "Commercial Wagon");
            put("COM", "Sport Utility Vehicle");
            put("COU", "Coupe");
            put("TOT", "Off Road Commercial");
        }
    };

    private static Map<String, String> fuelType = new HashMap<String, String>() {
        {
            put("DSL", "Diesel");
            put("TDS", "Diesel");
            put("GPL", "LPG");
            put("GNC", "Compressed Natural Gas");
            put("BIF", "Unleaded");
            put("IMU", "Unleaded");
            put("IMO", "Unleaded");
            put("INY", "Unleaded");
            put("CAR", "Unleaded");
        }
    };

    public static String getBody(String abbreviation) {
        return bodyTypes.getOrDefault(abbreviation, "UNDEFINED");
    }

    public static String getFuel(String abbreviation) {
        return fuelType.getOrDefault(abbreviation, "UNDEFINED");
    }
}
