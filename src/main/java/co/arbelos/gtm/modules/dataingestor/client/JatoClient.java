package co.arbelos.gtm.modules.dataingestor.client;

import co.arbelos.gtm.core.dto.jms.DataIngestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JatoClient implements Client {
    private static final Logger log = LoggerFactory.getLogger(JatoClient.class);

    @Override
    public void ingest(DataIngestDTO dataIngestDTO) {
        log.info("Trying to ingest JatoDB");

        // get data from FTP

        // move data to /data/jatodb

        // update database
    }
}

