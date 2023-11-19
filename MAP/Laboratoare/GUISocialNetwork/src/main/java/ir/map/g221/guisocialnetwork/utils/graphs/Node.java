package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;

import java.util.Set;

/**
 * Interface for any Object that can be viewed as a Node.
 * Note: in order to save the node in a HashSet, the object's fields that are used in the calculation of the
 * Hash Code must NOT be changed after adding the object to a specific Graph (an alternative would be removing,
 * updating then adding the object back to the Graph).
 * @param <T>
 */
public interface Node<T extends Node<T>> {
    /**
     * Checks if subject node has a neighbour.
     * @param neighbour The neighbour node.
     * @return true if the two nodes are linked, false otherwise.
     */
    boolean hasNeighbour(T neighbour);

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

    /**
     * Removes the edge of the nodes in their representation. The action is reciprocated by the other node.
     * @param neighbour The node to unpair with.
     * @throws InvalidEdgeException If the edge in question does not exist.
     */
    void unpairWith(T neighbour) throws InvalidEdgeException;

    /**
     * Gets an index as a String for the node (defaults to toString()).
     * @return the String representation of the index
     */
    default String indexToString() {
        return this.toString();
    }
}
