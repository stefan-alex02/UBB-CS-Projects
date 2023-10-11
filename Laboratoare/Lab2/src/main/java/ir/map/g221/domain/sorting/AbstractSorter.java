package ir.map.g221.domain.sorting;

import java.util.List;

public abstract class AbstractSorter {
    public abstract <T  extends Comparable<T>> void sort(List<T> list);
}
