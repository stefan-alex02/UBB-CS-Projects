using TimesheetsProject.domain.enums;

namespace TimesheetsProject.domain;

public class Task : HasId<string> {
    public TaskDifficulty Difficulty { get; set; }
    public int EstimatedHours { get; set; }

    public Task(string id, TaskDifficulty difficulty, int estimatedHours) : base(id) {
        Difficulty = difficulty;
        EstimatedHours = estimatedHours;
    }

    public override string ToString() {
        return this.Id + ' ' + this.Difficulty + ' ' + this.EstimatedHours;
    }
}