package ir.map.g221.domain.generaltypes;

import java.util.ArrayList;
import java.util.Collection;

public class ObjectTransformer {
    public static <T> Collection<T> iterableToCollection(Iterable<T> iterables) {
        Collection<T> collection = new ArrayList<>();
        iterables.forEach(collection::add);
        return collection;
    }
}
