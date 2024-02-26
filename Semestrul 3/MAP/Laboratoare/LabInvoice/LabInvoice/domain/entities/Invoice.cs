namespace LabInvoice.domain.entities;

public class Invoice : Document {
    public DateTime DueDate { get; set; }
    public List<Acquisition> Acquisitions { get; set; }
    public Category Category { get; set; }
    
    public Invoice(string id, string name, DateTime issueDate, DateTime dueDate, 
        List<Acquisition> acquisitions, 
        Category category) : base(id, name, issueDate) {
        DueDate = dueDate;
        Acquisitions = acquisitions;
        Category = category;
    }

    public override string ToString() {
        return base.ToString() + " | Due date: " + DueDate +
               " | Acquisitions: [\n" + 
               String.Join('\n', Acquisitions.Select(acq => acq.ToString())) + "\n]" +
               " | Category: " + Category;
    }
}