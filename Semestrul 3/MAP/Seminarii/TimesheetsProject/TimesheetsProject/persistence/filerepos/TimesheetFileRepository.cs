using TimesheetsProject.domain;

namespace TimesheetsProject.persistence.filerepos;

public class TimesheetFileRepository : FileRepository<Tuple<string, string>, Timesheet> {
    public TimesheetFileRepository(string fileName) : base(fileName) {
    }

    protected override string ToLine(Timesheet elem) {
        return elem.Id.Item1 + ',' + elem.Id.Item2 + ',' + elem.Date;
    }

    protected override Timesheet FromLine(string line) {
        var elems = line.Split(',');
        string employeeId = elems[0];
        string taskId = elems[1];
        DateTime date = DateTime.Parse(elems[2]);
        
        return new Timesheet(new Tuple<string, string>(employeeId, taskId), date);
    }
}