package ir.map.g221.graphs;

import java.util.Set;

public interface GraphExplorer<T> {
    /**
     * Gets the set of all visited vertices.
     * @return the visited vertices
     */
    Set<Vertex<T>> getVisitedVertices();

    /**
     * Tells if a vertex was visited by the explorer.
     * @param vertex the vertex
     * @return true if the vertex was visited, false otherwise
     */
    public boolean isVisited(Vertex<T> vertex);
}
