package ir.map.g221.containers;

import ir.map.g221.domain.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements Container{
    protected List<Task> list;

    public AbstractContainer() {
        list = new ArrayList<>();
    }
    @Override
    public abstract Task remove();

    @Override
    public void add(Task task) {
        list.add(task);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
