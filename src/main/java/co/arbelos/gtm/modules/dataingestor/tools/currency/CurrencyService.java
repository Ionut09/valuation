package co.arbelos.gtm.modules.dataingestor.tools.currency;

import org.springframework.stereotype.Component;

@Component
public class CurrencyService {

    public static Float convertToGross(String netPrice, Integer year) {
        float price = Float.parseFloat(netPrice);

        return  price * getVatFor(year);
    }

    public static Float convertToNet(String grossPrice, Integer year) {
        float price = Float.parseFloat(grossPrice);

        return  price / getVatFor(year);
    }

    public static Float getVatFor(Integer year) {
        if (year <= 1997) {
            return 1.18F;
        } else if (year <= 1999) {
            return 1.22F;
        } else if (year <= 2000) {
            return 1.19F;
        } else if (year <= 2009) {
            return 1.19F;
        } else if (year <= 2015) {
            return 1.24F;
        } else if (year <= 2016) {
            return 1.20F;
        } else if (year <= 2017) {
            return 1.19F;
        } else {
            return 1.19F;
        }
    }
}
