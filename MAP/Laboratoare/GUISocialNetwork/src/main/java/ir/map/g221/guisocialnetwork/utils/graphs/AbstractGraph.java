package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractGraph<TNode extends Node<TNode>> implements Graph<TNode> {
    protected final Set<TNode> nodes;
    protected final Set<Edge<TNode>> edges;

    public AbstractGraph() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public AbstractGraph(Set<TNode> nodes) {
        this.nodes = nodes;
        this.edges = new HashSet<>();
    }

    public AbstractGraph(Set<TNode> nodes, Set<Edge<TNode>> edges) {
        this.nodes = nodes;
        this.edges = new HashSet<>();
        forceAddEdges(edges);
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public Set<TNode> getNodes() {
        return nodes;
    }

    public boolean hasNode(TNode node) {
        return nodes.contains(node);
    }

    /**
     * Adds a node, if it doesn't belong to the Node set already.
     * @param node the new node to be added.
     */
    protected void addNode(TNode node) {
        nodes.add(node);
    }

    public boolean hasEdge(Edge<TNode> edge) {
        return edges.contains(edge);
    }

    public Set<Edge<TNode>> getEdges() {
        return edges;
    }

    protected boolean isEdgeIncludable(Edge<TNode> edge) {
        return hasNode(edge.getFirstNode()) && hasNode(edge.getSecondNode());
    }

    protected boolean areEdgesIncludable(Set<Edge<TNode>> edges) {
        return edges.stream()
                .allMatch(this::isEdgeIncludable);
    }

    public void forceAddEdge(Edge<TNode> edge) throws InvalidEdgeException {
        TNode node1 = nodes.stream()
                .filter(n -> n.equals(edge.getFirstNode()))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidEdgeException("Edge must have nodes in the subject graph.")
                );
        TNode node2 = nodes.stream()
                .filter(n -> n.equals(edge.getSecondNode()))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidEdgeException("Edge must have nodes in the subject graph.")
                );
        node1.pairWith(node2);
        edges.add(Edge.of(node1, node2));
    }

    public boolean tryAddEdge(Edge<TNode> edge) {
        TNode node1 = nodes.stream()
                .filter(n -> n.equals(edge.getFirstNode()))
                .findFirst()
                .orElse(null);
        TNode node2 = nodes.stream()
                .filter(n -> n.equals(edge.getSecondNode()))
                .findFirst()
                .orElse(null);

        if (node1 == null || node2 == null) {
            return false;
        }

        node1.pairWith(node2);
        edges.add(Edge.of(node1, node2));
        return true;
    }

    public void forceAddEdges(Set<Edge<TNode>> edges) throws InvalidEdgeException {
        if (!areEdgesIncludable(edges)) {
            throw new InvalidEdgeException("Edges must have nodes in the destination graph.");
        }
        edges.forEach(this::forceAddEdge);
    }

    public boolean tryAddEdges(Set<Edge<TNode>> edges) {
        return edges.stream()
                .allMatch(this::tryAddEdge);
    }

    public Set<TNode> BFS(TNode sourceNode) {
        GraphVisitCounter<TNode> visitCounter = new GraphVisitCounter<>(this);
        Queue<TNode> queue = new LinkedList<>();
        queue.add(sourceNode);
        visitCounter.setVisited(sourceNode);
        while(!queue.isEmpty()) {
            TNode node = queue.poll();
            node.getNeighbours().stream()
                    .filter(Predicate.not(visitCounter::isVisited))
                    .forEach(n -> {
                        queue.add(n);
                        visitCounter.setVisited(n);
                    });
        }
        return visitCounter.getVisitedNodes();
    }

    public void reset() {
        this.nodes.clear();
        this.edges.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractGraph<?> that = (AbstractGraph<?>) o;
        return Objects.equals(nodes, that.nodes) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }
}
