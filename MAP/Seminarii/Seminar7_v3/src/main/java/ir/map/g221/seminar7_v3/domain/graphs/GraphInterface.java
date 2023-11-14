package ir.map.g221.seminar7_v3.domain.graphs;

import ir.map.g221.seminar7_v3.exceptions.graphs.InvalidGraphException;

public interface GraphInterface<TNode extends Node<TNode>> {
    /**
     * Adds an edge to the graph, if it can be added, otherwise throws exception.
     * @param edge The edge that must be added.
     * @throws InvalidGraphException If the edge could not be added.
     */
    void forceAddEdge(Edge<TNode> edge) throws InvalidGraphException;

    /**
     * Tries to add an edge to the graph.
     * @param edge The edge that should be added.
     * @return true if the edge was added successfully, otherwise false.
     */
    boolean tryAddEdge(Edge<TNode> edge);

    /**
     * Adds all edges to the graph, if all of them can be added, otherwise throws exception.
     * @param edges The edges that must be added.
     * @throws InvalidGraphException If there are any edge that cannot be added.
     */
    void forceAddEdges(Iterable<Edge<TNode>> edges) throws InvalidGraphException;

    /**
     * Tries to add the edges to the graph.
     * @param edges The edges that should be added.
     * @return true if all edges were added successfully, otherwise false.
     */
    public boolean tryAddEdges(Iterable<Edge<TNode>> edges);
}
