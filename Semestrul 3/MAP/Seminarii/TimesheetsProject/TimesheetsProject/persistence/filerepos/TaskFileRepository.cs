using TimesheetsProject.domain.enums;
using Task = TimesheetsProject.domain.Task;

namespace TimesheetsProject.persistence.filerepos;

public class TaskFileRepository : FileRepository<string, Task> {
    public TaskFileRepository(string fileName) : base(fileName) {
    }

    protected override string ToLine(Task elem) {
        return elem.Id + ',' + elem.Difficulty + ',' + elem.EstimatedHours;
    }

    protected override Task FromLine(string line) {
        var elems = line.Split(',');
        string id = elems[0];
        TaskDifficulty difficulty = Enum.Parse<TaskDifficulty>(elems[1]);
        int estimatedHours = int.Parse(elems[2]);
        
        return new Task(id, difficulty, estimatedHours);
    }
}