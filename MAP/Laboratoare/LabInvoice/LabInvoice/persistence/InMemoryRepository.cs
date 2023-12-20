using LabInvoice.domain;

namespace LabInvoice.persistence;

public class InMemoryRepository<TId, TE> : IRepository<TId, TE> where TE: Entity<TId> where TId : notnull {
    protected readonly IDictionary<TId, TE> Dictionary = new Dictionary<TId, TE>();
    
    public IEnumerable<TE> FindAll() {
        return Dictionary.Values;
    }
}