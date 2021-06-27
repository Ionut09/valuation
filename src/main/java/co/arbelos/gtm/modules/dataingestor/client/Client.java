package co.arbelos.gtm.modules.dataingestor.client;

import co.arbelos.gtm.core.dto.jms.DataIngestDTO;

public interface Client {

    void ingest(DataIngestDTO dataIngestDTO);
}
