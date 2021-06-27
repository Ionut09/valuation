package co.arbelos.gtm.valuation.service.observationmarket;

import co.arbelos.gtm.core.domain.marketobservation.MarketMasterType;
import co.arbelos.gtm.modules.valuation.engine.ValuationEngine;
import com.microsoft.schemas.office.visio.x2012.main.MasterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import java.util.List;

@Service
public class ObservationMarketService {

    private static final Logger log = LoggerFactory.getLogger(ObservationMarketService.class);

    private final Queue observationMarketQueue;
    private final Queue masterTypeQueue;
    private final JmsTemplate jmsTemplate;
    private boolean observationMarketLock = false;
    private boolean masterTypeLock = false;

    private final ValuationEngine valuationEngine;

    public ObservationMarketService(@Qualifier("inmemory.observationmarket") Queue observationMarketQueue,
                                    @Qualifier("inmemory.mastertype") Queue masterTypeQueue,
                                    JmsTemplate jmsTemplate,
                                    ValuationEngine valuationEngine) {
        this.observationMarketQueue = observationMarketQueue;
        this.masterTypeQueue = masterTypeQueue;
        this.jmsTemplate = jmsTemplate;
        this.valuationEngine = valuationEngine;
    }

    public void triggerObservationMarketCreation() {
        observationMarketLock = true;
        jmsTemplate.convertAndSend(observationMarketQueue, "");
    }

    public void triggerMasterTypeCreations() {
        masterTypeLock = true;
        jmsTemplate.convertAndSend(masterTypeQueue, "");
    }

    @JmsListener(destination = "inmemory.observationmarket")
    public void createMatchings(){
        log.info("Executing market observation data creations!");

        valuationEngine.createMatchings();

        observationMarketLock = false;
    }

    @JmsListener(destination = "inmemory.mastertype")
    public void createMasterTypes() {
        log.info("Executing master type data creations!");

        valuationEngine.createMasterTypes();

        masterTypeLock = false;
    }

    public boolean getObservationMarketLock() {
        return this.observationMarketLock;
    }
    public boolean getMasterTypeLock() {
        return this.masterTypeLock;
    }

    public List<MarketMasterType> getMasterTypes(String manufacturer, String model) {
        return valuationEngine.findMasterTypes(manufacturer, model);
    }
}
