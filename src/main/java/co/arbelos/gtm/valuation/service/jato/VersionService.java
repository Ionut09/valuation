package co.arbelos.gtm.valuation.service.jato;

import co.arbelos.gtm.core.repository.jato.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VersionService {

    private final Logger log = LoggerFactory.getLogger(VersionService.class);

    private final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public List<String> getManufacturers() {
        return versionRepository.findDistinctManufacturers();
    }

    public List<String> getModels(String manufacturer) {
        return versionRepository.findDistinctModels(manufacturer);
    }

}
