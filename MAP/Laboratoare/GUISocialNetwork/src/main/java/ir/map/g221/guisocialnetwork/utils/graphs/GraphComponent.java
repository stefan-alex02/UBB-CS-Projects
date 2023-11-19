package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidGraphComponentException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidNodeException;

import java.util.*;

public final class GraphComponent<TNode extends Node<TNode>> extends AbstractGraph<TNode> {
    private Path<TNode> longestPath;

    public GraphComponent() {
        super();
        this.longestPath = null;
    }

    public GraphComponent(TNode node) {
        super();
        super.addNode(node);
        this.longestPath = null;
    }

    public GraphComponent(Set<TNode> nodes) {
        super(nodes);
        assertIntegrity();
    }

    public GraphComponent(Set<TNode> nodes, Set<Edge<TNode>> edges) {
        super(nodes, edges);
        assertIntegrity();
    }

    public void assertIntegrity() throws InvalidGraphComponentException {
        if (!isEmpty() && !super.BFS(nodes.iterator().next()).containsAll(nodes)) {
            throw new InvalidGraphComponentException("Nodes must all be connected in a component.");
        }
    }

    public void expand(TNode existingNode, TNode newNode) throws InvalidNodeException {
        if (!hasNode(existingNode)) {
            throw new InvalidNodeException("Node does not belong to subject graph component.");
        }
        super.addNode(newNode);
        forceAddEdge(Edge.of(existingNode, newNode));
    }

    public static <TNode extends Node<TNode>> GraphComponent<TNode> bridge(GraphComponent<TNode> componentA,
                                                                           GraphComponent<TNode> componentB,
                                                                           Edge<TNode> bridge) {
        if (componentA.hasNode(bridge.getFirstNode()) && componentB.hasNode(bridge.getSecondNode()) ||
                componentA.hasNode(bridge.getSecondNode()) && componentB.hasNode(bridge.getFirstNode())) {
            GraphComponent<TNode> graphComponent = new GraphComponent<>();
            graphComponent.nodes.addAll(componentA.getNodes());
            graphComponent.nodes.addAll(componentB.getNodes());
            graphComponent.edges.addAll(componentA.getEdges());
            graphComponent.edges.addAll(componentB.getEdges());
            graphComponent.forceAddEdge(bridge);
            graphComponent.assertIntegrity();
            return graphComponent;
        }
        throw new InvalidEdgeException("Edge must have its ends in different graph components.");
    }

    @Override
    public void forceAddEdge(Edge<TNode> edge) throws InvalidEdgeException {
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
    public void forceAddEdges(Set<Edge<TNode>> edges) throws InvalidEdgeException {
        super.forceAddEdges(edges);
        this.longestPath = null;
    }

    @Override
    public boolean tryAddEdges(Set<Edge<TNode>> edges) {
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
        nodes.forEach(node -> {
            Path<TNode> path = getLongestPathFrom(node, new Stack<>());
            if (path.compareTo(longestPath) > 0) {
                longestPath = path;
            }
        });
    }

    private Path<TNode> getLongestPathFrom(TNode node, Stack<Edge<TNode>> exploredEdges) {
        Path<TNode> maximalPath = new Path<>();
        node.getNeighbours().forEach((TNode neighbour) -> {
            Edge<TNode> currentEdge = Edge.of(node, neighbour);
            if (!exploredEdges.contains(currentEdge)) {
                exploredEdges.push(currentEdge);
                Path<TNode> newPath = getLongestPathFrom(neighbour, exploredEdges);
                if (newPath.compareTo(maximalPath) > 0) {
                    maximalPath.setPath((newPath.getPath()));
                }
                exploredEdges.pop();
            }
        });
        maximalPath.addToBeginning(node);
        return maximalPath;
    }

    @Override
    public String toString() {
        return "Component[" +
                "nodes=" + nodes + ']';
    }
}
