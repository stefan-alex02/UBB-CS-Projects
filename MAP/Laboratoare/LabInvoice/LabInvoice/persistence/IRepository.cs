using LabInvoice.domain;

namespace LabInvoice.persistence;

interface IRepository<in TId, TE> where TE : Entity<TId> {
    /// <summary>
    /// Gets all the entities in the repository.
    /// </summary>
    /// <returns>An IEnumerable of all entities</returns>
    public IEnumerable<TE> FindAll();
}