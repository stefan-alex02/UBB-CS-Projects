package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class UndirectedGraph<TNode extends Node<TNode>> extends AbstractGraph<TNode> implements Iterable<TNode> {
    private final Set<GraphComponent<TNode>> components;

    public UndirectedGraph() {
        super();
        this.components = new HashSet<>();
    }

    public UndirectedGraph(Set<TNode> nodes) {
        super();
        this.components = new HashSet<>();
        addNodes(nodes);
    }

    public UndirectedGraph(Set<TNode> nodes, Set<Edge<TNode>> edges) {
        super();
        this.components = new HashSet<>();
        addNodes(nodes);
        forceAddEdges(edges);
    }

    public static <TNode extends Node<TNode>> UndirectedGraph<TNode> ofAbstractGraph(
            AbstractGraph<TNode> abstractGraph) {
        return new UndirectedGraph<>(abstractGraph.nodes, abstractGraph.edges);
    }

    public GraphComponent<TNode> toComponent() {
        return new GraphComponent<>(nodes, edges);
    }

    public void addNodes(Collection<TNode> nodes) {
        nodes.forEach(this::addNode);
    }

    public void addNode(TNode node) {
        if (!nodes.contains(node)) {
            components.add(new GraphComponent<>(node));
        }
        super.addNode(node);
    }

    public void forceAddEdge(Edge<TNode> edge) throws InvalidEdgeException {
        super.forceAddEdge(edge);
        tryCombineComponents(edge);
    }

    public boolean tryAddEdge(Edge<TNode> edge) {
        boolean wasAdded = super.tryAddEdge(edge);
        if (wasAdded) {
            tryCombineComponents(edge);
        }
        return wasAdded;
    }

    public void forceAddEdges(Set<Edge<TNode>> edges) throws InvalidEdgeException {
        super.forceAddEdges(edges);
    }

    public boolean tryAddEdges(Set<Edge<TNode>> edges) {
        return super.tryAddEdges(edges);
    }

    private void tryCombineComponents(Edge<TNode> edge) {
        GraphComponent<TNode> firstComponent = componentOf(edge.getFirstNode()).orElseThrow();
        GraphComponent<TNode> secondComponent = componentOf(edge.getSecondNode()).orElseThrow();
        if (!firstComponent.equals(secondComponent)) {
            components.remove(firstComponent);
            components.remove(secondComponent);
            components.add(GraphComponent.bridge(firstComponent, secondComponent, edge));
        }
    }

    public void reset() {
        super.reset();
        this.components.clear();
    }

    public Set<GraphComponent<TNode>> getComponents() {
        return components;
    }

//    private void exploreAllComponents() {
//        initialiseIsVisited();
//        components.clear();
//        nodes.stream()
//                .filter(Predicate.not(super::isVisited))
//                .forEach((TNode node) -> {
//                    GraphComponent<TNode> newComponent = new GraphComponent<>(exploreFromNode(node));
//                    newComponent.tryAddEdges(edges);
//                    components.add(newComponent);
//                });
//    }
//
//    private Set<TNode> exploreFromNode(TNode node) {
//        setVisited(node);
//        Set<TNode> exploredNodes = new HashSet<>(Collections.singletonList(node));
//        node.getNeighbours()
//                .stream()
//                .filter(Predicate.not(super::isVisited))
//                .forEach((TNode neighbour) ->
//                        exploredNodes.addAll(exploreFromNode(neighbour))
//                );
//        return exploredNodes;
//    }

    public GraphComponent<TNode> getComponentWithLongestPath() {
        return components.stream()
                .max(Comparator.comparing(
                        GraphComponent::getLongestPath)
                )
                .orElse(new GraphComponent<>());
    }

    public static <TNode extends Node<TNode>> UndirectedGraph<TNode> union(UndirectedGraph<TNode> componentA,
                                                                           UndirectedGraph<TNode> componentB) {
        UndirectedGraph<TNode> unionGraph = new UndirectedGraph<>();
        unionGraph.nodes.addAll(componentA.nodes);
        unionGraph.nodes.addAll(componentB.nodes);
        unionGraph.edges.addAll(componentA.edges);
        unionGraph.edges.addAll(componentB.edges);
        unionGraph.components.addAll(componentA.components);
        unionGraph.components.addAll(componentB.components);
//        unionGraph.addNodes(componentA.nodes);
//        unionGraph.addNodes(componentB.nodes);
//        unionGraph.forceAddEdges(componentA.edges);
//        unionGraph.forceAddEdges(componentB.edges);
        return unionGraph;
    }

    public Optional<GraphComponent<TNode>> componentOf(TNode node) {
        return components.stream()
                .filter(component -> component.hasNode(node))
                .findAny();
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
