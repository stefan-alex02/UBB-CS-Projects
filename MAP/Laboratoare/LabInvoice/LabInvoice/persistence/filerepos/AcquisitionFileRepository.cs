using LabInvoice.domain.entities;

namespace LabInvoice.persistence.filerepos;

public class AcquisitionFileRepository : FileRepository<string, Acquisition>{
    public AcquisitionFileRepository(string fileName, string invoiceFileName, 
        string documentFileName) : base(fileName) {
    }

    protected override Acquisition FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        string name = elems[1];
        DateTime issueDate = DateTime.Parse(elems[2]);
        
        Acquisition acquisition = new Acquisition(id, name, issueDate);
    }
}