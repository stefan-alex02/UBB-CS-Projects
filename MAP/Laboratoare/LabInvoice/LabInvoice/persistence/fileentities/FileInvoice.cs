using LabInvoice.domain;

namespace LabInvoice.persistence.fileentities;

/// <summary>
/// This class has only a part of the fields of the logical Invoice Entity.
/// (Only those fields that are stored in invoices file)
/// </summary>
public class FileInvoice : Entity<string> {
    public DateTime DueDate { get; set; }
    public Category Category { get; set; }

    public FileInvoice(string id, DateTime dueDate, Category category) : base(id) {
        DueDate = dueDate;
        Category = category;
    }
}