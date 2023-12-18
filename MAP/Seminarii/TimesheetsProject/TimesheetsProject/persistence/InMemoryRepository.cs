using TimesheetsProject.domain;

namespace TimesheetsProject.persistence;

public class InMemoryRepository<TId, TE> : IRepository<TId, TE> where TE: HasId<TId> where TId : notnull {
    private readonly IDictionary<TId, TE> _dictionary = new Dictionary<TId, TE>();
    
    public TE? FindOne(TId id) {
        try {
            return _dictionary[id];
        }
        catch (KeyNotFoundException) {
            return null;
        }
    }

    public IEnumerable<TE> FindAll() {
        return _dictionary.Values;
    }

    public virtual TE? Save(TE entity) {
        return _dictionary.TryAdd(entity.Id, entity) ?  default : entity;
    }

    public virtual TE? Delete(TId id) {
        TE? oldEntity = FindOne(id);
        
        if (oldEntity is null) return default;
        
        _dictionary.Remove(id);
        return oldEntity;
    }

    public virtual TE Update(TE entity) {
        TE? oldEntity = FindOne(entity.Id);
        
        if (oldEntity is null) return entity;

        _dictionary[oldEntity.Id] = entity;
        return oldEntity;
    }
}