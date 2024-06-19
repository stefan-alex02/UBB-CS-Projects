package ro.mpp2024.persistence;

import ro.mpp2024.domain.Entity;

import java.util.Collection;
import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <TE> type of entities saved in repository
 * @param <TId> type TE must have an attribute of type TID
 */
public interface Repository<TE extends Entity<TId>, TId> {
    /**
     * Gets the {@linkplain Entity} having the specified id.
     *
     * @param id the id of the entity to be returned
     *           id must not be {@code null}
     * @return an {@link Optional} encapsulating the entity with the given id,
     *          or empty if no user with the specified ID could be found.
     * @throws IllegalArgumentException if id is {@code null}.
     */
    Optional<TE> getByID(TId id) throws IllegalArgumentException;

    /**
     * Gets all the stored entities.
     *
     * @return a {@link Collection} of all entities
     */
    Collection<TE> getAll();

    /**
     * Adds an {@linkplain Entity}.
     *
     * @param entity entity must be not {@code null}
     * @return an {@linkplain Optional} - the entity (with generated id) if it was saved,
     * otherwise {@code null}
     * @throws IllegalArgumentException if the given entity is {@code null}. *
     */
    Optional<TE> add(TE entity) throws IllegalArgumentException;

    /**
     * Removes the {@linkplain Entity} having the specified id.
     *
     * @param id id must be not {@code null}
     * @return an {@linkplain Optional}
     * - null if there is no entity with the given id,
     * - the removed entity, otherwise
     * @throws IllegalArgumentException if the given id is {@code null}.
     */
    Optional<TE> delete(TId id) throws IllegalArgumentException;

    /**
     * Updates the {@linkplain Entity} having the specified id.
     *
     * @param entity entity must not be {@code null}
     * @return an {@linkplain Optional} with the new entity if it was updated,
     * otherwise {@code null} (e.g. id does not exist)
     * @throws IllegalArgumentException if the given entity is {@code null}.
     */
    Optional<TE> update(TE entity) throws IllegalArgumentException;
}
