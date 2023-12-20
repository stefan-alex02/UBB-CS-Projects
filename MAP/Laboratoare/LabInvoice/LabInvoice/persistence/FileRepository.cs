using LabInvoice.domain;

namespace LabInvoice.persistence;

public abstract class FileRepository<TId, TE>: IRepository<TId, TE> where TE: Entity<TId> where TId : notnull {
    protected readonly string FileName;
    protected readonly IDictionary<TId, TE> Dictionary = new Dictionary<TId, TE>();
    
    protected FileRepository(string fileName) {
    }
    
    protected abstract TE FromLine(String line);

    private void ReadAll() {
        Dictionary.Clear();
        using StreamReader sr = new StreamReader(FileName);
        while (sr.ReadLine() is { } s) {
            TE entity = FromLine(s);
            Dictionary.TryAdd(entity.Id, entity);
        }
    }

    public IEnumerable<TE> FindAll() {
        ReadAll();
        return Dictionary.Values;
    }
}