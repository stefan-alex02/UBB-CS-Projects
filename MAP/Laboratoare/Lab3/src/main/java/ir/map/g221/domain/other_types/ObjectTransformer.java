package ir.map.g221.domain.other_types;

import ir.map.g221.domain.general_types.UnorderedPair;
import ir.map.g221.domain.graphs.Edge;
import ir.map.g221.domain.graphs.Node;

import java.util.Collection;

public class ObjectTransformer {
    public static <T extends Node<T>, TCol extends Collection> TCol unorderedPairToEdgeInCollection(
            Collection<UnorderedPair<T, T>> collection) {
        return (TCol) collection.stream().map(pair -> new Edge<>(pair));
    }
}
