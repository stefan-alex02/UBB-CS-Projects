package ir.map.g221.guisocialnetwork.persistence;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;

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
     * Creates an entity from given result set.
     * @param resultSet the result set, containing data
     * @return the newly created entity
     * @throws SQLException if the entity could not be created from given result set
     */
    E createEntityFrom(ResultSet resultSet) throws SQLException;

    /**
     * Gets the table name corresponding to the entity represented by the repository.
     * @return the table name
     */
    String getTableName();

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
     * @return the number of all entities.
     */
    Integer getSize();

    /**
     * @param entity entity must be not null
     * @return an {@code Optional} - null if the entity was saved,
     * - the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null. *
     */
    Optional<E> save(E entity);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return an {@code Optional}
     * - null if there is no entity with the given id,
     * - the removed entity, otherwise
     * @throws IllegalArgumentException if the given id is null.
     */
    Optional<E> delete(ID id);

    /**
     * @param entity entity must not be null
     * @return an {@code Optional}
     * - null if the entity was updated
     * - otherwise (e.g. id does not exist) returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    Optional<E> update(E entity);
}
