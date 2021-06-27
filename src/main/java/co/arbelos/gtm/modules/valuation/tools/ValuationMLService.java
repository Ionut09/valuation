package co.arbelos.gtm.modules.valuation.tools;

import co.arbelos.gtm.core.dao.valuation.PredictionVersionProjection;
import co.arbelos.gtm.core.domain.jato.Version;
import co.arbelos.gtm.core.domain.jato.germandb.VersionDE;
import co.arbelos.gtm.core.dto.web.valuation.EstimateCarDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ValuationMLService {

    private final Logger log = LoggerFactory.getLogger(ValuationMLService.class);

    @Value("${valuation.ml.server}")
    private String ML_SERVICE_URL;

    public Float estimateValue(Version carVersion, Integer kmNo) {
        EstimateCarDTO dto = new EstimateCarDTO();
        dto.setManufacturer(carVersion.getVehicleManufacturerName());
        dto.setModel(carVersion.getModelName());
        dto.setEngine_hp(carVersion.getMaximumPowerHP());
        dto.setManufacturer_year(Integer.valueOf(carVersion.getModelYear()));
        dto.setKm_no(kmNo);
        dto.setTransmission_type(carVersion.getTransmissionType());
        dto.setFuel_type(carVersion.getFuelType());

        log.debug("Trying to predict: {}", dto);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EstimateCarDTO> request = new HttpEntity<>(dto);

        return restTemplate.postForObject(ML_SERVICE_URL + "/api/predict", request, Float.class);
    }

    public Float estimateValueDE(VersionDE carVersion, Integer kmNo) {
        EstimateCarDTO dto = new EstimateCarDTO();
        dto.setManufacturer(carVersion.getVehicleManufacturerName());
        dto.setModel(carVersion.getModelName());
        dto.setEngine_hp(carVersion.getMaximumPowerHP());
        dto.setManufacturer_year(Integer.valueOf(carVersion.getModelYear()));
        dto.setKm_no(kmNo);
        dto.setTransmission_type(carVersion.getTransmissionType());
        dto.setFuel_type(carVersion.getFuelType());

        log.debug("Trying to predict: {}", dto);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EstimateCarDTO> request = new HttpEntity<>(dto);

        return restTemplate.postForObject(ML_SERVICE_URL + "/api/predict", request, Float.class);
    }

    public Float estimateValue(PredictionVersionProjection predictionVersionProjection, Integer kmNo) {
        EstimateCarDTO dto = new EstimateCarDTO();
        dto.setManufacturer(predictionVersionProjection.getMake());
        dto.setModel(predictionVersionProjection.getModel());
        dto.setEngine_hp(predictionVersionProjection.getHp());
        dto.setManufacturer_year(predictionVersionProjection.getYear());
        dto.setKm_no(kmNo);
        dto.setTransmission_type(predictionVersionProjection.getTransmissionType());
        dto.setFuel_type(predictionVersionProjection.getFuelType());

        log.debug("Trying to predict: {}", dto);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<EstimateCarDTO> request = new HttpEntity<>(dto);

        return restTemplate.postForObject(ML_SERVICE_URL + "/api/predict", request, Float.class);
    }

    public String triggerTraining() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(ML_SERVICE_URL + "/api/train", null, String.class);
    }

    public Boolean getLock() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(ML_SERVICE_URL + "/api/lock", Boolean.class);
    }

}
