package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;

import java.util.Set;

public interface Graph<TNode extends Node<TNode>> {
    /**
     * Gets the set of all nodes in the graph.
     * @return the nodes.
     */
    Set<TNode> getNodes();

    /**
     * Checks if the graph has any nodes.
     * @return true if the graph has no nodes, false otherwise.
     */
    boolean isEmpty();

    /**
     * Gets the number of nodes in the graph
     * @return the number of nodes.
     */
    int size();

    /**
     * Checks if the graph has a specific node.
     * @param node the node to be looked for.
     * @return true if the graph contains the node, false otherwise.
     */
    boolean hasNode(TNode node);

    /**
     * Checks if the graph has a specific edge.
     * @param edge the edge to be looked for.
     * @return true if the graph contains the edge, false otherwise.
     */
    boolean hasEdge(Edge<TNode> edge);

    /**
     * Adds an edge to the graph, if it can be added, otherwise throws exception.
     * @param edge The edge that must be added.
     * @throws InvalidEdgeException If the edge could not be added.
     */
    void forceAddEdge(Edge<TNode> edge) throws InvalidEdgeException;

    /**
     * Tries to add an edge to the graph.
     * @param edge The edge that should be added.
     * @return true if the edge was added successfully, otherwise false.
     */
    boolean tryAddEdge(Edge<TNode> edge);

    /**
     * Adds all edges to the graph, if all of them can be added, otherwise throws exception.
     * @param edges The edges that must be added.
     * @throws InvalidEdgeException If there are any edge that cannot be added.
     */
    void forceAddEdges(Set<Edge<TNode>> edges) throws InvalidEdgeException;

    /**
     * Tries to add the edges to the graph.
     * @param edges The edges that should be added.
     * @return true if all edges were added successfully, otherwise false.
     */
    boolean tryAddEdges(Set<Edge<TNode>> edges);

    /**
     * Deletes all content stored in the graph.
     */
    void reset();
}
