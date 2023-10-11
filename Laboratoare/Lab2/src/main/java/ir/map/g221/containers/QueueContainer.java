package ir.map.g221.containers;

import ir.map.g221.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class QueueContainer extends AbstractContainer {
    @Override
    public Task remove() {
        // TODO test if the list is empty
        return list.remove(0);
    }
}
