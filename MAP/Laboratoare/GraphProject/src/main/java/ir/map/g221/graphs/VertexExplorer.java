package ir.map.g221.graphs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class VertexExplorer<T> {
    private final ConnectedComponent<T> exploredComponent;
    private final Set<Vertex<T>> visitedVertices;

    private VertexExplorer() {
        visitedVertices = new HashSet<>();
        exploredComponent = ConnectedComponent.ofEmpty();
    }

    public ConnectedComponent<T> getExploredComponent() {
        return exploredComponent;
    }

    boolean isVisited(Vertex<T> vertex) {
        return visitedVertices.contains(vertex);
    }

    private void setVisited(Vertex<T> vertex) {
        visitedVertices.add(vertex);
    }

    private void setNotVisited(Vertex<T> vertex) {
        visitedVertices.remove(vertex);
    }

    static <T> ConnectedComponent<T> createComponentExploringFrom(Vertex<T> vertex) {
        VertexExplorer<T> vertexExplorer = new VertexExplorer<>();
        vertexExplorer.BFSCreate(vertex);
        return vertexExplorer.getExploredComponent();
    }

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
}
