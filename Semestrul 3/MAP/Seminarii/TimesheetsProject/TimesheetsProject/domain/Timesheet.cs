namespace TimesheetsProject.domain;

public class Timesheet : HasId<Tuple<string, string>> {
    public Employee? Employee { get; set; }
    public Task? Task { get; set; }
    public DateTime Date { get; set; }
    
    public Timesheet(Employee employee, Task task, DateTime date) : base(new Tuple<string, string>(employee.Id, task.Id)) {
        Employee = employee;
        Task = task;
        Date = date;
    }
    
    public Timesheet(Tuple<string, string> id, DateTime date) : base(id) {
        Date = date;
    }

    public override string ToString() {
        return this.Id.ToString() + ' ' + this.Date;
    }
}