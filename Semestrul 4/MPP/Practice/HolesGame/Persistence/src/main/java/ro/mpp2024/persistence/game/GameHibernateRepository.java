package ro.mpp2024.persistence.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.persistence.HibernateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class GameHibernateRepository implements GameRepository {
    private static final Logger logger = LogManager.getLogger(GameHibernateRepository.class);

    @Override
    public Optional<Game> getByID(Integer integer) throws IllegalArgumentException {
        logger.traceEntry();

        try(Session session= HibernateUtils.getSessionFactory().openSession()) {
            var result = Optional.ofNullable(
                    session.createQuery("from Game where id=:id", Game.class)
                            .setParameter("id", integer)
                            .getSingleResultOrNull()
            );

            logger.traceExit(result);
            return result;
        }
    }

    @Override
    public Collection<Game> getAll() {
        logger.traceEntry();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            var result = session.createQuery("from Game order by score, duration asc", Game.class)
                    .getResultList();

            logger.traceExit(result);
            return result;
        }
    }

    @Override
    public Optional<Game> add(Game entity) throws IllegalArgumentException {
        logger.traceEntry();

//        if (entity.getId() != null && entity.getId() == 0) {
//            entity.setId(null);
//        }
//        HibernateUtils.getSessionFactory().inTransaction(s -> s.merge(entity));

        Transaction tx = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            var tempPairs = entity.getPairs();
            var tempConfigs = entity.getConfigurations();

            entity.setPairs(new ArrayList<>());
            entity.setConfigurations(new ArrayList<>());

            Integer id = (Integer) session.save(entity);
            entity.setId(id);

            entity.setPairs(tempPairs);
            entity.setConfigurations(tempConfigs);

            for (Trial trial : entity.getPairs()) {
//                pair.setId(null);
                session.save(trial);
            }

            for (Configuration config : entity.getConfigurations()) {
                config.getId().setGameId(id);
                session.save(config);
            }

            tx.commit();
        } catch (HibernateException e) {
            logger.error("Error occurred", e);
            if (tx != null) tx.rollback();
            entity = null;
        }

        var result = Optional.of(entity);

        logger.traceExit(result);
        return result;
    }

    @Override
    public Optional<Game> delete(Integer integer) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) throws IllegalArgumentException {
        logger.traceEntry();

        HibernateUtils.getSessionFactory().inTransaction(s -> s.merge(entity));
        var result = Optional.of(entity);

        logger.traceExit(result);
        return result;
    }
}
