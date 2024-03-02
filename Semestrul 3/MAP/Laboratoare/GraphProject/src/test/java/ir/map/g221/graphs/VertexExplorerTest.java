package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
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

        /* ASCII Diagram of vertices connections:
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

    @Test
    void visitVerticesFrom() {
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

        /* ASCII Diagram of vertices connections:
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Set<Vertex<Integer>> vertices = Set.of(vertex1, vertex2, vertex3, vertex4, vertex5);
        Assertions.assertEquals(VertexExplorer.visitVerticesFrom(vertex1), vertices);
        Assertions.assertEquals(VertexExplorer.visitVerticesFrom(vertex2), vertices);
        Assertions.assertEquals(VertexExplorer.visitVerticesFrom(vertex5), vertices);
    }

    @Test
    void visitVerticesFromButIgnore() {
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

        /* ASCII Diagram of vertices connections:
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Assertions.assertEquals(VertexExplorer.visitVerticesFromButIgnore(vertex1, Set.of(vertex2)),
                Set.of(vertex1));
        Assertions.assertEquals(VertexExplorer.visitVerticesFromButIgnore(vertex2, Set.of(vertex2)),
                Set.of());
        Assertions.assertEquals(VertexExplorer.visitVerticesFromButIgnore(vertex3, Set.of(vertex2)),
                Set.of(vertex3, vertex4, vertex5));
        Assertions.assertEquals(VertexExplorer.visitVerticesFromButIgnore(vertex1, Set.of(vertex3, vertex5)),
                Set.of(vertex1, vertex2, vertex4));

    }

    @Test
    void createComponentsExploringFrom() {
        Vertex<Integer> vertex1 = Vertex.of(1);
        Vertex<Integer> vertex2 = Vertex.of(2);
        Vertex<Integer> vertex3 = Vertex.of(3);
        Vertex<Integer> vertex4 = Vertex.of(4);
        Vertex<Integer> vertex5 = Vertex.of(5);
        Vertex<Integer> vertex6 = Vertex.of(6);
        Vertex<Integer> vertex7 = Vertex.of(7);
        Vertex<Integer> vertex8 = Vertex.of(8);

        Vertex.connect(vertex1, vertex2);
        Vertex.connect(vertex2, vertex3);
        Vertex.connect(vertex2, vertex4);
        Vertex.connect(vertex2, vertex5);
        Vertex.connect(vertex3, vertex5);
        Vertex.connect(vertex4, vertex5);
        Vertex.connect(vertex3, vertex4);
        Vertex.connect(vertex6, vertex7);
        Vertex.connect(vertex7, vertex8);

        /* ASCII Diagram of vertices connections:
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --         6 --- 7 --- 8
         */

        var components = VertexExplorer.createComponentsExploringFrom(Set.of(vertex1));
        Assertions.assertEquals(1, components.size());
        Assertions.assertTrue(components.iterator().next().containsAllVertices(
                Set.of(vertex1.getData(),
                        vertex2.getData(),
                        vertex3.getData(),
                        vertex4.getData(),
                        vertex5.getData())));

        components = VertexExplorer.createComponentsExploringFrom(Set.of(vertex1, vertex6, vertex4, vertex8));
        Assertions.assertEquals(2, components.size());
        Assertions.assertTrue(components.stream().anyMatch(comp -> comp.containsAllVertices(
                Set.of(vertex1.getData(),
                        vertex2.getData(),
                        vertex3.getData(),
                        vertex4.getData(),
                        vertex5.getData()))));
        Assertions.assertTrue(components.stream().anyMatch(comp -> comp.containsAllVertices(
                Set.of(vertex6.getData(),
                        vertex7.getData(),
                        vertex8.getData()))));

        components = VertexExplorer.createComponentsExploringFrom(Set.of());
        Assertions.assertEquals(0, components.size());
    }

    @Test
    void getLongestPathFrom() {
        Vertex<Integer> vertex1 = Vertex.of(1);
        Vertex<Integer> vertex2 = Vertex.of(2);
        Vertex<Integer> vertex3 = Vertex.of(3);
        Vertex<Integer> vertex4 = Vertex.of(4);
        Vertex<Integer> vertex5 = Vertex.of(5);
        Vertex<Integer> vertex6 = Vertex.of(6);
        Vertex<Integer> vertex7 = Vertex.of(7);
        Vertex<Integer> vertex8 = Vertex.of(8);

        Vertex.connect(vertex1, vertex2);
        Vertex.connect(vertex2, vertex3);
        Vertex.connect(vertex2, vertex4);
        Vertex.connect(vertex2, vertex5);
        Vertex.connect(vertex3, vertex5);
        Vertex.connect(vertex4, vertex5);
        Vertex.connect(vertex3, vertex4);
        Vertex.connect(vertex6, vertex7);
        Vertex.connect(vertex7, vertex8);

        /* ASCII Diagram of vertices connections:
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --         6 --- 7 --- 8
         */

        Assertions.assertTrue(VertexExplorer.getLongestPathFrom(vertex1).containsAll(
                List.of(vertex1, vertex2, vertex3, vertex4, vertex5)));
        Assertions.assertTrue(VertexExplorer.getLongestPathFrom(vertex2).containsAll(
                List.of(vertex2, vertex3, vertex4, vertex5)));
        Assertions.assertTrue(
                VertexExplorer.getLongestPathFrom(vertex7).containsAll(List.of(vertex6, vertex7)) ||
                        VertexExplorer.getLongestPathFrom(vertex7).containsAll(List.of(vertex8, vertex7)));
    }
}