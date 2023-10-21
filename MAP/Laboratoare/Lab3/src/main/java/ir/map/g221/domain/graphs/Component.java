package ir.map.g221.domain.graphs;

import ir.map.g221.exceptions.graphs.InvalidComponentException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public final class Component<TNode extends Node<TNode>> implements Iterable<TNode> {
    private final Set<TNode> nodes;
    private final Set<Edge<TNode>> edges;
    private Path<TNode> longestPath;

    public Component(Set<TNode> nodes) {
        this.nodes = nodes;
        edges = new HashSet<>();
        longestPath = null;
    }

    public Set<TNode> getNodes() {
        return nodes;
    }

    public Component(Set<TNode> nodes, Set<Edge<TNode>> edges) throws InvalidComponentException {
        this.nodes = nodes;
        this.edges = new HashSet<>();
        longestPath = null;
        addEdges(edges);
    }

    private void verifyEdgeSetInclusion(Set<Edge<TNode>> edges) throws InvalidComponentException {
        if (!edges.stream().allMatch(this::canIncludeEdge)) {
            throw new InvalidComponentException("Edges must have nodes from the same component.");
        }
    }

    public boolean canIncludeEdge(Edge<TNode> edge) {
        return nodes.containsAll(edge.getNodes());
    }

    public void addEdges(Set<Edge<TNode>> newEdges) throws InvalidComponentException {
        verifyEdgeSetInclusion(newEdges);
        edges.addAll(newEdges);
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

    public boolean hasNode(TNode node) {
        return nodes.contains(node);
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Component) obj;
        return Objects.equals(this.nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }

    @Override
    public String toString() {
        return "Component[" +
                "nodes=" + nodes + ']';
    }

    @Override
    public @NotNull Iterator<TNode> iterator() {
        return nodes.iterator();
    }

    @Override
    public void forEach(Consumer<? super TNode> action) {
        nodes.forEach(action);
    }

    @Override
    public Spliterator<TNode> spliterator() {
        return nodes.spliterator();
    }
}
