package ir.map.g221.graphs;

import ir.map.g221.generictypes.Pair;
import ir.map.g221.graphexceptions.InvalidComponentException;
import ir.map.g221.graphexceptions.InvalidEdgeException;
import ir.map.g221.graphexceptions.InvalidVertexException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ConnectedComponent<T> implements Graph<T> {
    private final Set<Vertex<T>> vertices;
    private final Set<Edge<T>> edges;

    private ConnectedComponent() {
        this.vertices = new HashSet<>();
        this.edges = new HashSet<>();
    }

    private ConnectedComponent(Vertex<T> vertex) {
        this();
        this.vertices.add(vertex);
    }

    static <T> ConnectedComponent<T> ofEmpty() {
        return new ConnectedComponent<>();
    }

    static <T> ConnectedComponent<T> ofVertex(T vertexData) {
        return new ConnectedComponent<>(Vertex.of(vertexData));
    }

    /**
     * Merges two components by connecting two of their vertices.
     * @param componentA first component to connect
     * @param componentB second component to connect
     * @param vertexDataA data of the vertex contained in the first component
     * @param vertexDataB data of the vertex contained in the second component
     * @return a component containing all vertices and edges in the given components, including the new edge
     * between {@code vertexDataA} and {@code vertexDataB}
     * @param <T> the type of data contained in all vertices
     * @throws InvalidComponentException if any of the components is empty
     * @throws InvalidVertexException if the first given vertex does not belong to the first given component,
     * or for the second vertex respectively
     */
    static <T> ConnectedComponent<T> ofConnection(ConnectedComponent<T> componentA, ConnectedComponent<T> componentB,
                                                  T vertexDataA, T vertexDataB)
            throws InvalidComponentException, InvalidVertexException {
        if (componentA.isEmpty() || componentB.isEmpty()) {
            throw new InvalidComponentException("None of the given components can be empty.");
        }

        Vertex<T> vertexA = componentA.getVertex(vertexDataA);
        Vertex<T> vertexB = componentB.getVertex(vertexDataB);

        ConnectedComponent<T> componentToExpand, componentToClear;
        if (componentA.size() >= componentB.size()) {
            componentToExpand = componentA;
            componentToClear = componentB;
        }
        else {
            componentToExpand = componentB;
            componentToClear = componentA;
        }

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        componentToExpand.vertices.addAll(componentToClear.vertices);
        componentToExpand.edges.addAll(componentToClear.edges);
        Vertex.connect(vertexA, vertexB);
        componentToExpand.edges.add(Edge.of(vertexA, vertexB));
        // -- End of critical code --

        componentToClear.clear();

        return componentToExpand;
    }

    /**
     * Gets the vertex containing specified data.
     * @param data the data to search for
     * @return the vertex, belonging to the current component
     * @throws InvalidVertexException if there is no vertex in the component with the specified data
     */
    private Vertex<T> getVertex(T data) throws InvalidVertexException {
        return vertices.stream()
                .filter(vertex -> vertex.equals(data))
                .findAny()
                .orElseThrow(() -> new InvalidVertexException("Vertex does not belong to the component."));
    }

    /**
     * Checks if given vertex is a cut vertex (a.k.a. an articulation point)
     * @param data the data of the vertex to be checked for
     * @return true if the vertex is a cut vertex, false otherwise
     * @throws InvalidVertexException if the vertex does not belong to the component
     */
    boolean isCutVertex(T data) throws InvalidVertexException {
        if (!containsVertex(data)) {
            throw new InvalidVertexException("Given vertex does not belong to the component.");
        }
        Vertex<T> vertex = getVertex(data);

        Set<Vertex<T>> neighbours = vertex.getNeighbours();
        return !VertexExplorer.visitVerticesFromButIgnore(
                        neighbours.iterator().next(), Set.of(vertex))
                .containsAll(neighbours);
    }

    /**
     * @throws InvalidComponentException if the component is not empty and the given vertex
     * is not already included in the component
     */
    @Override
    public boolean addVertex(T vertexData) throws InvalidComponentException {
        if (isEmpty() || containsVertex(vertexData)) {
            return vertices.add(Vertex.of(vertexData));
        }
        throw new InvalidComponentException("Component would lose connectivity property if given vertex was added.");
    }


    /**
     * @throws InvalidComponentException if the specified vertex would break connectivity in component if removed
     * (in other words, if the component has more than one vertex and the given vertex is a cut vertex
     * (a.k.a. an articulation point)
     */
    @Override
    public boolean removeVertex(T vertexData) throws InvalidComponentException {
        if (isEmpty() || size() == 1) {
            return vertices.remove(Vertex.of(vertexData));
        }
        if (!containsVertex(vertexData)) {
            return false;
        }
        Vertex<T> vertex = getVertex(vertexData);

        if (vertex.getNeighbours().size() == 1) {
            retractTerminal(vertexData);
            return true;
        }

        if (isCutVertex(vertexData)) {
            throw new InvalidComponentException(
                    "Component would lose connectivity property if specified vertex was removed.");
        }
        Set<Vertex<T>> vertexNeighbours = vertex.getNeighbours();

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        Set.copyOf(vertexNeighbours).forEach(neighbour -> {
            Vertex.disconnect(vertex, neighbour);
            edges.remove(Edge.of(vertex, neighbour));
        });
        vertices.remove(vertex);
        // -- End of critical code --

        return true;
    }

    /**
     * Removes a vertex, and if it is a cut vertex, returns the set of resulted components.
     * @param component the component to remove the vertex from
     * @param vertexData data of the vertex to remove, that belongs to the graph
     * @return an {@link Optional} containing the set of components, if the vertex was a cut vertex
     * (a.k.a. an articulation point), or an empty {@code Optional}, otherwise
     * @implNote If the vertex is a cut vertex and a set of components is returned,
     * the old component that contained the vertex will be entirely cleared.
     * @param <T> the type of data contained in all vertices
     */
    static <T> Optional<Set<ConnectedComponent<T>>> removeVertex(ConnectedComponent<T> component, T vertexData) {
        if (component.isEmpty() || component.size() == 1) {
            component.vertices.remove(Vertex.of(vertexData));
            return Optional.empty();
        }
        if (!component.containsVertex(vertexData)) {
            return Optional.empty();
        }
        Vertex<T> vertex = component.getVertex(vertexData);

        if (vertex.getNeighbours().size() == 1) {
            component.retractTerminal(vertexData);
            return Optional.empty();
        }
        Set<Vertex<T>> vertexNeighbours = Set.copyOf(vertex.getNeighbours());

        boolean isCutVertex = component.isCutVertex(vertexData);

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        vertexNeighbours.forEach(neighbour -> {
            Vertex.disconnect(vertex, neighbour);
            component.edges.remove(Edge.of(vertex, neighbour));
        });
        component.vertices.remove(vertex);

        if (!isCutVertex) {
            // -- End of critical code : branch 1 --
            return  Optional.empty();
        }
        Set<ConnectedComponent<T>> components = VertexExplorer.createComponentsExploringFrom(vertexNeighbours);
        component.clear();
        // -- End of critical code : branch 2 --

        return Optional.of(components);
    }


    public boolean addEdge(T vertexDataA, T vertexDataB) throws InvalidEdgeException {
        if (!containsVertex(vertexDataA) || !containsVertex(vertexDataB)) {
            throw new InvalidEdgeException("Edge vertices do not belong to the graph.");
        }

        Vertex<T> vertexA = getVertex(vertexDataA);
        Vertex<T> vertexB = getVertex(vertexDataB);

        Vertex.connect(vertexA, vertexB);
        return edges.add(Edge.of(vertexA, vertexB));
    }

    /**
     * @throws InvalidVertexException If any of the vertices does not belong to the graph
     * @throws InvalidComponentException If the specified edge would break connectivity in component if removed
     * (in other words, if the edge is a bridge in the component)
     */
    @Override
    public boolean removeEdge(T vertexDataA, T vertexDataB) throws InvalidVertexException, InvalidComponentException {
        if (!containsVertex(vertexDataA) || !containsVertex(vertexDataB)) {
            throw new InvalidEdgeException("Edge vertices do not belong to component.");
        }

        Vertex<T> vertexA = getVertex(vertexDataA);
        Vertex<T> vertexB = getVertex(vertexDataB);

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        if (!edges.remove(Edge.of(vertexA, vertexB))) {
            return false;
        }
        Vertex.disconnect(vertexA, vertexB);

        ConnectedComponent<T> componentOfVertexA = VertexExplorer.createComponentExploringFrom(vertexA);
        if(!componentOfVertexA.containsVertex(vertexDataB)) {
            addEdge(vertexDataA, vertexDataB);
            // -- End of critical code : branch 1 --
            throw new InvalidComponentException(
                    "Component would lose connectivity property if specified edge was removed.");
        }
        // -- End of critical code : branch 2 --

        return true;
    }

    /**
     * Removes an edge, and if it is a bridge, returns the two resulted components.
     * @param component the component to remove the edge from
     * @param vertexDataA data of one vertex, that belongs to the graph
     * @param vertexDataB data of another vertex, that belongs to the graph
     * @return an {@link Optional} containing the pair of components (first one containing vertexDataA
     * and second one containing vertexDataB respectively), if the edge was a bridge,
     * or an empty {@code Optional}, otherwise
     * @implNote If the edge is a bridge and a pair of components is returned,
     * the old component that contained the edge will be entirely cleared.
     * @param <T> the type of data contained in all vertices
     * @throws InvalidVertexException if any of the given edge vertices does not belong to the graph
     */
    static <T> Optional<Pair<ConnectedComponent<T>, ConnectedComponent<T>>> removeEdge(
            ConnectedComponent<T> component, T vertexDataA, T vertexDataB) throws InvalidVertexException {
        if (!component.containsVertex(vertexDataA) || !component.containsVertex(vertexDataB)) {
            throw new InvalidVertexException("At least one vertex do not belong to component.");
        }

        Vertex<T> vertexA = component.getVertex(vertexDataA);
        Vertex<T> vertexB = component.getVertex(vertexDataB);

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        if (!component.edges.remove(Edge.of(vertexA, vertexB))) {
            return Optional.empty();
        }
        Vertex.disconnect(vertexA, vertexB);

        ConnectedComponent<T> componentOfVertexA = VertexExplorer.createComponentExploringFrom(vertexA);
        if(!componentOfVertexA.containsVertex(vertexDataB)) {
            ConnectedComponent<T> componentOfVertexB = VertexExplorer.createComponentExploringFrom(vertexB);
            component.clear();
            // -- End of critical code : branch 1 --
            return Optional.of(Pair.of(componentOfVertexA, componentOfVertexB));
        }
        // -- End of critical code : branch 2 --

        return Optional.empty();
    }

    /**
     * Expands the component by adding a new vertex and a new edge that connects it to another existing vertex.
     * @param existingVertexData data of the existing vertex
     * @param newVertexData data of the new vertex
     * @throws InvalidVertexException if {@code existingVertexData} does not belong to the graph,
     * or if {@code newVertexData} is already contained in the graph
     */
    void expand(T existingVertexData, T newVertexData) throws InvalidVertexException {
        if (!containsVertex(existingVertexData)) {
            throw new InvalidVertexException("1st given vertex parameter must exist in the component.");
        }
        if (containsVertex(newVertexData)) {
            throw new InvalidVertexException("2nd given vertex parameter must not exist in the component.");
        }

        Vertex<T> existingVertex = getVertex(existingVertexData);
        Vertex<T> newVertex = Vertex.of(newVertexData);

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        vertices.add(newVertex);
        Vertex.connect(existingVertex, newVertex);
        edges.add(Edge.of(existingVertex, newVertex));
        // -- End of critical code --
    }

    /**
     * Retracts the component by removing a terminal vertex, along the edge that connects it.
     * @param vertexDataToRemove the terminal vertex that is about to be removed, along its only edge
     * @throws InvalidVertexException if the vertex is not terminal (has only one neighbour)
     */
    void retractTerminal(T vertexDataToRemove) throws InvalidVertexException {
        if (!containsVertex(vertexDataToRemove)) {
            throw new InvalidVertexException("Given vertex must exist in the component.");
        }
        Vertex<T> vertexToRemove = getVertex(vertexDataToRemove);

        if (vertexToRemove.getNeighbours().size() != 1) {
            throw new InvalidVertexException("Given vertex is not terminal.");
        }
        Vertex<T> neighbour = vertexToRemove.getNeighbours().iterator().next();

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        edges.remove(Edge.of(vertexToRemove, neighbour));
        Vertex.disconnect(vertexToRemove, neighbour);
        vertices.remove(vertexToRemove);
        // -- End of critical code --
    }

    @Override
    public boolean containsVertex(T vertexData) {
        return vertices.stream().anyMatch(vertex -> vertex.equals(vertexData));
    }

    @Override
    public boolean containsAllVertices(Set<T> verticesData) {
        return verticesData.stream()
                .allMatch(this::containsVertex);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return vertices.size();
    }

    @Override
    public int numberOfEdges() {
        return edges.size();
    }

    @Override
    public void clear() {
        edges.clear();
        vertices.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedComponent<?> that = (ConnectedComponent<?>) o;
        return Objects.equals(vertices, that.vertices) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }
}
