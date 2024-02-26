using LabInvoice.domain.entities;
using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.repos;

public class DocumentRepo : IRepository<string, Document> {
    private FileRepository<string, FileDocument> DocumentFileRepo { get; }

    public DocumentRepo(FileRepository<string, FileDocument> documentFileRepo) {
        DocumentFileRepo = documentFileRepo;
    }

    public IEnumerable<Document> FindAll() 
        => from doc in DocumentFileRepo.FindAll()
            select new Document(doc.Id, doc.Name, doc.IssueDate);
}