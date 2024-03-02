namespace LabInvoice.domain;

public class Entity<TId> {
    public TId Id { get; set; }

    protected Entity(TId id) {
        Id = id;
    }

    public override string ToString() {
        return "Id: " + Id;
    }
}