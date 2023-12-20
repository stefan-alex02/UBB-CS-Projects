using LabInvoice.domain.entities;

namespace LabInvoice.persistence.filerepos;

public class DocumentFileRepository : FileRepository<string, Document> {
    public DocumentFileRepository(string fileName) : base(fileName) {
    }

    protected override Document FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        string name = elems[1];
        DateTime issueDate = DateTime.Parse(elems[2]);
        
        return new Document(id, name, issueDate);
    }
}