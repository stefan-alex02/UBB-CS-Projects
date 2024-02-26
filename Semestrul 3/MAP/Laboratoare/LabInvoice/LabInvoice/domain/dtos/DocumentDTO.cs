namespace LabInvoice.domain.dtos;

public class DocumentDto {
    public string Name { get; }
    public DateTime IssueDate { get; }

    public DocumentDto(string name, DateTime issueDate) {
        Name = name;
        IssueDate = issueDate;
    }

    public override string ToString() {
        return "Name: " + Name + " | Issue date: " + IssueDate;
    }
}