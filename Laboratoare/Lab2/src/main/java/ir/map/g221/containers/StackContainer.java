package ir.map.g221.containers;

import ir.map.g221.domain.Task;

import java.util.List;

public class StackContainer extends AbstractContainer {
    @Override
    public Task remove() {
        // TODO verify if the list is empty
        return list.remove(list.size() - 1);
    }
}
