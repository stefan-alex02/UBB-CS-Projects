namespace LabInvoice.domain.dtos;

public class AcquisitionInvoiceDto {
    public string ProductName { get; }
    public string InvoiceName { get; }
    
    public AcquisitionInvoiceDto(string productName, string invoiceName) {
        ProductName = productName;
        InvoiceName = invoiceName;
    }

    public override string ToString() {
        return "Product name: " + ProductName + " | Invoice name: " + InvoiceName;
    }
}