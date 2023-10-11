package ir.map.g221.domain.sorting;

import java.util.List;

public class QuickSorter extends AbstractSorter {
    private static QuickSorter instance = null;

    private QuickSorter() {
    }

    public static QuickSorter getInstance() {
        if (instance == null) {
            instance = new QuickSorter();
        }

        return instance;
    }

    @Override
    public <T extends Comparable<T>> void sort(List<T> list) {

    }
}
