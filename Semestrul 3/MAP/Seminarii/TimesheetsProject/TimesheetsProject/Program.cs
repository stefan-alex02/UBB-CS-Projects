using TimesheetsProject.domain;
using TimesheetsProject.domain.enums;
using TimesheetsProject.persistence;
using TimesheetsProject.persistence.filerepos;
using Task = TimesheetsProject.domain.Task;

IRepository<string, Employee> employeeRepo = new EmployeeFileRepository(@"..\..\..\files\employees.txt");
IRepository<string, Task> taskRepo = new TaskFileRepository(@"..\..\..\files\tasks.txt");
IRepository<Tuple<string, string>, Timesheet> timesheetRepo = 
    new TimesheetFileRepository(@"..\..\..\files\timesheets.txt");

List<Employee> employees = new List<Employee> {
    new Employee("gigi123", "Gigel", 12.6, KnowledgeLevel.Junior),
    new Employee("dandan", "Dan", 31.3, KnowledgeLevel.Medium)
};

// employees.ForEach(employee => employeeRepo.Save(employee));

foreach (var employee in employeeRepo.FindAll()) {
    Console.WriteLine(employee);
}


List<Task> tasks = new List<Task> {
    new Task("Proiect aplicatie Web in React", TaskDifficulty.Medium, 24),
    new Task("Motor cautare pentru magazin online", TaskDifficulty.Hard, 57)
};

// tasks.ForEach(task => tasksRepo.Save(task));
// var foundTask = taskRepo.FindOne("Proiect aplicatie Web");
// foundTask!.Id = "Proiect aplicatie Web in React";
// taskRepo.Update(foundTask);

foreach (var task in taskRepo.FindAll()) {
    Console.WriteLine(task);
}

List<Timesheet> timesheets = new List<Timesheet> {
    new Timesheet(employees[1], tasks[0], DateTime.Now),
    new Timesheet(employees[1], tasks[1], new DateTime(2023, 12, 17, 10, 00, 20)),
    new Timesheet(employees[0], tasks[1], new DateTime(2023, 12, 18, 9, 35, 40))
};

timesheets.ForEach(timesheet => {
    Timesheet? result = timesheetRepo.Save(timesheet);
});

foreach (var timesheet in timesheetRepo.FindAll()) {
    Console.WriteLine(timesheet);
}

timesheetRepo.Delete(timesheets[1].Id);

foreach (var timesheet in timesheetRepo.FindAll()) {
    Console.WriteLine(timesheet);
}