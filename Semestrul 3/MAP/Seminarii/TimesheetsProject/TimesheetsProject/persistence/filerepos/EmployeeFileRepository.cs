using TimesheetsProject.domain;
using TimesheetsProject.domain.enums;

namespace TimesheetsProject.persistence.filerepos;

public class EmployeeFileRepository : FileRepository<string, Employee> {
    public EmployeeFileRepository(string fileName) : base(fileName) {
    }

    protected override string ToLine(Employee elem) {
        return elem.Id + ',' + elem.Name + ',' + elem.HourlyIncome + ',' + elem.Level;
    }

    protected override Employee FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        string name = elems[1];
        double hourlyIncome = double.Parse(elems[2]);
        KnowledgeLevel level = Enum.Parse<KnowledgeLevel>(elems[3]);
        
        return new Employee(id, name, hourlyIncome, level);
    }
}