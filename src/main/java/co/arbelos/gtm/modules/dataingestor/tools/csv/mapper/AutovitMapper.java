package co.arbelos.gtm.modules.dataingestor.tools.csv.mapper;

import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import co.arbelos.gtm.core.util.AutovitUtil;
import co.arbelos.gtm.modules.dataingestor.tools.csv.exceptions.MalformedCSVException;
import co.arbelos.gtm.modules.dataingestor.tools.currency.CurrencyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class AutovitMapper {
    private static final Logger log = LoggerFactory.getLogger(AutovitMapper.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[dd-MM-yy][yyyy-MM-dd][yyyy-MM-d]");

//    @Deprecated
//    public static Ad from(Map<String, String> map) {
//        Ad ad = new Ad();
//
//        ad.setAdURI(map.get("id_anunt"));
//
//        // DATES
//        if (!map.get("data_anunt").equals("") && !map.get("data_anunt").equals(" ")) {
//            ad.setAdDate(LocalDate.parse(StringUtils.substringBefore(map.get("data_anunt"), " "), dateTimeFormatter));
//        } else {
//            throw new MalformedCSVException("Field data_anunt is empty!");
//        }
//
//        if (!map.get("creation_date").equals("") && !map.get("creation_date").equals(" ")) {
//            ad.setCollectionDate(LocalDate.parse(StringUtils.substringBefore(map.get("creation_date"), " "), dateTimeFormatter));
//        } else {
//            throw new MalformedCSVException("Field creation_date is empty!");
//        }
//
//        if (!map.get("anul_fabricatiei").equals("") && !map.get("anul_fabricatiei").equals(" ")) {
//            ad.setVehicleManufactoringYear(Integer.parseInt(map.get("anul_fabricatiei")));
//        }
//
//        if (!map.get("data_primei_inmatriculari").equals("") && !map.get("data_primei_inmatriculari").equals(" ")) {
//            try {
//                ad.setVehicleRegistrationDate(YearMonth.parse(StringUtils.substringBefore(map.get("data_primei_inmatriculari"), " ")).atDay(1));
//            } catch (Exception e) { }
//        }
//
//        ad.setVehicleManufacturerName(AutovitUtil.getManufacturerForJato(map.get("marca")).toUpperCase());
//        ad.setVehicleModelName(AutovitUtil.getModelForJato(map.get("model").toUpperCase()));
//        ad.setVehicleVersionName(map.get("versiune"));
//
//
//        if (!map.get("portiere").equals("") && !map.get("portiere").equals(" ")) {
//            try {
//                ad.setNumberOfDoors(Integer.parseInt(map.get("portiere")));
//            } catch (NumberFormatException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        ad.setBodyType(map.get("caroserie"));
//        ad.setDrivenWheels(AutovitUtil.getDrivenWheelsForJato(map.get("transmisie")));
//
//        if (!map.get("cp").equals("") && !map.get("cp").equals(" ")) {
//            ad.setEngineHP(Integer.valueOf(map.get("cp").replaceAll(" ","")));
//        }
//
//        if (!map.get("cmc").equals("") && !map.get("cmc").equals(" ")) {
//            ad.setCmc(Integer.parseInt(map.get("cmc")));
//        }
//
//        ad.setTransmissionType(AutovitUtil.getTransmissionTypeForJato(map.get("cv")).toUpperCase());
//        ad.setFuelType(AutovitUtil.getFuelTypeForJato(map.get("combustibil")).toUpperCase());
//
//        if (!map.get("km").equals("") && !map.get("km").equals(" ")) {
//            try {
//                ad.setNumberOfKm(Integer.valueOf(map.get("km")));
//            } catch (NumberFormatException e) {
//                throw  new MalformedCSVException("Field km is malformed: " + e.getMessage());
//            }
//        } else {
//            throw new MalformedCSVException("Field km is empty!");
//        }
//
//        if (!map.get("moneda").equals("") && !map.get("moneda").equals(" ") ||
//            !map.get("oferit_de").equals("") && !map.get("oferit_de").equals(" ") ||
//            !map.get("detalii_pret").equals("") && !map.get("detalii_pret").equals(" ") ||
//            !map.get("pret").equals("") && !map.get("pret").equals(" ")) {
//
//            if (map.get("moneda").equals("EUR")) {
//                if (checkPriceForVAT(map.get("detalii_pret"))) {
//                    ad.setPrice(CurrencyService.convertToNet(map.get("pret"), ad.getAdDate().getYear()));
//                } else {
//                    ad.setPrice(Float.parseFloat(map.get("pret")));
//                }
//            }
//        } else {
//            throw  new MalformedCSVException("Fields 'moneda' or 'oferit_de' or 'detalii_pret' or 'pret' don't exist!");
//        }
//
//        ad.setEquipments(map.get("echipamente"));
//
//        return ad;
//    }

    public static MarketData fromMap(Map<String, String> map) {
        MarketData marketData = new MarketData();

        try {
            marketData.setManufacturer(AutovitUtil.getManufacturerForJato(map.get("marca")).toUpperCase());
            marketData.setModel(AutovitUtil.getModelForJato(map.get("model").toUpperCase()));
            marketData.setTransmissionType(AutovitUtil.getTransmissionTypeForJato(map.get("cv")).toUpperCase());

            if (!map.get("cp").equals("") && !map.get("cp").equals(" ")) {
                try {
                    marketData.setEngineHp(Integer.valueOf(map.get("cp").replaceAll(" ", "")));
                } catch (NumberFormatException e) {
                    throw new MalformedCSVException("Field 'cp' is malformed: " + e.getMessage());
                }
            }

            marketData.setFuelType(AutovitUtil.getFuelTypeForJato(map.get("combustibil")).toUpperCase());

            if (!map.get("anul_fabricatiei").equals("") && !map.get("anul_fabricatiei").equals(" ")) {
                marketData.setManufacturerYear(Integer.parseInt(map.get("anul_fabricatiei")));
            }

            if (!map.get("km").equals("") && !map.get("km").equals(" ")) {
                try {
                    marketData.setKmNo(Integer.valueOf(map.get("km")));
                } catch (NumberFormatException e) {
                    throw new MalformedCSVException("Field 'km' is malformed: " + e.getMessage());
                }
            } else {
                throw new MalformedCSVException("Field 'km' is empty!");
            }

            if (!map.get("creation_date").equals("") && !map.get("creation_date").equals(" ")) {
                marketData.setCollectionDate(LocalDate.parse(StringUtils.substringBefore(map.get("creation_date"), " "), dateTimeFormatter));
            } else {
                throw new MalformedCSVException("Field 'creation_date' is empty!");
            }

            if (!map.get("moneda").equals("") && !map.get("moneda").equals(" ") ||
                !map.get("detalii_pret").equals("") && !map.get("detalii_pret").equals(" ") ||
                !map.get("pret").equals("") && !map.get("pret").equals(" ")) {

                if (map.get("pret").equals("0")) {
                    throw new MalformedCSVException("Fields 'moneda' or 'detalii_pret' or 'pret' have the value of 0!");
                }

                if (map.get("moneda").equals("EUR")) {
                    if (checkPriceForVAT(map.get("detalii_pret"))) {
                        marketData.setPrice(CurrencyService.convertToNet(map.get("pret"), marketData.getCollectionDate().getYear()));
                    } else {
                        marketData.setPrice(Float.parseFloat(map.get("pret")));
                    }
                }
            } else {
                throw new MalformedCSVException("Fields 'moneda' or 'detalii_pret' or 'pret' might not exist!");
            }

            marketData.setVin(map.get("vin"));
            marketData.setSource("AUTOVIT");

            log.debug("Market item to be persisted: {}", marketData);
        } catch (Exception e) {
            throw new MalformedCSVException("The file has an improper format or some columns are missing. Check for ',' separator to be used!");
        }
        return marketData;
    }

    private static boolean checkPriceForVAT(String priceDetails) {
        return !priceDetails.toLowerCase().contains("net");
    }
}
