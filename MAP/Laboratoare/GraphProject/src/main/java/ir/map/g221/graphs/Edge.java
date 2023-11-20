package ir.map.g221.graphs;

import ir.map.g221.generictypes.UnorderedPair;

public class Edge<T, W> {
    private final W data;
    private final UnorderedPair unorderedPair;

    public Edge(W data, UnorderedPair unorderedPair) {
        this.data = data;
        this.unorderedPair = unorderedPair;
    }

    public Edge(UnorderedPair unorderedPair) {
        this.unorderedPair = unorderedPair;
        this.data = null;
    }
}
