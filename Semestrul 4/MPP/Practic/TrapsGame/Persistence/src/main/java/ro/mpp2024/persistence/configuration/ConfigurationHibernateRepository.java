package ro.mpp2024.persistence.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.HibernateUtils;

import java.io.ObjectInputFilter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class ConfigurationHibernateRepository implements ConfigurationRepository {
    private static final Logger logger = LogManager.getLogger(ConfigurationHibernateRepository.class);


    @Override
    public Optional<Configuration> getByID(Configuration.ConfigurationId configurationId) throws IllegalArgumentException {
        logger.traceEntry();

        try(Session session= HibernateUtils.getSessionFactory().openSession()) {
            var result = Optional.ofNullable(
                    session.createQuery("from Configuration where id=:id", Configuration.class)
                            .setParameter("id", configurationId)
                            .getSingleResultOrNull()
            );

            logger.traceExit(result);
            return result;
        }
    }

    @Override
    public Collection<Configuration> getAll() {
        return List.of();
    }

    @Override
    public Optional<Configuration> add(Configuration entity) throws IllegalArgumentException {
        logger.traceEntry();

        entity.getGame().getConfigurations().add(entity);

//        HibernateUtils.getSessionFactory().inTransaction(s -> s.merge(entity));

        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            var id = session.save(entity);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            entity = null;
        }

        return Optional.of(entity);
    }

    @Override
    public Optional<Configuration> delete(Configuration.ConfigurationId configurationId) throws IllegalArgumentException {
        logger.traceEntry();

        Optional<Configuration> entity = getByID(configurationId);
        entity.ifPresent(t -> HibernateUtils.getSessionFactory().inTransaction(s -> s.remove(t)));


        return entity;
    }

    @Override
    public Optional<Configuration> update(Configuration entity) throws IllegalArgumentException {
        return Optional.empty();
    }
}
