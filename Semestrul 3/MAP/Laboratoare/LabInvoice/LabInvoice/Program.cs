using LabInvoice.business;
using LabInvoice.domain.entities;
using LabInvoice.persistence;
using LabInvoice.persistence.fileentities;
using LabInvoice.persistence.filerepos;
using LabInvoice.persistence.repos;

FileRepository<string, FileDocument> documentFileRepo = 
    new DocumentFileRepo(@"..\..\..\data\documents.txt");
FileRepository<string, FileInvoice> invoiceFileRepo =
    new InvoiceFileRepo(@"..\..\..\data\invoices.txt");
FileRepository<string, FileAcquisition> acquisitionFileRepo =
    new AcquisitionFileRepo(@"..\..\..\data\acquisitions.txt");

IRepository<string, Document> documentRepo = 
    new DocumentRepo(documentFileRepo);
IRepository<string, Invoice> invoiceRepo = 
    new InvoiceRepo(documentFileRepo, invoiceFileRepo, acquisitionFileRepo);
IRepository<string, Acquisition> acquisitionRepo =
    new AcquisitionRepo(documentFileRepo, invoiceFileRepo, acquisitionFileRepo);

DocumentService documentService = new DocumentService(documentRepo);
InvoiceService invoiceService = new InvoiceService(invoiceRepo);
AcquisitionsService acquisitionsService = new AcquisitionsService(acquisitionRepo);

var console = new LabInvoice.ui.Console(documentService, invoiceService, acquisitionsService);

console.Run();