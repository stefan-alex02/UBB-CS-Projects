package ir.map.g221.graphs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node<T> {
    private final T data;
    private final Set<Node<T>> neighbours;

    private Node(T data) {
        this.data = data;
        neighbours = new HashSet<>();
    }

    /**
     * Creates a node containing given data.
     * @param data the data to be contained
     * @return the created Node
     * @param <T> the type of data to be contained
     */
    static <T> Node<T> of(T data) {
        return new Node<>(data);
    }

    /**
     * Gets the data contained inside the node.
     * @return data
     */
    T getData() {
        return data;
    }

    /**
     * Gets the neighbouring nodes.
     * @return the set of neighbours
     */
    Set<Node<T>> getNeighbours() {
        return neighbours;
    }

    /**
     * Connects the two specified nodes, by adding each one to the other's list of neighbours.
     * @param nodeA a node to connect
     * @param nodeB another node to connect
     * @param <T> the type of the data contained inside the nodes
     */
    static <T> void connect(Node<T> nodeA, Node<T> nodeB) {
        nodeA.neighbours.add(nodeB);
        nodeB.neighbours.add(nodeA);
    }

    /**
     * Disconnects the two specified nodes, by removing each one from the other's list of neighbours.
     * @param nodeA a node to disconnect
     * @param nodeB another node to disconnect
     * @param <T> the type of the data contained inside the nodes
     */
    static <T> void disconnect(Node<T> nodeA, Node<T> nodeB) {
        nodeA.neighbours.remove(nodeB);
        nodeB.neighbours.remove(nodeA);
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
            Node<?> node = (Node<?>) o;
            return Objects.equals(data, node.data);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
