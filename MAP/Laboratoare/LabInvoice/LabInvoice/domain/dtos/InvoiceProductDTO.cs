namespace LabInvoice.domain.dtos;

public class InvoiceProductDto {
    public string Name { get; }
    public int ProductCount { get; }
    
    public InvoiceProductDto(string name, int productCount) {
        Name = name;
        ProductCount = productCount;
    }

    public override string ToString() {
        return "Name: " + Name + " | Product count: " + ProductCount;
    }
}
