using LabInvoice.domain;
using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.filerepos;

public class InvoiceFileRepo : FileRepository<string, FileInvoice> {
    public InvoiceFileRepo(string fileName) : base(fileName) {
    }

    protected override FileInvoice FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        DateTime dueDate =  DateTime.Parse(elems[1]);
        Category category = Enum.Parse<Category>(elems[2]);
        
        return new FileInvoice(id, dueDate, category);
    }
}