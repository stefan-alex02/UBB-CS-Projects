using LabInvoice.domain.dtos;
using LabInvoice.domain.entities;
using LabInvoice.persistence;

namespace LabInvoice.business;

public class DocumentService {
    private readonly IRepository<string, Document> _documentService;

    public DocumentService(IRepository<string, Document> documentService) {
        this._documentService = documentService;
    }
    
    public IEnumerable<Document> GetAll() {
        return _documentService.FindAll();
    }

    public IEnumerable<DocumentDto> DocumentsIssuedIn2023() =>
        from document in _documentService.FindAll()
        where document.IssueDate.Year == 2023
        select new DocumentDto(document.Name, document.IssueDate);
}