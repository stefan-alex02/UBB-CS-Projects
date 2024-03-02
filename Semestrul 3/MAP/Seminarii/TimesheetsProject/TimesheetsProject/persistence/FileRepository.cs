using TimesheetsProject.domain;

namespace TimesheetsProject.persistence;

public abstract class FileRepository<TId, TE>: InMemoryRepository<TId, TE> where TE: HasId<TId> where TId : notnull {
    private readonly string _fileName;

    protected FileRepository(string fileName) {
        this._fileName = fileName;
        ReadAll();
    }

    protected abstract string ToLine(TE elem);
        
    protected abstract TE FromLine(String line);

    private void ReadAll() {
        using (StreamReader sr = new StreamReader(_fileName)) {
            while (sr.ReadLine() is { } s) {
                TE entity = FromLine(s);
                base.Save(entity);
            }
        }
    }

    private void WriteAll() {
        using (StreamWriter sw = new StreamWriter(_fileName)) {
            foreach (var entity in base.FindAll()) {
                sw.WriteLine(ToLine(entity));
            }
        }
    }

    public override TE? Save(TE entity) {
        TE? retValue = base.Save(entity);
        if (retValue is not default(TE)) return retValue;
        
        WriteAll();
        return retValue;
    }

    public override TE? Delete(TId id) {
        TE? retValue = base.Delete(id);
        if (retValue is default(TE)) return retValue;
        
        WriteAll();
        return retValue;
    }

    public override TE Update(TE entity) {
        TE retValue = base.Update(entity);
        if (retValue == entity) return retValue;
        
        WriteAll();
        return retValue;
    }
}