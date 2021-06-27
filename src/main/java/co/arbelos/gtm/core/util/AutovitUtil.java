package co.arbelos.gtm.core.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutovitUtil {

    private static Map<String, String> manufacturerName = new HashMap<String, String>() {
        {
            put("Citroën", "CITROEN");
            put("Citro?�", "CITROEN");
            put("Mercedes-Benz", "MERCEDES");
            put("Mercedes Benz", "MERCEDES");
            put("MINI (BMW)", "MINI");
            put("Škoda", "Skoda");
            put("�oda", "Skoda");
            put("VW", "VOLKSWAGEN");
        }
    };


    private static Map<String, String> manufacturerNameReverse = new HashMap<String, String>() {
        {
            put("MERCEDES", "Mercedes-Benz");
            put("MINI", "MINI (BMW)");
            put("Skoda", "Škoda");
            put("VOLKSWAGEN", "VOLKSWAGEN");
        }
    };

    private static Map<String, String> modelName = new HashMap<String, String>() {
        {
            put("A4 ALLROAD", "A4 ALLROAD QUATTRO");
            put("A6 ALLROAD", "A6 ALLROAD QUATTRO");
            put("TT S", "TTS");
            put("BMW I8", "I8");
            put("SERIA 1", "SERIES 1");
            put("SERIA 2", "SERIES 2");
            put("SERIA 3", "SERIES 3");
            put("SERIA 4", "SERIES 4");
            put("SERIA 5", "SERIES 5");
            put("SERIA 6", "SERIES 6");
            put("SERIA 7", "SERIES 7");
            put("SERIA 8", "SERIES 8");
            put("Z", "Z4");
            put("300c", "300");
            put("C-ELYSÉE", "C-ELYSEE");
            put("CACTUS", "C4 CACTUS");
            put("C4 GRAND PICASSO", "GRAND C4 PICASSO");
            put("JUMPY COMBI", "JUMPY");
            put("DS3", "DS 3");
            put("DS4", "DS 4");
            put("DS5", "DS 5");
            put("124", "124 SPIDER");
            put("PUNTO EVO", "PUNTO");
            put("PUNTO", "PUNTO CLASSIC");
            put("TOURNEO CONNECT", "GRAND TOURNEO CONNECT");
            put("KA", "KA+");
            put("TOURNEO CUSTOM", "TOURNEO COURIER");
            put("EX 35", "EX");
            put("FX 35", "FX");
            put("FX 37", "FX");
            put("FX 45", "FX");
            put("FX30D", "FX");
            put("G37", "G");
            put("XJ12", "XJ SERIES");
            put("XJ40", "XJ SERIES");
            put("XJ6", "XJ SERIES");
            put("XJR", "XJ SERIES");
            put("XKR", "XK SERIES");
            put("CEE'D", "CEED");
            put("PRO_CEE'D", "PROCEED");
            put("SERIA GS", "GS");
            put("GS450H", "GS");
            put("SERIA IS", "IS");
            put("LC 500H", "LC");
            put("SERIA LS", "LS");
            put("LS500H", "LS");
            put("SERIA NX", "NX");
            put("SERIA RX", "RX");
            put("2", "MAZDA2");
            put("3", "MAZDA3");
            put("5", "MAZDA5");
            put("6", "MAZDA6");
            put("A", "A-CLASS");
            put("A 160", "A-CLASS");
            put("A 180", "A-CLASS");
            put("CLASA A", "A-CLASS");
            put("B", "B-CLASS");
            put("CLASA B", "B-CLASS");
            put("C", "C-CLASS");
            put("CLASA C", "C-CLASS");
            put("CL", "CL-CLASS");
            put("CLASA CL", "CL-CLASS");
            put("CLASA CLA", "CLA");
            put("CLA", "CLA-CLASS");
            put("CLC", "CLC-CLASS");
            put("CLASA CLC", "CLC-CLASS");
            put("CLK", "CLK-CLASS");
            put("CLASA CLK", "CLK-CLASS");
            put("CLASA CLS", "CLS-CLASS");
            put("E", "E-CLASS");
            put("CLASA 3", "E-CLASS");
            put("G", "G-CLASS");
            put("CLASA G", "G-CLASS");
            put("GL", "GL-CLASS");
            put("CLASA GL", "GL-CLASS");
            put("GLA", "GLA-CLASS");
            put("GLA 180D", "GLA-CLASS");
            put("CLASA GLA", "GLA-CLASS");
            put("GLC", "GLC-CLASS");
            put("CLASA GLC", "GLC-CLASS");
            put("GLE", "GLE-CLASS");
            put("CLASA GLE", "GLE-CLASS");
            put("GLE COUPE", "GLE-CLASS COUPE");
            put("GLK", "GLK-CLASS");
            put("CLASA GLK", "GLK-CLASS");
            put("GLS", "GLS-CLASS");
            put("CLASA GLS", "GLS-CLASS");
            put("M", "M-CLASS");
            put("CLASA M", "M-CLASS");
            put("R", "R-CLASS");
            put("CLASA R", "R-CLASS");
            put("S", "S-CLASS");
            put("S450 COUPE", "S-CLASS");
            put("CLASA S", "S-CLASS");
            put("SL", "SL-CLASS");
            put("CLASA SL", "SL-CLASS");
            put("CLASA SLC", "SLC-CLASS");
            put("CLASA SLK", "SLK-CLASS");
            put("SLS", "SLS AMG");
            put("CLASA SLS", "SLS AMG");
            put("V", "V-CLASS");
            put("CLASA V", "V-CLASS");
            put("ONE", "MINI");
            put("ECLIPSE", "ECLIPSE CROSS");
            put("350 Z", "350Z");
            put("370 Z", "370Z");
            put("206 PLUS", "206 +");
            put("207 CC", "207");
            put("308 CC", "308");
            put("508 RXH", "508");
            put("CLIO", "CLIO RS");
            put("MEGANE RS", "MEGANE");
            put("SCENIC RX4", "SCENIC");
            put("LAND CRUISER", "LANDCRUISER");
            put("PRIUS +", "PRIUS");
            put("RAV-4", "RAV4");
            put("V40 CC", "V40 CROSS COUNTRY");
            put("XC 40", "XC40");
            put("XC 60", "XC60");
            put("XC 70", "XC70");
            put("XC 90", "XC90");
        }
    };

    private static Map<String, String> modelNameReverse = new HashMap<String, String>() {
        {

            put("A4 ALLROAD QUATTRO", "A4 ALLROAD");
            put("A6 ALLROAD QUATTRO", "A6 ALLROAD");

            put("TTS", "TT S");
            put("I8", "BMW I8");

            put("SERIES 1", "SERIA-1");
            put("SERIES 2", "SERIA-2");
            put("SERIES 3", "SERIA-3");
            put("SERIES 4", "SERIA-4");
            put("SERIES 5", "SERIA-5");
            put("SERIES 6", "SERIA-6");
            put("SERIES 7", "SERIA-7");
            put("SERIES 8", "SERIA-8");

            put("XJ SERIES", "XJ");
            put("XK SERIES", "XK");

            put("MAZDA2", "2");
            put("MAZDA3", "3");
            put("MAZDA5", "5");
            put("MAZAD6", "6");

            put("A-CLASS", "A_CLASSE");
            put("B-CLASS", "B_CLASSE");
            put("C-CLASS", "C_CLASSE");
            put("CL-CLASS", "CL_CLASSE");
            put("CLA-CLASS", "CLA_CLASSE");
            put("CLC-CLASS", "CLC");
            put("CLK-CLASS", "CLK_CLASSE");
            put("CLS-CLASS", "CLS_CLASSE");
            put("E-CLASS", "E_CLASSE");
            put("G-CLASS", "G_CLASSE");
            put("GL-CLASS", "GL_CLASSE");
            put("GLA-CLASS", "GLA_CLASSE");
            put("GLC-CLASS", "GLC_CLASSE");
            put("GLE-CLASS", "GLE_CLASSE");
            put("GLK-CLASS", "GLK");
            put("GLS-CLASS", "GLS-350");
            put("M-CLASS", "M_CLASSE");
            put("R-CLASS", "R_CLASSE");
            put("S-CLASS", "S");
            put("SL-CLASS", "SL");
            put("SLC-CLASS", "CLASA SLC");
            put("SLK-CLASS", "SLK_CLASSE");
            put("SLS AMG", "SLS");
            put("V-CLASS", "V_CLASSE");
        }
    };

    private static Map<String, String> drivenWheels = new HashMap<String, String>() {
        {
            put("Fata", "F");
            put("Spate", "R");
            put("4x4 (automat)", "4");
            put("4x4-manual", "4");
            put("4x4", "4");
        }
    };

    private static Map<String, String> transmission = new HashMap<String, String>() {
        {
            put("Manuala", "M");
            put("Automata", "A");
            put("Automata (dublu ambr", "A");
            put("Automata (CVT)", "A");
            put("Semi-automata (secve", "A");
        }
    };

    private static Map<String, String> transmissionReverse = new HashMap<String, String>() {
        {
            put("M", "Manual");
            put("A", "Automatic");
        }
    };

    private static Map<String, String> fuelType = new HashMap<String, String>() {
        {
            put("Diesel", "D");
            put("Benzina", "U");
            put("Hibrid", "E");
            put("Electric", "E");
            put("Benzina + GPL", "U");
            put("Benzina + CNG", "U");
        }
    };

    public static String getManufacturerForJato(String manufacturer) {
        String result = manufacturerName.getOrDefault(manufacturer, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return manufacturer;
        } else {
            return result;
        }
    }

    public static String getManufacturerForAutovit(String manufacturer) {
        String result = manufacturerNameReverse.getOrDefault(manufacturer, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return manufacturer;
        } else {
            return result;
        }
    }

    public static String getModelForJato(String model) {
        String result = modelName.getOrDefault(model, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return model;
        } else {
            return result;
        }
    }

    public static String getModelForAutovit(String model) {
        String result = modelNameReverse.getOrDefault(model, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return model;
        } else {
            return result;
        }
    }

    public static String getDrivenWheelsForJato(String dw) {
        String result = drivenWheels.getOrDefault(dw, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return dw;
        } else {
            return result;
        }
    }

    public static String getTransmissionTypeForJato(String tr) {
        String result = transmission.getOrDefault(tr, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return tr;
        } else {
            return result;
        }
    }

    public static String getTransmissionTypeForAutovit(String tr) {
        String result = transmissionReverse.getOrDefault(tr, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return tr;
        } else {
            return result;
        }
    }

    public static String getFuelTypeForJato(String fuel) {
        String result = fuelType.getOrDefault(fuel, "UNDEFINED");

        if (result.equals("UNDEFINED")) {
            return fuel;
        } else {
            return result;
        }
    }
}
