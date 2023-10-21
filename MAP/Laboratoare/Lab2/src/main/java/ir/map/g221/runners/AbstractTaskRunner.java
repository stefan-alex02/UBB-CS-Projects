package ir.map.g221.runners;

import ir.map.g221.domain.Task;

public abstract class AbstractTaskRunner implements TaskRunner {
    private final TaskRunner runner;

    protected AbstractTaskRunner(TaskRunner runner) {
        this.runner = runner;
    }

    @Override
    public void executeOneTask() {
        runner.executeOneTask();
    }

    @Override
    public void executeAll() {
        while(hasTask()) {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        runner.addTask(t);
    }

    @Override
    public boolean hasTask() {
        return runner.hasTask();
    }
}
