using TimesheetsProject.domain.enums;

namespace TimesheetsProject.domain;

public class Employee: HasId<string> {
    public string Name { get; set; }
    public double HourlyIncome { get; set; }
    public KnowledgeLevel Level { get; set; }

    public Employee(string id, string name, double hourlyIncome, KnowledgeLevel level) : base(id) {
        Name = name;
        HourlyIncome = hourlyIncome;
        Level = level;
    }

    public override string ToString() {
        return Id + " " + Name + " " + this.Level + " " + this.HourlyIncome;
    }
}