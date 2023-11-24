package ir.map.g221.graphs;

import ir.map.g221.graphexceptions.InvalidEdgeException;
import ir.map.g221.graphexceptions.InvalidVertexException;

import java.util.Set;

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
    boolean containsVertex(T vertexData);

    /**
     * Checks if graph contains a vertex for every given data in the set.
     * @param verticesData the set of data to search for in the graph
     * @return true if graph contains all vertices, false otherwise
     */
    boolean containsAllVertices(Set<T> verticesData);

    /**
     * Adds a vertex to the graph.
     * @param vertexData the data of the vertex to be added
     * @return true if the graph did not already contain the vertex with the specified data, false otherwise
     */
    boolean addVertex(T vertexData);

    /**
     * Removes a vertex from the graph.
     * @param vertexData the data of the vertex to be removed
     * @return true if the graph contained the vertex with the specified data before removing, false otherwise
     */
    boolean removeVertex(T vertexData);

    /**
     * Adds an edge to the graph.
     * @param vertexDataA data of one vertex, that belongs to the graph
     * @param vertexDataB data of another vertex, that belongs to the graph
     * @return true if the graph did not already contain the given edge, false otherwise
     * @throws InvalidEdgeException if any of the specified vertices do not belong to the graph
     */
    boolean addEdge(T vertexDataA, T vertexDataB) throws InvalidEdgeException;

    /**
     * Removes an edge to the graph, specified by the data of its two vertices.
     * @param vertexDataA data of one vertex, that belongs to the graph
     * @param vertexDataB data of another vertex, that belongs to the graph
     * @return true if the graph contained the specified edge, false otherwise
     * @throws InvalidVertexException if given edge vertices do not belong to the graph
     */
    boolean removeEdge(T vertexDataA, T vertexDataB) throws InvalidVertexException;

    /**
     * Clears the entire content of the graph.
     */
    void clear();
}
