namespace TimesheetsProject.domain;

public abstract class HasId<TId> {
    public TId Id { get; set; }

    protected HasId(TId id) {
        Id = id;
    }
}