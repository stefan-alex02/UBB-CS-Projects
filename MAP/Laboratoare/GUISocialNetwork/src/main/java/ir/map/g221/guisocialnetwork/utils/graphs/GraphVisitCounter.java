package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidNodeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphVisitCounter<TNode extends Node<TNode>> {
    private final Graph<TNode> graph;
    private final Map<TNode, Boolean> nodeVisits;
    private final Map<Edge<TNode>, Boolean> edgeVisits;

    public GraphVisitCounter(Graph<TNode> graph) {
        this.graph = graph;
        nodeVisits = new HashMap<>();
        edgeVisits = new HashMap<>();
        graph.getNodes().forEach(this::setNotVisited);
    }

    public Set<TNode> getVisitedNodes() {
        return graph.getNodes().stream()
                .filter(this::isVisited)
                .collect(Collectors.toSet());
    }

    public boolean isVisited(TNode node) throws InvalidNodeException {
        if (!graph.hasNode(node)) {
            throw new InvalidNodeException("Node does not belong to subject graph.");
        }
        return nodeVisits.get(node);
    }

    public void setNotVisited(TNode node) throws InvalidNodeException {
        if (!graph.hasNode(node)) {
            throw new InvalidNodeException("Node does not belong to subject graph.");
        }
        nodeVisits.put(node, false);
    }

    public void setVisited(TNode node) throws InvalidNodeException {
        if (!graph.hasNode(node)) {
            throw new InvalidNodeException("Node does not belong to subject graph.");
        }
        nodeVisits.put(node, true);
    }

    public boolean isVisited(Edge<TNode> edge) throws InvalidEdgeException {
        if (!graph.hasEdge(edge)) {
            throw new InvalidEdgeException("Edge is not included in the subject graph.");
        }
        return edgeVisits.get(edge);
    }

    public void setNotVisited(Edge<TNode> edge) throws InvalidEdgeException {
        if (!graph.hasEdge(edge)) {
            throw new InvalidEdgeException("Edge is not included in the subject graph.");
        }
        edgeVisits.put(edge, false);
    }

    public void setVisited(Edge<TNode> edge) throws InvalidEdgeException {
        if (!graph.hasEdge(edge)) {
            throw new InvalidEdgeException("Edge is not included in the subject graph.");
        }
        edgeVisits.put(edge, true);
    }
}
