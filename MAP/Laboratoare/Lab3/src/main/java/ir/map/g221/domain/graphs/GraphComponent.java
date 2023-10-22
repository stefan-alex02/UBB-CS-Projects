package ir.map.g221.domain.graphs;

import ir.map.g221.exceptions.graphs.InvalidGraphException;

import java.util.*;

public final class GraphComponent<TNode extends Node<TNode>> extends UnorderedGraph<TNode> {
    private Path<TNode> longestPath;

    public GraphComponent() {
        super();
        this.longestPath = null;
    }

    public GraphComponent(Set<TNode> nodes) {
        super(nodes);
        this.longestPath = null;
    }

    public GraphComponent(Set<TNode> nodes, Set<Edge<TNode>> edges) throws InvalidGraphException {
        super(nodes, edges);
        this.longestPath = null;
    }

    @Override
    public void forceAddEdge(Edge<TNode> edge) throws InvalidGraphException {
        super.forceAddEdge(edge);
        this.longestPath = null;
    }

    @Override
    public boolean tryAddEdge(Edge<TNode> edge) {
        boolean result = super.tryAddEdge(edge);
        this.longestPath = null;
        return result;
    }

    @Override
    public void forceAddEdges(Iterable<Edge<TNode>> edges) throws InvalidGraphException {
        super.forceAddEdges(edges);
        this.longestPath = null;
    }

    @Override
    public boolean tryAddEdges(Iterable<Edge<TNode>> edges) {
        boolean result = super.tryAddEdges(edges);
        this.longestPath = null;
        return result;
    }

    public Path<TNode> getLongestPath() {
        if (longestPath == null) {
            generateLongestPath();
        }
        return longestPath;
    }

    private void generateLongestPath() {
        longestPath = new Path<>();
        for (TNode node: nodes) {
            Path<TNode> path = getLongestPathFrom(node, new Stack<>());
            if (path.compareTo(longestPath) > 0) {
                longestPath = path;
            }
        }
    }

    private Path<TNode> getLongestPathFrom(TNode node, Stack<Edge<TNode>> exploredEdges) {
        Path<TNode> maximalPath = new Path<>();
        maximalPath.addToBeginning(node);
        for (TNode neighbour: node.getNeighbours()) {
            Edge<TNode> currentEdge = Edge.of(node, neighbour);
            if (!exploredEdges.contains(currentEdge)) {
                exploredEdges.push(currentEdge);
                Path<TNode> newPath = getLongestPathFrom(neighbour, exploredEdges);
                newPath.addToBeginning(node);
                if (newPath.compareTo(maximalPath) > 0) {
                    maximalPath = newPath;
                }
                exploredEdges.pop();
            }
        }
        return maximalPath;
    }

    @Override
    public String toString() {
        return "Component[" +
                "nodes=" + nodes + ']';
    }
}
