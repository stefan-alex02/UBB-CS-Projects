package ir.map.g221.graphs;

import java.util.*;

public class VertexExplorer<T> implements GraphExplorer<T> {
    private final ConnectedComponent<T> exploredComponent;
    private final Set<Vertex<T>> visitedVertices = new HashSet<>();
    private final Stack<Vertex<T>> longestPath = new Stack<>();

    private VertexExplorer() {
        exploredComponent = ConnectedComponent.ofEmpty();
    }

    public ConnectedComponent<T> getExploredComponent() {
        return exploredComponent;
    }

    public Set<Vertex<T>> getVisitedVertices() {
        return visitedVertices;
    }

    public boolean isVisited(Vertex<T> vertex) {
        return visitedVertices.contains(vertex);
    }

    /**
     * Sets a vertex as visited.
     * @param vertex the vertex
     */
    private void setVisited(Vertex<T> vertex) {
        visitedVertices.add(vertex);
    }

    /**
     * Sets a vertex as not visited.
     * @param vertex the vertex
     */
    private void setNotVisited(Vertex<T> vertex) {
        visitedVertices.remove(vertex);
    }

    /**
     * Visits all vertices, starting from a specific vertex.
     * @param vertex the vertex to start exploring from
     * @return the set of all visited vertices
     * @param <T> the type of data contained in all vertices
     */
    static <T> Set<Vertex<T>> visitVerticesFrom(Vertex<T> vertex) {
        VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
        vertexExplorer.BFSExplore(vertex);
        return vertexExplorer.getVisitedVertices();
    }

    /**
     * Visits all vertices, starting from a specific vertex, but ignoring the ones given in a set.
     * @param vertex the vertex to start exploring from
     * @param ignoredVertices the set of vertices that must not be explored
     *                        (this also includes passing through them)
     * @return the set of all visited vertices
     * @param <T> the type of data contained in all vertices
     */
    static <T> Set<Vertex<T>> visitVerticesFromButIgnore(Vertex<T> vertex, Set<Vertex<T>> ignoredVertices) {
        VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
        vertexExplorer.BFSExploreExcept(vertex, ignoredVertices);
        return vertexExplorer.getVisitedVertices();
    }

    /**
     * Creates a new connected component by exploring from a given vertex.
     * @param vertex the vertex to start exploring from
     * @return the connected component
     * @param <T> the type of data contained in all vertices
     */
    static <T> ConnectedComponent<T> createComponentExploringFrom(Vertex<T> vertex) {
        VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
        vertexExplorer.BFSCreate(vertex);
        return vertexExplorer.getExploredComponent();
    }

    /**
     * Creates new connected components by exploring from a set of given vertices.
     * @param vertices the vertices to start exploring from
     * @return the set of obtained connected components
     * @param <T> the type of data contained in all vertices
     */
    static <T> Set<ConnectedComponent<T>> createComponentsExploringFrom(Set<Vertex<T>> vertices) {
        Set<ConnectedComponent<T>> components = new HashSet<>();
        Set<Vertex<T>> visitedVertices = new HashSet<>();

        vertices.forEach(vertex -> {
            if (!visitedVertices.contains(vertex)) {
                VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
                vertexExplorer.BFSCreate(vertex);
                components.add(vertexExplorer.getExploredComponent());
                visitedVertices.addAll(vertexExplorer.getVisitedVertices());
            }
        });
        return components;
    }

    /**
     * Gets the longest path that starts from given vertex.
     * @param vertex the vertex to start creating the path from
     * @return the longest path
     * @param <T> the type of data contained in all vertices
     */
    static <T> List<Vertex<T>> getLongestPathFrom(Vertex<T> vertex) {
        VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
        vertexExplorer.generateLongestPathFrom(vertex, new Stack<>());
        return vertexExplorer.longestPath;
    }

    /**
     * Explores all vertices from a given vertex using Breadth-First-Search, returning all visited vertices
     * @param sourceVertex the vertex to start exploring from
     */
    void BFSExplore(Vertex<T> sourceVertex) {
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(sourceVertex);
        setVisited(sourceVertex);
        while(!queue.isEmpty()) {
            Vertex<T> currentVertex = queue.poll();
            currentVertex.getNeighbours()
                    .forEach(neighbour -> {
                        if (!isVisited(neighbour)) {
                            queue.add(neighbour);
                            setVisited(neighbour);
                        }
                    });
        }
    }

    /**
     * Explores all vertices from a given vertex using Breadth-First-Search, except the ones given in a set,
     * returning all visited vertices
     * @param sourceVertex the vertex to start exploring from
     * @param ignoredVertices the set of vertices that must not be explored
     *                        (this also includes passing through them)
     */
    void BFSExploreExcept(Vertex<T> sourceVertex, Set<Vertex<T>> ignoredVertices) {
        if (ignoredVertices.contains(sourceVertex)) {
            return;
        }
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(sourceVertex);
        setVisited(sourceVertex);
        while(!queue.isEmpty()) {
            Vertex<T> currentVertex = queue.poll();
            currentVertex.getNeighbours()
                    .forEach(neighbour -> {
                        if (!isVisited(neighbour) && !ignoredVertices.contains(neighbour)) {
                            queue.add(neighbour);
                            setVisited(neighbour);
                        }
                    });
        }
    }

    /**
     * Creates a new connected component, by exploring from a given vertex using Breadth-First-Search,
     * and keeping previous visited vertices in mind.
     * @param sourceVertex the vertex to start exploring from
     */
    void BFSCreate(Vertex<T> sourceVertex) {
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(sourceVertex);
        setVisited(sourceVertex);
        exploredComponent.addVertex(sourceVertex.getData());
        while(!queue.isEmpty()) {
            Vertex<T> currentVertex = queue.poll();
            currentVertex.getNeighbours()
                    .forEach(neighbour -> {
                if (!isVisited(neighbour)) {
                    exploredComponent.expand(currentVertex.getData(), neighbour.getData());
                    queue.add(neighbour);
                    setVisited(neighbour);
                }
                else {
                    exploredComponent.addEdge(currentVertex.getData(), neighbour.getData());
                }
            });
        }
    }

    /**
     * Recursive method for finding the longest path, by continuing from given vertex.
     * @param vertex the vertex to continue the path from
     */
    private void generateLongestPathFrom(Vertex<T> vertex, Stack<Vertex<T>> currentPath) {
        setVisited(vertex);
        currentPath.push(vertex);
        if (currentPath.size() > longestPath.size()) {
            longestPath.clear();
            longestPath.addAll(currentPath);
        }
        vertex.getNeighbours().forEach(neighbour -> {
            if (!isVisited(neighbour)) {
                generateLongestPathFrom(neighbour, currentPath);
            }
        });
        currentPath.pop();
        setNotVisited(vertex);
    }
}
