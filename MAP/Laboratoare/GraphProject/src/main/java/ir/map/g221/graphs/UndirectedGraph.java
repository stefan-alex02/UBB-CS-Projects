package ir.map.g221.graphs;

import ir.map.g221.generictypes.UnorderedPair;
import ir.map.g221.graphexceptions.InvalidEdgeException;
import ir.map.g221.graphexceptions.InvalidVertexException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UndirectedGraph<T> implements Graph<T> {
    private final Set<ConnectedComponent<T>> components;

    private UndirectedGraph() {
        components = new HashSet<>();
    }

    public static <T> UndirectedGraph<T> ofEmpty() {
        return new UndirectedGraph<>();
    }

    public boolean addVertex(T vertexData) {
        if (!hasVertex(vertexData)) {
            components.add(ConnectedComponent.ofVertex(vertexData));
            return true;
        }
        return false;
    }

    /**
     * Adds vertices to the graph.
     * @param verticesData the set of data of the new vertices to be added
     * @return true if the set had some data which was not contained in the graph, false otherwise
     */
    boolean addVertices(Set<T> verticesData) {
        return verticesData.stream()
                .map(this::addVertex)
                .reduce(false, (anyNew, isNew) -> isNew || anyNew);
    }

    public boolean addEdge(T vertexDataA, T vertexDataB) throws InvalidEdgeException {
        ConnectedComponent<T> componentA = getComponentOf(vertexDataA).orElseThrow(
                () -> new InvalidEdgeException("Vertex A of given edge does not belong to the graph."));
        ConnectedComponent<T> componentB = getComponentOf(vertexDataB).orElseThrow(
                () -> new InvalidEdgeException("Vertex B of given edge does not belong to the graph."));

        boolean wasEdgeNew;

        if (componentA.equals(componentB)) {
            // Beginning of critical code (Graph briefly losses the component that is about to extend)
            components.remove(componentA);
            wasEdgeNew = componentA.addEdge(vertexDataA, vertexDataB);
            components.add(componentA);
            // End of critical code
        }
        else {
            // Beginning of critical code (Graph briefly losses the components that are about to connect)
            components.remove(componentA);
            components.remove(componentB);
            components.add(ConnectedComponent.ofConnection(componentA, componentB, vertexDataA, vertexDataB));
            // End of critical code

            wasEdgeNew = true;
        }

        return wasEdgeNew;
    }

    @Override
    public boolean removeEdge(T vertexDataA, T vertexDataB) throws InvalidVertexException {
        return false;
    }

    /**
     * Adds edges to the graph.
     * @param vertexDataPairs the set of vertex data pairs that should form the edges that must be added
     * @return true if the set had some pairs whose edges were not contained in the graph, false otherwise
     */
    public boolean addEdges(Set<UnorderedPair<T,T>> vertexDataPairs) {
        return vertexDataPairs.stream()
                .map(pair -> addEdge(pair.getFirst(), pair.getSecond()))
                .reduce(false, (anyNew, isNew) -> isNew || anyNew);
    }

    /**
     * Gets the component that has a vertex with the specified data
     * @param vertexData the vertex data to search for in the graph
     * @return an {@link Optional} containing the component with the searched vertex,
     * or an empty {@code Optional} if the vertex does not belong to the graph
     */
    Optional<ConnectedComponent<T>> getComponentOf(T vertexData) {
        return components.stream().filter(component -> component.hasVertex(vertexData)).findAny();
    }

    /**
     * Gets the number of all connected components contained inside the graph.
     * @return the number of components
     */
    public int numberOfComponents() {
        return components.size();
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
                .map(ConnectedComponent::size)
                .reduce(0, Integer::sum);
    }

    @Override
    public int numberOfEdges() {
        return components.stream()
                .map(ConnectedComponent::numberOfEdges)
                .reduce(0, Integer::sum);
    }

    @Override
    public void clear() {
        components.clear();
    }
}
