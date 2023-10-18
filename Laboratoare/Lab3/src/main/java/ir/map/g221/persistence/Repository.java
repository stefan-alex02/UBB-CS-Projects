package ir.map.g221.persistence;

import ir.map.g221.domain.Entity;
import ir.map.g221.exceptions.ValidationException;

import java.util.Collection;
import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    /**
     *
     * @param entity must be not null.
     * @return the saved entity, or null if an entity with the
     *          same id already exists.
     * @throws ValidationException
     *            if the entity is not valid.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     */
    E add(E entity) throws ValidationException, IllegalArgumentException;

    /**
     *
     * @param id the id of the entity to be returned
     *           id must not be null.
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id.
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E getById(ID id) throws IllegalArgumentException;

    /**
     *
     * @return all entities as an Iterable.
     */
    Collection<E> getAll();

    /**
     *
     * @return the number of all entities.
     */
    int getSize();

    /**
     *
     * @param entity must not be null.
     * @return the updated entity, or null if there is no entity with the given id.
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */
    E update(E entity) throws IllegalArgumentException, ValidationException;

    /**
     * Removes the entity with the specified id.
     * @param id must be not null.
     * @return the removed entity, or null if there is no entity with the given id.
     * @throws IllegalArgumentException if the given id is null.
     */
    E delete(ID id);
}
