package co.arbelos.gtm.valuation.service.machinelearning;

import co.arbelos.gtm.core.domain.machinelearning.TrainResult;
import co.arbelos.gtm.core.repository.machinelearning.TrainResultRepository;
import co.arbelos.gtm.modules.valuation.tools.ValuationMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MachineLearningService {

    private final Logger log = LoggerFactory.getLogger(MachineLearningService.class);

    private final TrainResultRepository trainResultRepository;
    private final ValuationMLService valuationMLService;

    public MachineLearningService(TrainResultRepository trainResultRepository,
                                  ValuationMLService valuationMLService) {
        this.trainResultRepository = trainResultRepository;
        this.valuationMLService = valuationMLService;
    }

    public TrainResult getTrainingResult(String manufacturer, String model) {
        return trainResultRepository.findLastOne(manufacturer, model);
    }

    public Boolean getLocking() {
        return valuationMLService.getLock();
    }

    @Transactional
    public String triggerTraining() {
        trainResultRepository.truncateTable();

        return valuationMLService.triggerTraining();
    }
}
