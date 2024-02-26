namespace LabInvoice.domain.entities;

public class Document : Entity<string>{
    public string Name { get; set; }
    public DateTime IssueDate { get; set; }
    
    public Document(string id, string name, DateTime issueDate) : base(id) {
        Name = name;
        IssueDate = issueDate;
    }

    public override string ToString() {
        return base.ToString() + " | Name: " + Name +
               " | Issue date: " + IssueDate;
    }
}