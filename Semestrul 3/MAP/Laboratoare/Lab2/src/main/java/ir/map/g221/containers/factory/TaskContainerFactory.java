package ir.map.g221.containers.factory;

import ir.map.g221.containers.Container;
import ir.map.g221.containers.QueueContainer;
import ir.map.g221.containers.StackContainer;

public class TaskContainerFactory implements Factory{
    private static TaskContainerFactory instance = null;

    private TaskContainerFactory() {
    }

    public static TaskContainerFactory getInstance() {
        if (instance == null) {
            instance = new TaskContainerFactory();
        }

        return instance;
    }
    @Override
    public Container createContainer(ContainerStrategy strategy) {
        return switch (strategy) {
            case LIFO -> new StackContainer();
            case FIFO -> new QueueContainer();
            default -> null;
        };
    }
}
