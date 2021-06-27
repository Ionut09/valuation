package co.arbelos.gtm.valuation.service.images;

import co.arbelos.gtm.core.dto.web.wizard.VehicleDTO;
import co.arbelos.gtm.core.repository.jato.VersionRepository;
import co.arbelos.gtm.core.util.JatoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final VersionRepository versionRepository;

    public ImageService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public String getImageFor(String manufacturer, String model, String year) {
        String result = "undefined.JPG";

        log.debug("Fetching photos for {} model!", model);

        List<String> results = Optional
            .ofNullable(versionRepository.findDoorsNumberAndBody(manufacturer, model, year))
            .orElseThrow(() -> new RuntimeException("No data found for constructing image names!"));

        if (!results.isEmpty()) {
            result = manufacturer + "/" + model + "/" + year + "/" + results.get(0);
        }

        List<String> _results = Optional
            .ofNullable(versionRepository.findDrivenWheels(manufacturer, model, year))
            .orElseThrow(() -> new RuntimeException("No data found for constructing image names!"));

        if (_results.size() == 1 && _results.get(0).equals("4")) {
            result = result + "-4";
        }

        log.debug("Returning photo {} for {} model!", result, model);

        return result;
    }

    public String getImageFor(String manufacturer, String model, Integer year) {
        return getImageFor(manufacturer, model, String.valueOf(year));
    }

    public String getImageFor(String manufacturer, String model, String year, Integer doorsNo, String body) {
        String result = manufacturer + "/" + model + "/" + year + "/" + doorsNo + JatoUtil.getBodyTypeCode(body);

        List<String> _results = Optional
            .ofNullable(versionRepository.findDrivenWheels(manufacturer, model, year))
            .orElseThrow(() -> new RuntimeException("No data found for constructing image names!"));

        if (_results.size() == 1 && _results.get(0).equals("4")) {
            result = result + "-4";
        }

        return result;
    }

    public String getImageFor(String manufacturer, String model, Integer year, Integer doorsNo, String body) {
        String result =  manufacturer + "/" + model + "/" + year + "/" + doorsNo + JatoUtil.getBodyTypeCode(body);

        List<String> _results = Optional
            .ofNullable(versionRepository.findDrivenWheels(manufacturer, model, year.toString()))
            .orElseThrow(() -> new RuntimeException("No data found for constructing image names!"));

        if (_results.size() == 1 && _results.get(0).equals("4")) {
            result = result + "-4";
        }

        return result;
    }

    public String getImageFor(VehicleDTO vehicleDTO) {
        String result =  vehicleDTO.getManufacturer() + "/" +
            vehicleDTO.getModel() + "/" +
            vehicleDTO.getModelYear() + "/" +
            vehicleDTO.getNoOfDoors() +
            JatoUtil.getBodyTypeCode(vehicleDTO.getBody());

        List<String> _results = Optional
            .ofNullable(versionRepository.findDrivenWheels(vehicleDTO.getManufacturer(), vehicleDTO.getModel(), vehicleDTO.getModelYear().toString()))
            .orElseThrow(() -> new RuntimeException("No data found for constructing image names!"));

        if (_results.size() == 1 && _results.get(0).equals("4")) {
            result = result + "-4";
        }

        return result;
    }
}
