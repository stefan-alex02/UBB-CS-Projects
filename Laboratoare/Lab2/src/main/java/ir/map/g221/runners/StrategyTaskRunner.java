package ir.map.g221.runners;

import ir.map.g221.containers.Container;
import ir.map.g221.containers.factory.ContainerStrategy;
import ir.map.g221.containers.factory.TaskContainerFactory;
import ir.map.g221.domain.Task;

public class StrategyTaskRunner implements TaskRunner{
    private final Container container;

    public StrategyTaskRunner(ContainerStrategy strategy) {
        container = TaskContainerFactory.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        if (hasTask()) {
            container.remove().execute();
        }
    }

    @Override
    public void executeAll() {
        while(hasTask()) {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
