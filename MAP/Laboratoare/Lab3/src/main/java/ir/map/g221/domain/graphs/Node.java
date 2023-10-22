package ir.map.g221.domain.graphs;

import java.util.Set;

public interface Node<T extends Node<T>> {
    Set<T> getNeighbours();
    Integer getDegree();
    String toString();

    /**
     * Pairs the node with another node. The action is reciprocated by the other node.
     * @param neighbour The node to pair with.
     */
    void pairWith(T neighbour);
}
