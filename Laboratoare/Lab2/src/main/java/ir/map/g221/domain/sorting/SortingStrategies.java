package ir.map.g221.domain.sorting;

import java.util.Hashtable;
import java.util.Map;

public enum SortingStrategies {
    BUBBLE_SORT(BubbleSorter.getInstance()),
    QUICK_SORT(QuickSorter.getInstance());

    private final AbstractSorter sorter;

    SortingStrategies(AbstractSorter sorter) {
        this.sorter = sorter;
    }

    public AbstractSorter getSorter() {
        return sorter;
    }
}
