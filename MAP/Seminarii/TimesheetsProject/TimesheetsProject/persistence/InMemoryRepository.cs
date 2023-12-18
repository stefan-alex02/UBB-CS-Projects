using TimesheetsProject.domain;

namespace TimesheetsProject.persistence;

public class InMemoryRepository<TID, TE> : IRepository<TID, TE> where TE: HasId<TID>
{
    private IDictionary<TID, TE> _dictionary = new Dictionary<TID, TE>();
        
    public TE FindOne(TID id)
    {

        throw new NotImplementedException();
    }

    public IEnumerable<TE> FindAll() {
        return _dictionary.Values;
    }

    public TE Save(TE entity) {
        return _dictionary.TryAdd(entity.Id, entity) ?  default(TE) : entity;
    }

    public TE Delete(TID id)
    {
        throw new NotImplementedException();
    }

    public TE Update(TE entity)
    {
        throw new NotImplementedException();
    }
}