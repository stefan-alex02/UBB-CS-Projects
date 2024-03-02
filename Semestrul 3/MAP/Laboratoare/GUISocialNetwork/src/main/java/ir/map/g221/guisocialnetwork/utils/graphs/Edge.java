package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;

import java.util.Objects;

public class Edge<T> {
    private final UnorderedPair<Vertex<T>, Vertex<T>> unorderedPair;

    private Edge(UnorderedPair<Vertex<T>, Vertex<T>> unorderedPair) {
        this.unorderedPair = unorderedPair;
    }

    /**
     * Creates a new edge, and also connects the specified nodes in their implementation.
     * @param vertexA a node
     * @param vertexB another node
     * @return the newly created edge having the given nodes
     * @param <T> the type of data contained in the nodes
     * @throws InvalidEdgeException if the two nodes are equal
     */
    static <T> Edge<T> of(Vertex<T> vertexA, Vertex<T> vertexB) throws InvalidEdgeException {
        if (vertexA.equals(vertexB)) {
            throw new InvalidEdgeException("Edge nodes must be different");
        }
        return new Edge<>(UnorderedPair.of(vertexA, vertexB));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return Objects.equals(unorderedPair, edge.unorderedPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unorderedPair);
    }
}
