namespace sem11.domain;

public enum KnowledgeLevel
{
    Junior, Medium, Senior
}
    
public class Angajat: Entity<string>
{
    public string Nume { get; set; }
        


    public double VenitPeOra { get; set; }

    public KnowledgeLevel Level { get; set; }

    public override string ToString()
    {
        return Id + " " + Nume + " " + this.Level + " " + this.VenitPeOra;
    }
        
}