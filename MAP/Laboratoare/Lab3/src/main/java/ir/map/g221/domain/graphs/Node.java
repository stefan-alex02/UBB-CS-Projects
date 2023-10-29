package ir.map.g221.domain.graphs;

import java.util.Set;

public interface Node<T extends Node<T>> {
    /**
     * Gets all the neighbours of the Node.
     * @return The set of all neighbours.
     */
    Set<T> getNeighbours();

    /**
     * @return The number of all neighbours of the node.
     */
    Integer getDegree();

    /**
     * @return The string representation of the node.
     */
    String toString();

    /**
     * Pairs the node with another node. The action is reciprocated by the other node.
     * @param neighbour The node to pair with.
     */
    void pairWith(T neighbour);
}
