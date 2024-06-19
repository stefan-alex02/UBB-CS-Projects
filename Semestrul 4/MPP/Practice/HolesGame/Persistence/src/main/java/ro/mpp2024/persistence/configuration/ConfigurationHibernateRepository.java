package ro.mpp2024.persistence.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.persistence.HibernateUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ConfigurationHibernateRepository implements ConfigurationRepository {
    private static final Logger logger = LogManager.getLogger(ConfigurationHibernateRepository.class);


    @Override
    public Optional<Configuration> getByID(Configuration.ConfigurationId configurationId) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Collection<Configuration> getAll() {
        return List.of();
    }

    @Override
    public Optional<Configuration> add(Configuration entity) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> delete(Configuration.ConfigurationId configurationId) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> update(Configuration entity) throws IllegalArgumentException {
        return Optional.empty();
    }
}
