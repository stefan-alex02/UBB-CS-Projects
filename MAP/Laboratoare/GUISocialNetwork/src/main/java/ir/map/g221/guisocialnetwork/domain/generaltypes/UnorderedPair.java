package ir.map.g221.guisocialnetwork.domain.generaltypes;

import java.util.Objects;

public class UnorderedPair<T1, T2> extends Pair<T1, T2> {
    public UnorderedPair(T1 first, T2 second) { super(first, second); }

    public static <T1, T2> UnorderedPair<T1, T2> of(T1 first, T2 second) {
        return new UnorderedPair<>(first, second);
    }

    /***
     * Creates an unordered pair with the given elements in ascending order.
     * @param elementA one of the 2 elements.
     * @param elementB one of the 2 elements.
     * @return the unordered pair.
     * @param <T> the type of BOTH arguments (must be comparable).
     */
    public static <T extends Comparable<T>> UnorderedPair<T, T> ofAscending(T elementA, T elementB) {
        return new UnorderedPair<>(
                elementA.compareTo(elementB) <= 0 ? elementA : elementB,
                elementA.compareTo(elementB) <= 0 ? elementB : elementA
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return (Objects.equals(first, pair.first) && Objects.equals(second, pair.second)) ||
                (Objects.equals(first, pair.second) && Objects.equals(second, pair.first));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first) + Objects.hash(second);
    }
}
