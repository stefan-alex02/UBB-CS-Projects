using LabInvoice.domain;

namespace LabInvoice.persistence.fileentities;

public class FileAcquisition : Entity<string> {
    public string Product { get; set; }
    public int Quantity { get; set; }
    public double ProductPrice { get; set; }
    public string DocumentId { get; set; }

    public FileAcquisition(string id, string product, int quantity, double productPrice, string documentId) : base(id) {
        Product = product;
        Quantity = quantity;
        ProductPrice = productPrice;
        DocumentId = documentId;
    }
}