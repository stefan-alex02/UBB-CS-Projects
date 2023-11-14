package ir.map.g221.seminar7_v3.domain.graphs;

import ir.map.g221.seminar7_v3.domain.generaltypes.UnorderedPair;

import java.util.HashSet;
import java.util.Set;

public class Edge<TNode extends Node<TNode>>{
    private final UnorderedPair<TNode, TNode> unorderedPair;

    public Edge(TNode first, TNode second) {
        unorderedPair = new UnorderedPair<>(first, second);
    }

    public Edge(UnorderedPair<TNode, TNode> unorderedPair) {
        this.unorderedPair = unorderedPair;
    }

    public static <T extends Node<T>> Edge<T> of(T first, T second) {
        return new Edge<>(first, second);
    }

    public TNode getFirst() {
        return unorderedPair.getFirst();
    }

    public TNode getSecond() {
        return unorderedPair.getSecond();
    }

    public Set<TNode> getNodes() {
        return new HashSet<>() {{
            add(getFirst());
            add(getSecond());
        }};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return unorderedPair.equals(edge.unorderedPair);
    }

    @Override
    public int hashCode() {
        return unorderedPair.hashCode();
    }
}
