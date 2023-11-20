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

    public static <T> Node<T> of(T data) {
        return new Node<>(data);
    }

    public T getData() {
        return data;
    }

    public Set<Node<T>> getNeighbours() {
        return neighbours;
    }

    /**
     * Adds a neighbour to the node's set of neighbours.
     * @param newNeighbour The new neighbour.
     * @return true if the node did not already have the specified neighbour, false otherwise.
     */
    public boolean addNeighbour(Node<T> newNeighbour) {
        return neighbours.add(newNeighbour);
    }

    /**
     * Removes a neighbour from the node's set of neighbours.
     * @param neighbourToRemove The neighbour to be removed.
     * @return true if the node had the specified neighbour, false otherwise.
     */
    public boolean removeNeighbour(Node<T> neighbourToRemove) {
        return neighbours.remove(neighbourToRemove);
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
