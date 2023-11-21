package ir.map.g221.graphs;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UndirectedGraph<T> implements Graph<T> {
    private final Set<GraphComponent<T>> components;

    private UndirectedGraph() {
        components = new HashSet<>();
    }

    static <T> UndirectedGraph<T> empty() {
        return new UndirectedGraph<>();
    }

    /**
     * Adds a new vertex to the graph.
     * @param vertexData the data of the new vertex to be added.
     * @return true if the graph did not already contain a vertex with the specified data, false otherwise
     */
    boolean addVertex(T vertexData) {
        if (!hasVertex(vertexData)) {
            components.add(GraphComponent.ofVertex(vertexData));
            return true;
        }
        return false;
    }

    /**
     * Adds new vertices to the graph.
     * @param verticesData the set of data of the new vertices to be added.
     * @return true if the set had some data which was not contained in the graph, false otherwise
     */
    boolean addVertices(Set<T> verticesData) {
        return verticesData.stream()
                .map(this::addVertex)
                .reduce(false, (anyNew, isNew) -> isNew || anyNew);
    }

    @Override
    public boolean hasVertex(T vertexData) {
        return components.stream().anyMatch(component -> component.hasVertex(vertexData));
    }

    @Override
    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public int size() {
        return components.stream()
                .map(GraphComponent::size)
                .reduce(0, Integer::sum);
    }
}
