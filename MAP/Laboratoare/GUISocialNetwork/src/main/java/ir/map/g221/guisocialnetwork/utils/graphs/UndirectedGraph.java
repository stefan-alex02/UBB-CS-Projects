package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidGraphException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
        nodes.forEach(node -> isVisited.put(node, false));
    }

    protected boolean isEdgeIncludable(Edge<TNode> edge) {
        return hasNode(edge.getFirst()) && hasNode(edge.getSecond());
    }

    protected boolean areEdgesIncludable(Iterable<Edge<TNode>> edges) throws InvalidGraphException {
        return ObjectTransformer
                .iterableToCollection(edges)
                .stream()
                .allMatch(this::isEdgeIncludable);
    }

    public void forceAddEdge(Edge<TNode> edge) throws InvalidGraphException {
        TNode node1 = nodes.stream()
                .filter(n -> n.equals(edge.getFirst()))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidGraphException("Edge must have nodes in the destination (sub)graph.")
                );
        TNode node2 = nodes.stream()
                .filter(n -> n.equals(edge.getSecond()))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidGraphException("Edge must have nodes in the destination (sub)graph.")
                );
        node1.pairWith(node2);
        edges.add(Edge.of(node1, node2));
        components = null;
    }

    public boolean tryAddEdge(Edge<TNode> edge) {
        TNode node1 = nodes.stream()
                .filter(n -> n.equals(edge.getFirst()))
                .findFirst()
                .orElse(null);
        TNode node2 = nodes.stream()
                .filter(n -> n.equals(edge.getSecond()))
                .findFirst()
                .orElse(null);

        if (node1 == null || node2 == null) {
            return false;
        }

        node1.pairWith(node2);
        edges.add(Edge.of(node1, node2));
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
        return ObjectTransformer
                .iterableToCollection(edges)
                .stream()
                .allMatch(this::tryAddEdge);
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
        nodes.stream()
                .filter(Predicate.not(isVisited::get))
                .forEach((TNode node) -> {
                    GraphComponent<TNode> newComponent = new GraphComponent<>(exploreFromNode(node));
                    newComponent.tryAddEdges(edges);
                    components.add(newComponent);
                });
    }

    private Set<TNode> exploreFromNode(TNode node) {
        isVisited.put(node, true);
        Set<TNode> exploredNodes = new HashSet<>(Collections.singletonList(node));
        node.getNeighbours()
                .stream()
                .filter(Predicate.not(isVisited::get))
                .forEach((TNode neighbour) ->
                        exploredNodes.addAll(exploreFromNode(neighbour))
                );
        return exploredNodes;
    }

    public GraphComponent<TNode> getComponentWithLongestPath() {
        if (components == null) {
            exploreAllComponents();
        }
        initialiseIsVisited();

        return components.stream()
                .max(Comparator.comparing(
                        GraphComponent::getLongestPath)
                )
                .orElse(new GraphComponent<>());
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
