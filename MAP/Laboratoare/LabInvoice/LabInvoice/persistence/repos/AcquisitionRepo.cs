using LabInvoice.domain.entities;

namespace LabInvoice.persistence.repos;

public class AcquisitionRepo : IRepository<string, Acquisition>{

    public IEnumerable<Acquisition> FindAll() {
        throw new NotImplementedException();
    }
}