package ir.map.g221.domain;

import java.util.Map;

public class Graph<TNode> {
    private Boolean[][] matrix;
    private Integer[] componentIndex;
    private Map<TNode, Integer> nodeMap;

    public Graph(int noOfNodes, Iterable<TNode> nodeSet) {
        matrix = new Boolean[noOfNodes][noOfNodes];
        componentIndex = new Integer[noOfNodes];
        createMapFromSet(nodeSet);
    }

    private void createMapFromSet(Iterable<TNode> nodeSet) {
        int index = 0;
        for (TNode node: nodeSet) {
            this.nodeMap.put(node, index);
            index++;
        }
    }

    public void setEdge(TNode node1, TNode node2) {
        Integer index1 = nodeMap.get(node1);
        Integer index2 = nodeMap.get(node2);
        matrix[index1][index2] = matrix[index2][index1] = true;
    }

    public void explore() {

    }
}
