package co.arbelos.gtm.core.repository.configuration;

import co.arbelos.gtm.core.domain.configuration.AppConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigurationRepository extends JpaRepository<AppConfiguration, Long> {
}
