package ir.map.g221.guisocialnetwork.utils.graphs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Vertex<T> {
    private T data;
    private final Set<Vertex<T>> neighbours;

    private Vertex(T data) {
        this.data = data;
        neighbours = new HashSet<>();
    }

    /**
     * Creates a vertex containing given data.
     * @param data the data to be contained
     * @return the created Vertex
     * @param <T> the type of data to be contained
     */
    static <T> Vertex<T> of(T data) {
        return new Vertex<>(data);
    }

    /**
     * Modifies the data of the vertex.
     * @param data the new data to update in the vertex
     */
    void setData(T data) {
        this.data = data;
    }

    /**
     * Gets the data contained inside the vertex.
     * @return data
     */
    T getData() {
        return data;
    }

    /**
     * Gets the neighbouring vertices.
     * @return the set of neighbours
     */
    Set<Vertex<T>> getNeighbours() {
        return neighbours;
    }

    /**
     * Connects the two specified vertices, by adding each one to the other's list of neighbours.
     * @param vertexA a vertex to connect
     * @param vertexB another vertex to connect
     * @param <T> the type of the data contained inside the vertices
     */
    static <T> void connect(Vertex<T> vertexA, Vertex<T> vertexB) {
        vertexA.neighbours.add(vertexB);
        vertexB.neighbours.add(vertexA);
    }

    /**
     * Disconnects the two specified vertices, by removing each one from the other's list of neighbours.
     * @param vertexA a vertex to disconnect
     * @param vertexB another vertex to disconnect
     * @param <T> the type of the data contained inside the vertices
     */
    static <T> void disconnect(Vertex<T> vertexA, Vertex<T> vertexB) {
        vertexA.neighbours.remove(vertexB);
        vertexB.neighbours.remove(vertexA);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass() && o.getClass() != data.getClass())) return false;
        if (o.getClass() == data.getClass()) {
            T oData = (T) o;
            return Objects.equals(data, oData);
        }
        else {
            Vertex<?> vertex = (Vertex<?>) o;
            return Objects.equals(data, vertex.data);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
