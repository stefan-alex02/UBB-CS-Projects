using LabInvoice.domain.entities;
using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.repos;

public class AcquisitionRepo : IRepository<string, Acquisition>{
    private FileRepository<string, FileDocument> DocumentFileRepo { get; }
    private FileRepository<string, FileInvoice> InvoiceFileRepo { get; }
    private FileRepository<string, FileAcquisition> AcquisitionFileRepo { get; }

    public AcquisitionRepo(FileRepository<string, FileDocument> documentFileRepo, FileRepository<string, FileInvoice> invoiceFileRepo, FileRepository<string, FileAcquisition> acquisitionFileRepo) {
        DocumentFileRepo = documentFileRepo;
        InvoiceFileRepo = invoiceFileRepo;
        AcquisitionFileRepo = acquisitionFileRepo;
    }

    public IEnumerable<Acquisition> FindAll() {
        IDictionary<string, Invoice> invoices = new Dictionary<string, Invoice>();

        return AcquisitionFileRepo.FindAll()
            .Join(DocumentFileRepo.FindAll(),
                acquisition => acquisition.DocumentId,
                document => document.Id, (acquisition, document) => (acquisition, document))
            .Join(InvoiceFileRepo.FindAll(),
                tuple => tuple.document.Id,
                invoice => invoice.Id, (tuple, invoice) => (tuple.acquisition, tuple.document, invoice))
            .Select(tuple => {
                Acquisition acquisition = new Acquisition(
                    tuple.acquisition.Id,
                    tuple.acquisition.Product,
                    tuple.acquisition.Quantity,
                    tuple.acquisition.ProductPrice,
                    null!);

                if (!invoices.TryGetValue(tuple.acquisition.DocumentId, out Invoice? invoice)) {
                    invoice = new Invoice(tuple.document.Id,
                            tuple.document.Name,
                            tuple.document.IssueDate,
                            tuple.invoice.DueDate,
                            new List<Acquisition>(),
                            tuple.invoice.Category);
                    invoices.Add(invoice.Id, invoice);
                }

                invoice.Acquisitions.Add(acquisition);
                acquisition.Invoice = invoice;

                return acquisition;
            });
    }
}