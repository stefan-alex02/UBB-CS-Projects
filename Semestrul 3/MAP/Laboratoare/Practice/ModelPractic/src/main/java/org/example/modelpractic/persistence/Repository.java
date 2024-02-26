package org.example.modelpractic.persistence;

import org.example.modelpractic.domain.Entity;
import org.example.modelpractic.exceptions.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <ID> type E must have an attribute of type ID
 * @param <E> type of entities saved in repository
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * @param id the id of the entity to be returned
     *           id must not be null
     * @return an {@code Optional} encapsulating the entity with the given id,
     *          or empty if no user with the specified ID could be found.
     * @throws IllegalArgumentException if id is null.
     */
    Optional<E> findOne(ID id);

    /**
     * @return all entities.
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null. *
     */
    Optional<E> save(E entity);
}
