package ir.map.g221.graphs;

import java.util.HashSet;
import java.util.Set;

public class GraphComponent<T> implements Graph<T> {
    private final Set<Vertex<T>> vertices;
    private final Set<Edge<T>> edges;

    private GraphComponent() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    private GraphComponent(Vertex<T> vertex) {
        this();
        this.vertices.add(vertex);
    }

    static <T> GraphComponent<T> empty() {
        return new GraphComponent<>();
    }

    static <T> GraphComponent<T> ofVertex(T vertexData) {
        return new GraphComponent<>(Vertex.of(vertexData));
    }

    @Override
    public boolean hasVertex(T vertexData) {
        return vertices.contains(Vertex.of(vertexData));
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return vertices.size();
    }
}
