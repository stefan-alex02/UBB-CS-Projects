package ir.map.g221.domain;

import ir.map.g221.domain.general_types.UnorderedPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnorderedGraph<TNode> {
    private final Integer nodeCount;
    private final Boolean[][] matrix;
    private Boolean[] isVisited;
    private List<List<TNode>> components;
    private final Map<TNode, Integer> nodeToIndexMap;
    private final Map<Integer, TNode> indexToNodeMap;

    public UnorderedGraph(int noOfNodes, Iterable<TNode> nodeSet) {
        nodeCount = noOfNodes;
        matrix = new Boolean[noOfNodes][noOfNodes];
        isVisited = new Boolean[noOfNodes];
        nodeToIndexMap = new HashMap<>();
        indexToNodeMap = new HashMap<>();
        generateMaps(nodeSet);
        components = new ArrayList<>();
    }

    private void generateMaps(Iterable<TNode> nodeSet) {
        int index = 0;
        for (TNode node: nodeSet) {
            this.nodeToIndexMap.put(node, index);
            this.indexToNodeMap.put(index, node);
            index++;
        }
    }

    public void generateEdges(List<UnorderedPair<TNode, TNode>> pairList) {
        for (var pair: pairList) {
            setEdge(pair.getFirst(), pair.getSecond());
        }
    }

    public void setEdge(TNode node1, TNode node2) {
        Integer index1 = nodeToIndexMap.get(node1);
        Integer index2 = nodeToIndexMap.get(node2);
        matrix[index1][index2] = matrix[index2][index1] = true;
    }

    public List<List<TNode>> getAllComponents() {
        if (components.isEmpty()) {
            exploreAllComponents();
        }
        return components;
    }

    private void exploreAllComponents() {
        isVisited = new Boolean[nodeCount];
        components = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            if (!isVisited[i]) {
                components.add(exploreFromNode(i));
            }
        }
    }

    private List<TNode> exploreFromNode(Integer nodeIndex) {
        isVisited[nodeIndex] = true;
        List<TNode> exploredNodes = new ArrayList<>();
        exploredNodes.add(indexToNodeMap.get(nodeIndex));
        for (int i = 0; i < nodeCount; i++) {
            if (!isVisited[i]) {
                exploredNodes.addAll(exploreFromNode(i));
            }
        }
        return exploredNodes;
    }
}
