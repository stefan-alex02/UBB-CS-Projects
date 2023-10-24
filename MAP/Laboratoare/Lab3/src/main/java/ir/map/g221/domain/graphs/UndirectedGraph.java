package ir.map.g221.domain.graphs;

import ir.map.g221.exceptions.graphs.InvalidGraphException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class UndirectedGraph<TNode extends Node<TNode>> implements Iterable<TNode>, GraphInterface<TNode> {
    protected final Set<TNode> nodes;
    protected final Set<Edge<TNode>> edges;
    private List<GraphComponent<TNode>> components;
    private final Map<TNode, Boolean> isVisited;

    public UndirectedGraph() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
        this.components = null;
        this.isVisited = new HashMap<>();
    }

    public UndirectedGraph(Set<TNode> nodes) {
        this.nodes = nodes;
        this.edges = new HashSet<>();
        this.components = null;

        this.isVisited = new HashMap<>();
        initialiseIsVisited();
    }

    public UndirectedGraph(Set<TNode> nodes, Iterable<Edge<TNode>> edges) {
        this.nodes = nodes;
        this.edges = new HashSet<>();
        forceAddEdges(edges);
        this.components = null;

        this.isVisited = new HashMap<>();
        initialiseIsVisited();
    }

    public Set<TNode> getNodes() {
        return nodes;
    }

    public boolean hasNode(TNode node) {
        return nodes.contains(node);
    }

    private void initialiseIsVisited() {
        for (var node: nodes) {
            isVisited.put(node, false);
        }
    }

    protected boolean isEdgeIncludable(Edge<TNode> edge) {
        return hasNode(edge.getFirst()) && hasNode(edge.getSecond());
    }

    protected boolean areEdgesIncludable(Iterable<Edge<TNode>> edges) throws InvalidGraphException {
        for (var edge: edges) {
            if (!isEdgeIncludable(edge)) {
                return false;
            }
        }
        return true;
    }

    public void forceAddEdge(Edge<TNode> edge) throws InvalidGraphException {
        if (!isEdgeIncludable(edge)) {
            throw new InvalidGraphException("Edge must have nodes in the destination (sub)graph.");
        }
        edges.add(edge);
        components = null;
    }

    public boolean tryAddEdge(Edge<TNode> edge) {
        if (!isEdgeIncludable(edge)) {
            return false;
        }
        edges.add(edge);
        components = null;
        return true;
    }

    public void forceAddEdges(Iterable<Edge<TNode>> edges) throws InvalidGraphException {
        if (!areEdgesIncludable(edges)) {
            throw new InvalidGraphException("Edges must have nodes in the destination (sub)graph.");
        }
        edges.forEach(this::forceAddEdge);
    }

    public boolean tryAddEdges(Iterable<Edge<TNode>> edges) {
        boolean success = true;
        for (var edge: edges) {
            success = tryAddEdge(edge);
        }
        return success;
    }

    public List<GraphComponent<TNode>> getAllComponents() {
        if (components == null) {
            exploreAllComponents();
        }
        return components;
    }

    private void exploreAllComponents() {
        initialiseIsVisited();
        components = new ArrayList<>();
        for (TNode node : nodes) {
            if (!isVisited.get(node)) {
                GraphComponent<TNode> newComponent = new GraphComponent<>(exploreFromNode(node));
                newComponent.tryAddEdges(edges);
                components.add(newComponent);
            }
        }
    }

    private Set<TNode> exploreFromNode(TNode node) {
        isVisited.put(node, true);
        Set<TNode> exploredNodes = new HashSet<>(Collections.singletonList(node));
        for (TNode neighbour: node.getNeighbours()) {
            if (!isVisited.get(neighbour)) {
                exploredNodes.addAll(exploreFromNode(neighbour));
            }
        }
        return exploredNodes;
    }

    public GraphComponent<TNode> getComponentWithLongestPath() {
        if (components == null) {
            exploreAllComponents();
        }
        initialiseIsVisited();
        // DO NOT REMOVE THE GREY COLOURED TYPE ARGUMENT BELOW
        GraphComponent<TNode> bestComponent = new GraphComponent<TNode>(new HashSet<>());
        for (GraphComponent<TNode> component : components) {
            if (component.getLongestPath().compareTo(bestComponent.getLongestPath()) > 0) {
                bestComponent = component;
            }
        }
        return bestComponent;
    }

    public static <TNode extends Node<TNode>> UndirectedGraph<TNode> union(UndirectedGraph<TNode> componentA, UndirectedGraph<TNode> componentB) {
        UndirectedGraph<TNode> unionGraph = new UndirectedGraph<>();
        unionGraph.nodes.addAll(componentA.nodes);
        unionGraph.nodes.addAll(componentB.nodes);
        unionGraph.forceAddEdges(componentA.edges);
        unionGraph.forceAddEdges(componentB.edges);
        return unionGraph;
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return size() == 0;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UndirectedGraph<?> that = (UndirectedGraph<?>) o;
        return Objects.equals(nodes, that.nodes) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }
}
