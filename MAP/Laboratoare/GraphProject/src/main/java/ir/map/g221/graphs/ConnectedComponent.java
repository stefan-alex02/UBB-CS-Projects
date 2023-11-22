package ir.map.g221.graphs;

import ir.map.g221.graphexceptions.InvalidComponentException;
import ir.map.g221.graphexceptions.InvalidEdgeException;
import ir.map.g221.graphexceptions.InvalidVertexException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
        if (!componentA.hasVertex(vertexDataA)) {
            throw new InvalidVertexException("First given vertex must belong to the first given component.");
        }
        if (!componentB.hasVertex(vertexDataB)) {
            throw new InvalidVertexException("Second given vertex must belong to the second given component.");
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
     * Expands the component by adding a new vertex and a new edge that connects it to another existing vertex.
     * @param existingVertexData data of the existing vertex
     * @param newVertexData data of the new vertex
     * @throws InvalidVertexException if {@code existingVertexData} does not belong to the graph,
     * or if {@code newVertexData} is already contained in the graph
     */
    void expand(T existingVertexData, T newVertexData) throws InvalidVertexException {
        if (!hasVertex(existingVertexData)) {
            throw new InvalidVertexException("1st given vertex parameter must exist in the component.");
        }
        if (hasVertex(newVertexData)) {
            throw new InvalidVertexException("2nd given vertex parameter must not exist in the component.");
        }

        Vertex<T> existingVertex = getVertex(existingVertexData);
        Vertex<T> newVertex = Vertex.of(newVertexData);

        // -- Beginning of critical code (Component briefly losses its connectivity property) --
        vertices.add(newVertex);
        edges.add(Edge.of(existingVertex, newVertex));
        // -- End of critical code --
    }

    public boolean addEdge(T vertexDataA, T vertexDataB) throws InvalidEdgeException {
        if (!hasVertex(vertexDataA) || !hasVertex(vertexDataB)) {
            throw new InvalidEdgeException("Edge vertices do not belong to the graph.");
        }

        return edges.add(Edge.of(getVertex(vertexDataA), getVertex(vertexDataB)));
    }


    @Override
    public boolean hasVertex(T vertexData) {
        return vertices.stream().anyMatch(vertex -> vertex.equals(vertexData));
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
