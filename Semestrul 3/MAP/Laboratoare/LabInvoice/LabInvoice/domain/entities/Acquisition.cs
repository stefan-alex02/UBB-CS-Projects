namespace LabInvoice.domain.entities;

public class Acquisition : Entity<string> {
    public string Product { get; set; }
    public int Quantity { get; set; }
    public double ProductPrice { get; set; }
    public Invoice Invoice { get; set; }
    
    public Acquisition(string id, string product, int quantity, double productPrice, 
        Invoice invoice) : base(id) {
        Product = product;
        Quantity = quantity;
        ProductPrice = productPrice;
        Invoice = invoice;
    }

    public override string ToString() {
        return base.ToString() + " | Product name: " + Product +
               " | Quantity: " + Quantity +
               " | Price: " + ProductPrice +
               " | Invoice Id: " + Invoice.Id;
    }
}