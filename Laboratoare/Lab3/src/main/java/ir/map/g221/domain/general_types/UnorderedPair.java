package ir.map.g221.domain.general_types;

import java.util.Objects;

public class UnorderedPair<T1, T2> extends Pair<T1, T2> {
    public UnorderedPair(T1 first, T2 second) {
        super(first, second);
    }

    public static <T1, T2> UnorderedPair<T1, T2> of(T1 first, T2 second) {
        return new UnorderedPair<>(first, second);
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
