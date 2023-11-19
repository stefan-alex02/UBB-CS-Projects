package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidNodeException;
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

    @Override
    public void updateNode(TNode oldNode, TNode newNode) {
        nodes.remove(oldNode);
        oldNode.getNeighbours().forEach(neighbour -> edges.remove(Edge.of(oldNode, neighbour)));
        GraphComponent<TNode> component = componentOf(oldNode).orElseThrow();
        components.remove(component);
        component.updateNode(oldNode, newNode);
        components.add(component);
    }

    public void forceRemoveNode(TNode node) throws InvalidNodeException {
        if (!hasNode(node)) {
            throw new InvalidNodeException("Node must belong to subject graph.");
        }
        nodes.remove(node);
        node.getNeighbours().forEach(neighbour -> edges.remove(Edge.of(node, neighbour)));
        GraphComponent<TNode> component = componentOf(node).orElseThrow();
        components.remove(component);
        Set<GraphComponent<TNode>> resultedComponents = component.breakByNode(node);
        if (resultedComponents.size() > 1) {
            components.addAll(resultedComponents);
        }
        else if(!component.isEmpty()) {
            components.add(component);
        }
    }

    public void forceAddEdge(Edge<TNode> edge) throws InvalidEdgeException {
        super.forceAddEdge(edge);
        if (!tryCombineComponents(edge)) {
            GraphComponent<TNode> component = componentOf(edge.getFirstNode()).orElseThrow();
            components.remove(component);
            component.forceAddEdge(edge);
            components.add(component);
        }
    }

    public boolean tryAddEdge(Edge<TNode> edge) {
        boolean wasAdded = super.tryAddEdge(edge);
        if (wasAdded && !tryCombineComponents(edge)) {
            GraphComponent<TNode> component = componentOf(edge.getFirstNode()).orElseThrow();
            components.remove(component);
            component.tryAddEdge(edge);
            components.add(component);
        }
        return wasAdded;
    }

    public void forceAddEdges(Set<Edge<TNode>> edges) throws InvalidEdgeException {
        super.forceAddEdges(edges);
    }

    public boolean tryAddEdges(Set<Edge<TNode>> edges) {
        return super.tryAddEdges(edges);
    }

    public void forceRemoveEdge(Edge<TNode> edge) throws InvalidEdgeException {
        if (!isEdgeIncludable(edge) || !hasEdge(edge)) {
            throw new InvalidEdgeException("Edge must exist in the subject component.");
        }
//        edge.getFirstNode().unpairWith(edge.getSecondNode());
        edges.remove(edge);

        GraphComponent<TNode> component = componentOf(edge.getFirstNode()).orElseThrow();
        components.remove(component);
        Set<GraphComponent<TNode>> resultedComponents = component.breakByEdge(edge);
        if (resultedComponents.size() > 1) {
            components.addAll(resultedComponents);
        }
        else {
            components.add(component);
        }
    }

    private boolean tryCombineComponents(Edge<TNode> edge) {
        GraphComponent<TNode> firstComponent = componentOf(edge.getFirstNode()).orElseThrow();
        GraphComponent<TNode> secondComponent = componentOf(edge.getSecondNode()).orElseThrow();
        if (!firstComponent.equals(secondComponent)) {
            components.remove(firstComponent);
            components.remove(secondComponent);
            components.add(GraphComponent.bridge(firstComponent, secondComponent, edge));
            return true;
        }
        return false;
    }

    public void reset() {
        super.reset();
        this.components.clear();
    }

    public Set<GraphComponent<TNode>> getComponents() {
        return components;
    }

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
}
