using LabInvoice.domain.entities;
using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.repos;

public class InvoiceRepo : IRepository<string, Invoice> {
    private FileRepository<string, FileDocument> DocumentFileRepo { get; }
    private FileRepository<string, FileInvoice> InvoiceFileRepo { get; }
    private FileRepository<string, FileAcquisition> AcquisitionFileRepo { get; }

    public InvoiceRepo(FileRepository<string, FileDocument> documentFileRepo, 
        FileRepository<string, FileInvoice> invoiceFileRepo, FileRepository<string, FileAcquisition> acquisitionFileRepo) {
        DocumentFileRepo = documentFileRepo;
        InvoiceFileRepo = invoiceFileRepo;
        AcquisitionFileRepo = acquisitionFileRepo;
    }
    
    public IEnumerable<Invoice> FindAll() {
        return (from doc in DocumentFileRepo.FindAll()
            join inv in InvoiceFileRepo.FindAll() on doc.Id equals inv.Id
            select (doc, inv))
            .Select(arg => {
                Invoice rawInvoice = new Invoice(arg.doc.Id, arg.doc.Name, arg.doc.IssueDate, arg.inv.DueDate, 
                    null!, arg.inv.Category);
                
                rawInvoice.Acquisitions =
                    (from acq in AcquisitionFileRepo.FindAll()
                    select new Acquisition(acq.Id, acq.Product, acq.Quantity, acq.ProductPrice, rawInvoice)).ToList();
                
                return rawInvoice;
            });
    }
}