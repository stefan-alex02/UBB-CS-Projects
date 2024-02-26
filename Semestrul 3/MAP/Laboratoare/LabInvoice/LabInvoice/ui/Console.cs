using System.Collections;
using LabInvoice.business;

namespace LabInvoice.ui;

public class Console {
    private readonly DocumentService _documentService;
    private readonly InvoiceService _invoiceService;
    private readonly AcquisitionsService _acquisitionsService;

    public Console(DocumentService documentService, InvoiceService invoiceService, AcquisitionsService acquisitionsService) {
        _documentService = documentService;
        _invoiceService = invoiceService;
        _acquisitionsService = acquisitionsService;
    }

    private readonly Action<IEnumerable> _displayAll = enumerable => {
        foreach (var o in enumerable) {
            System.Console.WriteLine(o);
        }
    };

    private void ShowAllData() {
        System.Console.WriteLine("Document:");
        _displayAll(_documentService.GetAll());

        System.Console.WriteLine("Invoices:");
        _displayAll(_invoiceService.GetAll());

        System.Console.WriteLine("Acquisitions:");
        _displayAll(_acquisitionsService.GetAll());
    }

    private void DocsIssuedIn2023() {
        System.Console.WriteLine("Documents issued in 2023:");
        _displayAll(_documentService.DocumentsIssuedIn2023());
    }

    private void InvoicesDueToCurrentMonth() {
        System.Console.WriteLine("Invoices due to current month:");
        _displayAll(_invoiceService.InvoicesDueToCurrentMonth());
    }

    private void InvoicesWithAtLeast3Products() {
        System.Console.WriteLine("Invoices with at least 3 products:");
        _displayAll(_invoiceService.InvoicesWithAtLeast3Products());
    }

    private void AcquisitionsOfUtilitiesInvoices() {
        System.Console.WriteLine("Acquisitions of Utility invoices:");
        _displayAll(_acquisitionsService.AcquisitionsOfUtilitiesInvoices());
    }

    private void CategoryWithMostExpenses() {
        System.Console.WriteLine("Category with most expenses:");
        System.Console.WriteLine(_invoiceService.CategoryWithMostExpenses());
    }

    public void Run() {
        while (true) {
            System.Console.WriteLine();
            System.Console.WriteLine("================== OPTIONS =====================");
            System.Console.WriteLine("0. Exit");
            System.Console.WriteLine("1. Show all docs issued in 2023");
            System.Console.WriteLine("2. Show all invoices due to current month");
            System.Console.WriteLine("3. Show all invoices with at least 3 products");
            System.Console.WriteLine("4. Show all acquisitions from Utilities category");
            System.Console.WriteLine("5. Show most expensive invoice category");
            System.Console.WriteLine("6. Show all data");
            System.Console.WriteLine("================================================");

            switch (System.Console.ReadLine()) {
                case "0":
                case null:
                    return;
                
                case "1":
                    DocsIssuedIn2023();
                    break;
                
                case "2":
                    InvoicesDueToCurrentMonth();
                    break;
                
                case "3":
                    InvoicesWithAtLeast3Products();
                    break;
                
                case "4":
                    AcquisitionsOfUtilitiesInvoices();
                    break;
                
                case "5":
                    CategoryWithMostExpenses();
                    break;
                
                case "6":
                    ShowAllData();
                    break;
                
                default:
                    System.Console.WriteLine("Unknown command. Please try again.");
                    break;
            }
        }
    }
}