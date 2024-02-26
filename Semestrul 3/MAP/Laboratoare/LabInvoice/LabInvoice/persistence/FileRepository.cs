using LabInvoice.domain;

namespace LabInvoice.persistence;

public abstract class FileRepository<TId, TE>: IRepository<TId, TE> where TE: Entity<TId> where TId : notnull {
    private readonly string _fileName;
    private readonly IDictionary<TId, TE> _dictionary = new Dictionary<TId, TE>();
    
    protected FileRepository(string fileName) {
        _fileName = fileName;
    }
    
    protected abstract TE FromLine(String line);

    private void ReadAll() {
        _dictionary.Clear();
        using StreamReader sr = new StreamReader(_fileName);
        while (sr.ReadLine() is { } s) {
            TE entity = FromLine(s);
            _dictionary.TryAdd(entity.Id, entity);
        }
    }

    public IEnumerable<TE> FindAll() {
        ReadAll();
        return _dictionary.Values;
    }
}