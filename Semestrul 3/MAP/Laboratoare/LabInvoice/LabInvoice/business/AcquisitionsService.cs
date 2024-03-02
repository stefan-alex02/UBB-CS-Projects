using LabInvoice.domain;
using LabInvoice.domain.dtos;
using LabInvoice.domain.entities;
using LabInvoice.persistence;

namespace LabInvoice.business;

public class AcquisitionsService {
    private readonly IRepository<string, Acquisition> _acquisitionRepository;

    public IEnumerable<Acquisition> GetAll() {
        return _acquisitionRepository.FindAll();
    }

    public AcquisitionsService(IRepository<string, Acquisition> acquisitionRepository) {
        this._acquisitionRepository = acquisitionRepository;
    }

    public IEnumerable<AcquisitionInvoiceDto> AcquisitionsOfUtilitiesInvoices() =>
        from acquisition in _acquisitionRepository.FindAll()
        where acquisition.Invoice.Category == Category.Utilities
        select new AcquisitionInvoiceDto(acquisition.Product, acquisition.Invoice.Name);
}