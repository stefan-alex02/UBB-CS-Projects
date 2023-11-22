package ir.map.g221.graphs;

import ir.map.g221.graphexceptions.InvalidEdgeException;

public interface Graph<T> {
    /**
     * Tells if the graph has no vertices.
     * @return true if graph is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the number of vertices in the graph.
     * @return the number of vertices
     */
    int size();

    /**
     * Gets the number of edges in the graph.
     * @return the number of edges
     */
    int numberOfEdges();

    /**
     * Checks if the graph contains a vertex with the given data.
     * @param vertexData the vertex data to look for
     * @return true if there is a vertex with the specified data in the graph, false otherwise
     */
    boolean hasVertex(T vertexData);

    /**
     * Adds an edge to the graph.
     * @param vertexDataA data of one vertex, that belongs to the graph
     * @param vertexDataB data of another vertex, that belongs to the graph
     * @return true if the graph did not already contain the given edge, false otherwise
     * @throws InvalidEdgeException if any of the specified vertices do not belong to the graph
     */
    boolean addEdge(T vertexDataA, T vertexDataB) throws InvalidEdgeException;

    /**
     * Clears the entire content of the graph.
     */
    void clear();
}
