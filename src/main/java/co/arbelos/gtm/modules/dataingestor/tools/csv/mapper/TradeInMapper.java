package co.arbelos.gtm.modules.dataingestor.tools.csv.mapper;

import co.arbelos.gtm.core.domain.marketobservation.MarketData;
import co.arbelos.gtm.modules.dataingestor.tools.csv.exceptions.MalformedCSVException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class TradeInMapper {

    private static final Logger log = LoggerFactory.getLogger(TradeInMapper.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("[dd-MM-yy][yyyy-MM-dd][yyyy-MM-d]");

    public static MarketData fromMap(Map<String, String> map) {
        MarketData marketData = new MarketData();

        try {
            String model = map.get("manufacturer");
            if (model == null) {
                model = map.get("\uFEFFmanufacturer");
            }

            marketData.setManufacturer(model.toUpperCase());
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'manufacturer' is malformed!");
        }

        try {
            marketData.setModel(map.get("model").toUpperCase());
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'model' is malformed!");
        }

        try {
            marketData.setTransmissionType(map.get("transmission_type").toUpperCase());
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'transmission_type' is malformed!");
        }

        try {
            marketData.setEngineHp(Integer.valueOf(map.get("engine_hp")));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'engine_hp' is malformed!");
        }

        try {
            marketData.setFuelType(map.get("fuel_type").toUpperCase());
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'fuel_type' is malformed!");
        }

        try {
            marketData.setManufacturerYear(Integer.parseInt(map.get("manufacturer_year")));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'manufacturer_year' is malformed!");
        }

        try {
            marketData.setKmNo(Integer.valueOf(map.get("km")));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'km' is malformed!");
        }

        try {
            String[] priceString = map.get("price").split(",");
            String price = priceString[0].replaceAll("[^a-zA-Z0-9]", "");
            marketData.setPrice(Float.parseFloat(price));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'price' is malformed!");
        }

        try {
            marketData.setSource(map.get("source"));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'source' is malformed!");
        }

        try {
            marketData.setVin(map.get("vin"));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'vin' is malformed!");
        }

        try {
            marketData.setCollectionDate(LocalDate.parse(map.get("collection_date"), dateTimeFormatter));
        } catch (Exception e) {
            throw new MalformedCSVException("Field 'collection_date' is malformed!");
        }

        log.debug("Market item to be persisted: {}", marketData);
        return marketData;
    }
}
