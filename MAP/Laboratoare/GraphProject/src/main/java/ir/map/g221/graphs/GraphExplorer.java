package ir.map.g221.graphs;

import java.util.HashMap;
import java.util.Map;

public class GraphExplorer<T> {
    private final Graph<T> subjectGraph;
    private final Map<Vertex<T>, Boolean> visitedMap;

    GraphExplorer(Graph<T> subjectGraph) {
        this.subjectGraph = subjectGraph;
        visitedMap = new HashMap<>();
    }

    private void initialize() {
        visitedMap.clear();

    }
}
