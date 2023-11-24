package ir.map.g221.graphs;

import ir.map.g221.generictypes.Pair;
import ir.map.g221.generictypes.UnorderedPair;
import ir.map.g221.graphexceptions.InvalidComponentException;
import ir.map.g221.graphexceptions.InvalidEdgeException;
import ir.map.g221.graphexceptions.InvalidVertexException;

import java.net.ConnectException;
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
        if (!containsVertex(vertexData)) {
            components.add(ConnectedComponent.ofVertex(vertexData));
            return true;
        }
        return false;
    }
    @Override
    public boolean removeVertex(T vertexData) {
        if (!containsVertex(vertexData)) {
            return false;
        }
        ConnectedComponent<T> component = getComponentOf(vertexData).orElseThrow();

        // -- Beginning of critical code (Graph briefly losses the component that is about to change) --
        components.remove(component);
        Optional<Set<ConnectedComponent<T>>> resultedComponents =
                ConnectedComponent.removeVertex(component, vertexData);
        if (resultedComponents.isEmpty()) {
            components.add(component);
            // -- End of critical code : branch 1 --

            return true;
        }
        components.addAll(resultedComponents.get());
        // -- End of critical code : branch 2 --

        return true;
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
            // -- Beginning of critical code (Graph briefly losses the component that is about to extend) --
            components.remove(componentA);
            wasEdgeNew = componentA.addEdge(vertexDataA, vertexDataB);
            components.add(componentA);
            // -- End of critical code --
        }
        else {
            // -- Beginning of critical code (Graph briefly losses the components that are about to connect) --
            components.remove(componentA);
            components.remove(componentB);
            components.add(ConnectedComponent.ofConnection(componentA, componentB, vertexDataA, vertexDataB));
            // -- End of critical code --

            wasEdgeNew = true;
        }

        return wasEdgeNew;
    }

    /**
     * @throws InvalidVertexException If any of the vertices does not belong to the graph
     */
    @Override
    public boolean removeEdge(T vertexDataA, T vertexDataB) throws InvalidVertexException {
        if (!containsVertex(vertexDataA) || !containsVertex(vertexDataB)) {
            throw new InvalidVertexException("At least one of the vertices does not belong to the graph.");
        }
        ConnectedComponent<T> component = getComponentOf(vertexDataA).orElseThrow();
        int oldNumberOfEdges = component.numberOfEdges();

        // -- Beginning of critical code (Graph briefly losses the component that is about to change) --
        components.remove(component);
        Optional<Pair<ConnectedComponent<T>, ConnectedComponent<T>>> resultedComponents =
                ConnectedComponent.removeEdge(component, vertexDataA, vertexDataB);
        if (resultedComponents.isEmpty()) {
            components.add(component);
            // -- End of critical code : branch 1 --

            return component.numberOfEdges() < oldNumberOfEdges;
        }
        ConnectedComponent<T> firstComponent = resultedComponents.get().getFirst();
        ConnectedComponent<T> secondComponent = resultedComponents.get().getSecond();
        components.add(firstComponent);
        components.add(secondComponent);
        // -- End of critical code : branch 2 --

        return firstComponent.numberOfEdges() + secondComponent.numberOfEdges() < oldNumberOfEdges;
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
        return components.stream().filter(component -> component.containsVertex(vertexData)).findAny();
    }

    /**
     * Gets the number of all connected components contained inside the graph.
     * @return the number of components
     */
    public int numberOfComponents() {
        return components.size();
    }

    @Override
    public boolean containsVertex(T vertexData) {
        return components.stream().anyMatch(component -> component.containsVertex(vertexData));
    }

    @Override
    public boolean containsAllVertices(Set<T> verticesData) {
        return verticesData.stream()
                .allMatch(this::containsVertex);
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
