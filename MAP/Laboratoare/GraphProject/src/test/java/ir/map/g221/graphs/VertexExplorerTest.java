package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class VertexExplorerTest {

    @Test
    void createComponentExploringFrom() {
        Vertex<Integer> vertex1 = Vertex.of(1);
        Vertex<Integer> vertex2 = Vertex.of(2);
        Vertex<Integer> vertex3 = Vertex.of(3);
        Vertex<Integer> vertex4 = Vertex.of(4);
        Vertex<Integer> vertex5 = Vertex.of(5);

        Vertex.connect(vertex1, vertex2);
        Vertex.connect(vertex2, vertex3);
        Vertex.connect(vertex2, vertex4);
        Vertex.connect(vertex2, vertex5);
        Vertex.connect(vertex3, vertex5);
        Vertex.connect(vertex4, vertex5);
        Vertex.connect(vertex3, vertex4);

        /*
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        ConnectedComponent<Integer> component1 = VertexExplorer.createComponentExploringFrom(vertex1);
        Assertions.assertEquals(5, component1.size());
        Assertions.assertEquals(7, component1.numberOfEdges());
        IntStream.range(1, 6).forEach(number -> Assertions.assertTrue(component1.containsVertex(number)));

        ConnectedComponent<Integer> component2 = VertexExplorer.createComponentExploringFrom(vertex2);
        Assertions.assertEquals(component1, component2);
    }
}