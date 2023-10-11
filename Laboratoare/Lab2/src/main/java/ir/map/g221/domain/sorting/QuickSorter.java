package ir.map.g221.domain.sorting;

import java.util.Collection;
import java.util.Collections;
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

    private <T extends Comparable<T>> int partition(List<T> list, int low, int high) {
        int i, j, middle;
        T pivot = list.get(middle = (low + high) / 2);
        i = low;
        j = high - 1;
        Collections.swap(list, middle, high);
        while(i < j) {
            while(i < j && list.get(i).compareTo(pivot) <= 0) i++;
            while(i < j && list.get(j).compareTo(pivot) >= 0) j--;

            Collections.swap(list, i, j);
        }
        if (list.get(i).compareTo(pivot) > 0) {
            Collections.swap(list, high, i);
        }

        return i;
    }

    private <T extends Comparable<T>> void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int part = partition(list, low, high);

            quickSort(list, low, part);
            quickSort(list, part + 1, high);
        }
    }

    @Override
    public <T extends Comparable<T>> void sort(List<T> list) {
        quickSort(list, 0, list.size() - 1);
    }
}
