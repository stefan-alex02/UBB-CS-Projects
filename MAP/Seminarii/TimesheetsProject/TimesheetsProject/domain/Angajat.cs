using TimesheetsProject.domain.enums;

namespace TimesheetsProject.domain;

public class Angajat: HasId<string>
{
    public string Nume { get; set; }
        


    public double VenitPeOra { get; set; }

    public KnowledgeLevel Level { get; set; }

    public override string ToString()
    {
        return Id + " " + Nume + " " + this.Level + " " + this.VenitPeOra;
    }
        
}