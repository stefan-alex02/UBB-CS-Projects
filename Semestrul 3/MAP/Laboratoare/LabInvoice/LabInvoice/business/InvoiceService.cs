using LabInvoice.domain;
using LabInvoice.domain.dtos;
using LabInvoice.domain.entities;
using LabInvoice.persistence;

namespace LabInvoice.business;

public class InvoiceService {
    private readonly IRepository<string, Invoice> _invoiceRepository;

    public InvoiceService(IRepository<string, Invoice> invoiceRepository) {
        this._invoiceRepository = invoiceRepository;
    }
    
    public IEnumerable<Invoice> GetAll() {
        return _invoiceRepository.FindAll();
    }

    public IEnumerable<InvoiceDueDto> InvoicesDueToCurrentMonth() =>
        from invoice in _invoiceRepository.FindAll()
        where invoice.DueDate.Month == DateTime.Now.Month && invoice.DueDate.Year == DateTime.Now.Year
        select new InvoiceDueDto(invoice.Name, invoice.DueDate);

    public IEnumerable<InvoiceProductDto> InvoicesWithAtLeast3Products() =>
        _invoiceRepository.FindAll()
            .Select(invoice => new InvoiceProductDto(
                invoice.Name,
                invoice.Acquisitions.Sum(acquisition => acquisition.Quantity)))
            .Where(dto => dto.ProductCount >= 3);

    public Category CategoryWithMostExpenses() =>
        Enum.GetValues<Category>()
            .MaxBy(category => _invoiceRepository
                .FindAll()
                .Where(invoice => invoice.Category == category)
                .Sum(invoice => invoice.Acquisitions
                    .Sum(acquisition => acquisition.ProductPrice * acquisition.Quantity)
                )
            );
}