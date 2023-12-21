namespace LabInvoice.domain.dtos;

public class InvoiceDueDto {
    public string Name { get; }
    public DateTime DueDate { get; }
    
    public InvoiceDueDto(string name, DateTime dueDate) {
        Name = name;
        DueDate = dueDate;
    }

    public override string ToString() {
        return "Name: " + Name + " | Due date: " + DueDate;
    }
}