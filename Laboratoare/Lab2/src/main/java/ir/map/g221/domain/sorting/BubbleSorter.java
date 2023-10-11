package ir.map.g221.domain.sorting;

import java.util.List;

public class BubbleSorter extends AbstractSorter{
    private static BubbleSorter instance = null;

    private BubbleSorter() {
    }

    public static BubbleSorter getInstance() {
        if (instance == null) {
            instance = new BubbleSorter();
        }

        return instance;
    }

    @Override
    public <T extends Comparable<T>> void sort(List<T> list) {
        int limit = list.size();
        boolean sorted;

        do {
            sorted = true;
            limit--;
            for (int i = 0; i < limit; i++) {
                if (list.get(i).compareTo(list.get(i+1)) > 0) {
                    T temp = list.get(i);
                    list.set(i, list.get(i+1));
                    list.set(i+1, temp);

                    sorted = false;
                }
            }
        } while(!sorted);
    }
}
