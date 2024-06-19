package ro.mpp2024.persistence.configuration;

import ro.mpp2024.domain.Configuration;
import ro.mpp2024.persistence.Repository;

import java.util.Optional;

public interface ConfigurationRepository extends Repository<Configuration, Integer> {
    Optional<Configuration> getRandomConfiguration();
}
