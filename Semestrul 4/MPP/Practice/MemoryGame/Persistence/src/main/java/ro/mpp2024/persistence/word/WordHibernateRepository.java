package ro.mpp2024.persistence.word;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Order;
import org.hibernate.query.Query;
import ro.mpp2024.domain.Word;
import ro.mpp2024.persistence.HibernateUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WordHibernateRepository implements WordRepository {
    private static final Logger logger = LogManager.getLogger(WordHibernateRepository.class);

    @Override
    public Collection<Word> chooseWords(int n) {
        logger.traceEntry();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            var result = session
                    .createQuery("from Word order by random()", Word.class)
                    .setMaxResults(n)
                    .getResultList();

            logger.traceExit(result);

            return result;
        } catch (Exception e) {
            logger.error("Error while choosing words", e);
            return List.of();
        }
    }

    @Override
    public Optional<Word> getByID(String s) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Collection<Word> getAll() {
        return List.of();
    }

    @Override
    public Optional<Word> add(Word entity) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Word> delete(String s) throws IllegalArgumentException {
        return Optional.empty();
    }

    @Override
    public Optional<Word> update(Word entity) throws IllegalArgumentException {
        return Optional.empty();
    }
}
