using LabInvoice.domain;

namespace LabInvoice.persistence.fileentities;

public class FileDocument : Entity<string>, IParsable<>{
    public FileDocument(string id) : base(id) {
    }
}