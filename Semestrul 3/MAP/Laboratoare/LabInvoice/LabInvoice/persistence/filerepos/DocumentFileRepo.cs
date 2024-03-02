using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.filerepos;

public class DocumentFileRepo : FileRepository<string, FileDocument> {
    public DocumentFileRepo(string fileName) : base(fileName) {
    }

    protected override FileDocument FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        string name = elems[1];
        DateTime issueDate = DateTime.Parse(elems[2]);
        
        return new FileDocument(id, name, issueDate);
    }
}