package ir.map.g221.graphs;

import ir.map.g221.generictypes.UnorderedPair;
import ir.map.g221.graphexceptions.InvalidEdgeException;

public class Edge<T> {
    private final UnorderedPair<Node<T>, Node<T>> unorderedPair;

    private Edge(UnorderedPair<Node<T>, Node<T>> unorderedPair) {
        this.unorderedPair = unorderedPair;
    }

    /**
     * Creates a new edge, and also connects the specified nodes.
     * @param nodeA a node
     * @param nodeB another node
     * @return the newly created edge having the given nodes
     * @param <T> the type of data contained in the nodes
     * @throws InvalidEdgeException if the two nodes are equal
     */
    static <T> Edge<T> ofData(Node<T> nodeA, Node<T> nodeB) throws InvalidEdgeException {
        if (nodeA.equals(nodeB)) {
            throw new InvalidEdgeException("Edge nodes must be different");
        }
        Node.connect(nodeA, nodeB);
        return new Edge<>(UnorderedPair.of(nodeA, nodeB));
    }
}
