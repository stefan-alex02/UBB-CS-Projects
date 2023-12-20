using LabInvoice.domain;

namespace LabInvoice.persistence;

public abstract class FileRepository<TId, TE>: InMemoryRepository<TId, TE> where TE: Entity<TId> where TId : notnull {
    private readonly string _fileName;

    protected FileRepository(string fileName) {
        this._fileName = fileName;
        ReadAll();
    }
        
    protected abstract TE FromLine(String line);

    private void ReadAll() {
        using StreamReader sr = new StreamReader(_fileName);
        while (sr.ReadLine() is { } s) {
            TE entity = FromLine(s);
            Dictionary.TryAdd(entity.Id, entity);
        }
    }
}