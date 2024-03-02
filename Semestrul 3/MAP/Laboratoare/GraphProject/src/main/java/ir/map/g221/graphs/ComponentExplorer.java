package ir.map.g221.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComponentExplorer<T> implements GraphExplorer<T> {
    private final ConnectedComponent<T> subjectComponent;
    private final Set<Vertex<T>> visitedVertices;

    private ComponentExplorer(ConnectedComponent<T> subjectComponent) {
        this.subjectComponent = subjectComponent;
        visitedVertices = new HashSet<>();
    }

    public Set<Vertex<T>> getVisitedVertices() {
        return visitedVertices;
    }

    public boolean isVisited(Vertex<T> vertex) {
        return visitedVertices.contains(vertex);
    }

    private void setVisited(Vertex<T> vertex) {
        visitedVertices.add(vertex);
    }

    private void setNotVisited(Vertex<T> vertex) {
        visitedVertices.remove(vertex);
    }

    static <T> List<Vertex<T>> getLongestPathFrom(ConnectedComponent<T> component) {
        List<Vertex<T>> longestPath = new ArrayList<>();

        component.getVertices().forEach(vertex -> {
            List<Vertex<T>> newPath = VertexExplorer.getLongestPathFrom(vertex);
            if (newPath.size() > longestPath.size()) {
                longestPath.clear();
                longestPath.addAll(newPath);
            }
        });

        return longestPath;
    }
}
