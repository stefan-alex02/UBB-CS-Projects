using TimesheetsProject.domain;

namespace TimesheetsProject.persistence;

interface IRepository<in TId, TE> where TE : HasId<TId> {
    /// <summary>
    /// Gets an entity from repository by given ID.
    /// </summary>
    /// <param name="id">The Id of the entity to be found</param>
    /// <returns>The entity if it exists, default value otherwise</returns>
    TE? FindOne(TId id);
    
    /// <summary>
    /// Gets all the entities in the repository.
    /// </summary>
    /// <returns>An IEnumerable of all entities</returns>
    IEnumerable<TE> FindAll();
    
    /// <summary>
    /// Saves a given entity to the repository.
    /// </summary>
    /// <param name="entity">The entity to save</param>
    /// <returns>Default value if the entity was saved with success, the entity if otherwise</returns>
    TE? Save(TE entity);
    
    /// <summary>
    /// Attempts to delete an entity by given ID. 
    /// </summary>
    /// <param name="id">The ID of the entity to be deleted</param>
    /// <returns>The entity if it was deleted with success, default value otherwise</returns>
    TE? Delete(TId id);
    
    /// <summary>
    /// Attempts to update an entity having the ID of the new entity.
    /// </summary>
    /// <param name="entity">The new entity that is to replace the existing one</param>
    /// <returns>The old entity if it was updated with success,
    /// otherwise returns the given entity</returns>
    TE Update(TE entity);
}