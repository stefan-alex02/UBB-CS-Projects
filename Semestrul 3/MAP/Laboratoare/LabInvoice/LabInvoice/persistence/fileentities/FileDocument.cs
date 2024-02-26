using LabInvoice.domain;

namespace LabInvoice.persistence.fileentities;

public class FileDocument : Entity<string> {
    public string Name { get; set; }
    public DateTime IssueDate { get; set; }
    
    public FileDocument(string id, string name, DateTime issueDate) : base(id) {
        Name = name;
        IssueDate = issueDate;
    }
}