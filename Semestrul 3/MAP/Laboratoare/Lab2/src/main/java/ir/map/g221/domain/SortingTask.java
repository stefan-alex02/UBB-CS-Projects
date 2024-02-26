package ir.map.g221.domain;

import ir.map.g221.domain.sorting.AbstractSorter;

import java.util.List;

public class SortingTask extends Task {
    private List<Integer> list;
    private AbstractSorter sorter;

    public SortingTask(String taskID, String descriere, List<Integer> list, AbstractSorter sorter) {
        super(taskID, descriere);
        this.list = list;
        this.sorter = sorter;
    }

    @Override
    public void execute() {
        sorter.sort(list);
        System.out.println(list);
    }
}
