package ir.map.g221.domain.graphs;

import ir.map.g221.domain.general_types.Bijection;
import ir.map.g221.domain.general_types.UnorderedPair;

import java.util.*;

public class UnorderedGraph<TNode extends Node<TNode>> {
    private final Integer nodeCount;
    private final Set<Edge<TNode>> edges;
    private final Boolean[] isVisited;
    private List<Component<TNode>> components;
    private final Bijection<TNode, Integer> nodeIndexBijection;
    private final Set<TNode> nodeSet;

    public UnorderedGraph(Iterable<TNode> nodes) {
        nodeSet = new HashSet<>((Collection<TNode>) nodes);
        nodeCount = nodeSet.size();

        isVisited = new Boolean[nodeCount];
        initialiseIsVisited();

        nodeIndexBijection = new Bijection<>();
        generateBijection(nodes);

        edges = new HashSet<>();
        components = new ArrayList<>();
    }

    private void initialiseIsVisited() {
        Arrays.fill(isVisited, false);
    }

    private void generateBijection(Iterable<TNode> nodeSet) {
        int index = 0;
        for (TNode node: nodeSet) {
            nodeIndexBijection.addPair(node, index++);
        }
    }

    public void generateEdges(List<UnorderedPair<TNode, TNode>> pairList) {
        for (var pair: pairList) {
            setEdge(pair.getFirst(), pair.getSecond());
        }
    }

    public void setEdge(TNode node1, TNode node2) {
        edges.add(Edge.of(node1, node2));
    }

    public List<Component<TNode>> getAllComponents() {
        if (components.isEmpty()) {
            exploreAllComponents();
        }
        return components;
    }

    private void exploreAllComponents() {
        initialiseIsVisited();
        components = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            if (!isVisited[i]) {
                Component<TNode> newComponent = new Component<>(exploreFromNode(i));
                newComponent.addEdges(
                        new HashSet<>(edges.stream()
                        .filter(newComponent::canIncludeEdge)
                        .toList())
                );
                components.add(newComponent);
            }
        }
    }

    private Set<TNode> exploreFromNode(Integer nodeIndex) {
        isVisited[nodeIndex] = true;
        Set<TNode> exploredNodes = new HashSet<>();
        TNode node = nodeIndexBijection.preimageOf(nodeIndex);
        exploredNodes.add(node);
        for (TNode neighbour: node.getNeighbours()) {
            Integer indexOfNeighbour = nodeIndexBijection.imageOf(neighbour);
            if (!isVisited[indexOfNeighbour]) {
                exploredNodes.addAll(exploreFromNode(indexOfNeighbour));
            }
        }
        return exploredNodes;
    }

    public Component<TNode> getComponentWithLongestPath() {
        if (components.isEmpty()) {
            exploreAllComponents();
        }
        initialiseIsVisited();
        Component<TNode> bestComponent = new Component<>(new HashSet<>());
        for (Component<TNode> component : components) {
            if (component.getLongestPath().compareTo(bestComponent.getLongestPath()) > 0) {
                bestComponent = component;
            }
        }
        return bestComponent;
    }

    private Component<TNode> getComponent(TNode node) {
        return components.stream()
                .filter(component -> component.hasNode(node))
                .findFirst()
                .orElse(null);
    }
}
