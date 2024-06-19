package ro.mpp2024.persistence.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.persistence.HibernateUtils;

import java.util.Collection;
import java.util.Optional;

@Component
public class ConfigurationHibernateRepository implements ConfigurationRepository {
    private static final Logger logger = LogManager.getLogger(ConfigurationHibernateRepository.class);

    @Override
    public Optional<Configuration> getByID(Integer integer) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Collection<Configuration> getAll() {
        logger.traceEntry();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            var result = session.createQuery("from Configuration ", Configuration.class)
                    .getResultList();

            logger.traceExit(result);
            return result;
        }
    }

    @Override
    public Optional<Configuration> add(Configuration entity) throws IllegalArgumentException {
        logger.traceEntry();

        HibernateUtils.getSessionFactory().inTransaction(s -> s.merge(entity));
        var result = Optional.of(entity);

        logger.traceExit(result);
        return result;
    }

    @Override
    public Optional<Configuration> delete(Integer integer) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Configuration> update(Configuration entity) throws IllegalArgumentException {
        logger.traceEntry();

        HibernateUtils.getSessionFactory().inTransaction(s -> s.merge(entity));
        var result = Optional.of(entity);

        logger.traceExit(result);
        return result;
    }

    @Override
    public Optional<Configuration> getRandomConfiguration() {
        logger.traceEntry();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            var result = session
                    .createQuery("from Configuration order by random()", Configuration.class)
                    .setMaxResults(1)
                    .getSingleResult();

            logger.traceExit(result);

            return Optional.of(result);
        } catch (Exception e) {
            logger.error("Error while choosing words", e);
            return Optional.empty();
        }
    }
}
