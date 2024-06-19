package ro.mpp2024.persistence.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.HibernateUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserHibernateRepository implements UserRepository {
    private static final Logger logger = LogManager.getLogger(UserHibernateRepository.class);

    @Override
    public Optional<User> getByID(Integer integer) throws IllegalArgumentException {
        logger.traceEntry();

        try(Session session= HibernateUtils.getSessionFactory().openSession()) {
            var result = Optional.ofNullable(
                    session.createQuery("from User where id=:id", User.class)
                            .setParameter("id", integer)
                            .getSingleResultOrNull()
            );

            logger.traceExit(result);
            return result;
        }
    }

    @Override
    public Collection<User> getAll() {
        return List.of();
    }

    @Override
    public Optional<User> add(User entity) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Integer integer) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByAlias(String alias) {
        logger.traceEntry();

        try(Session session= HibernateUtils.getSessionFactory().openSession()) {
            var result = Optional.ofNullable(
                    session.createQuery("from User where alias=:alias", User.class)
                            .setParameter("alias", alias)
                            .getSingleResultOrNull()
            );

            logger.traceExit(result);
            return result;
        }
    }
}
