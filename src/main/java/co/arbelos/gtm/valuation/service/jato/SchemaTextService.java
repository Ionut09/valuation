package co.arbelos.gtm.valuation.service.jato;

import co.arbelos.gtm.core.dao.jato.CarSegmentProjection;
import co.arbelos.gtm.core.repository.jato.SchemaTextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaTextService {

    private final Logger log = LoggerFactory.getLogger(SchemaTextService.class);

    private final SchemaTextRepository schemaTextRepository;

    public SchemaTextService(SchemaTextRepository schemaTextRepository) {
        this.schemaTextRepository = schemaTextRepository;
    }

    public List<CarSegmentProjection> getCarSegments() {
        return schemaTextRepository.findCarSegments();
    }

}
