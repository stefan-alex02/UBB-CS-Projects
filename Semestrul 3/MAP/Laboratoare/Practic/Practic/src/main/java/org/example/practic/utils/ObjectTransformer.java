package org.example.practic.utils;

import java.util.*;

public class ObjectTransformer {
    public static <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<>();
        iterable.forEach(collection::add);
        return collection;
    }

    public static <T> Set<T> iterableToSet(Iterable<T> iterable) {
        Set<T> set = new HashSet<>();
        iterable.forEach(set::add);
        return set;
    }

    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
