package ir.map.g221.graphs;

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


}
