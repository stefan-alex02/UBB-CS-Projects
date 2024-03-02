using LabInvoice.persistence.fileentities;

namespace LabInvoice.persistence.filerepos;

public class AcquisitionFileRepo : FileRepository<string, FileAcquisition> {
    public AcquisitionFileRepo(string fileName) : base(fileName) {
    }

    protected override FileAcquisition FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        string product = elems[1];
        int quantity = Int32.Parse(elems[2]);
        double price = Double.Parse(elems[3]);
        string documentId = elems[4];
        
        return new FileAcquisition(id, product, quantity, price, documentId);
    }
}