package ir.map.g221.graphs;

public interface Graph<T> {

    /**
     * Checks if the graph contains a vertex with the given data.
     * @param vertexData the vertex data to look for.
     * @return true if there is a vertex with the specified data in the graph, false otherwise
     */
    boolean hasVertex(T vertexData);

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
}
