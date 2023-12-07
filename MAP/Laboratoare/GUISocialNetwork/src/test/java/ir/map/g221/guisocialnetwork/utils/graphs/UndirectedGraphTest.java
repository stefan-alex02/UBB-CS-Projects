package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.ExistingVertexException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;
import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidVertexException;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class UndirectedGraphTest {
    static UndirectedGraph<Integer> generateExample() {
        UndirectedGraph<Integer> graph = UndirectedGraph.ofEmpty();

        graph.addVertices(Set.of(1,2,3,4,5));
        Assertions.assertEquals(5, graph.numberOfComponents());

        graph.addEdge(2, 1);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        graph.addEdge(3, 4);

        Assertions.assertEquals(7, graph.numberOfEdges());
        Assertions.assertEquals(1, graph.numberOfComponents());

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        return graph;
    }

    static UndirectedGraph<Integer> generateExtendedExample() {
        UndirectedGraph<Integer> graph = generateExample();

        graph.addVertices(Set.of(6,7,8));

        graph.addEdge(6, 7);
        graph.addEdge(7, 8);

        Assertions.assertEquals(9, graph.numberOfEdges());
        Assertions.assertEquals(2, graph.numberOfComponents());

        return graph;
    }

    @Test
    void addVertex() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.ofEmpty();

        Assertions.assertTrue(undirectedGraph.addVertex(5));
        Assertions.assertEquals(1, undirectedGraph.size());

        Assertions.assertTrue(undirectedGraph.addVertex(7));
        Assertions.assertEquals(2, undirectedGraph.size());
    }

    @Test
    void hasVertex() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.ofEmpty();

        Assertions.assertTrue(undirectedGraph.addVertex(5));
        Assertions.assertTrue(undirectedGraph.addVertex(7));

        Assertions.assertTrue(undirectedGraph.containsVertex(5));
        Assertions.assertTrue(undirectedGraph.containsVertex(7));
        Assertions.assertFalse(undirectedGraph.containsVertex(8));
    }

    @Test
    void testAddVertices() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.ofEmpty();

        Assertions.assertTrue(undirectedGraph.addVertices(Set.of(5, 8, 9)));
        Assertions.assertEquals(3, undirectedGraph.size());
        Assertions.assertTrue(undirectedGraph.containsVertex(5));
        Assertions.assertFalse(undirectedGraph.containsVertex(6));
        Assertions.assertEquals(3, undirectedGraph.numberOfComponents());

        Assertions.assertTrue(undirectedGraph.addVertices(Set.of(5, 6)));
        Assertions.assertEquals(4, undirectedGraph.size());
        Assertions.assertTrue(undirectedGraph.containsVertex(5));
        Assertions.assertTrue(undirectedGraph.containsVertex(6));

        Assertions.assertFalse(undirectedGraph.addVertices(Set.of(5, 6, 9)));
        Assertions.assertEquals(4, undirectedGraph.size());
    }

    @Test
    void addEdge() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.ofEmpty();

        Assertions.assertTrue(undirectedGraph.addVertices(Set.of(5, 8, 9)));
        Assertions.assertEquals(3, undirectedGraph.numberOfComponents());

        Assertions.assertThrowsExactly(InvalidEdgeException.class,
                () -> undirectedGraph.addEdge(5, 1));
        Assertions.assertThrowsExactly(InvalidEdgeException.class,
                () -> undirectedGraph.addEdge(1, 8));

        Assertions.assertTrue(undirectedGraph.addEdge(5, 8));
        Assertions.assertEquals(2, undirectedGraph.numberOfComponents());

        Assertions.assertFalse(undirectedGraph.addEdge(5, 8));

        Assertions.assertTrue(undirectedGraph.addEdges(
                Set.of(
                        UnorderedPair.of(5,8),
                        UnorderedPair.of(5,9)))
        );
        Assertions.assertEquals(1, undirectedGraph.numberOfComponents());

        Assertions.assertFalse(undirectedGraph.addEdges(
                Set.of(
                        UnorderedPair.of(5,8),
                        UnorderedPair.of(5,9)))
        );
    }

    @Test
    void removeEdge() {
        UndirectedGraph<Integer> graph = generateExample();

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Assertions.assertThrowsExactly(InvalidEdgeException.class,
                () -> graph.removeEdge(1, 9));
        Assertions.assertFalse(graph.removeEdge(1, 5));

        Assertions.assertTrue(graph.removeEdge(2, 5));
        Assertions.assertEquals(6, graph.numberOfEdges());
        Assertions.assertEquals(1, graph.numberOfComponents());

        Assertions.assertTrue(graph.removeEdge(2, 3));
        Assertions.assertEquals(1, graph.numberOfComponents());

        Assertions.assertTrue(graph.removeEdge(2, 4));
        Assertions.assertEquals(2, graph.numberOfComponents());
        Assertions.assertTrue(graph.getComponentOf(2).orElseThrow().containsAllVertices(Set.of(1, 2)));
        Assertions.assertEquals(2, graph.getComponentOf(2).orElseThrow().size());
        Assertions.assertTrue(graph.getComponentOf(5).orElseThrow().containsAllVertices(Set.of(3, 4, 5)));
        Assertions.assertEquals(3, graph.getComponentOf(5).orElseThrow().size());

        Assertions.assertTrue(graph.removeEdge(1, 2));
        Assertions.assertEquals(3, graph.numberOfComponents());
        Assertions.assertEquals(3, graph.numberOfEdges());
    }

    @Test
    void removeVertex() {
        UndirectedGraph<Integer> graph = generateExample();

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Assertions.assertFalse(graph.removeVertex(7));

        Assertions.assertTrue(graph.removeVertex(5));
        Assertions.assertEquals(1, graph.numberOfComponents());
        Assertions.assertEquals(4, graph.numberOfEdges());
        Assertions.assertTrue(graph.getComponentOf(1).orElseThrow().containsAllVertices(Set.of(1, 2, 3, 4)));

        Assertions.assertTrue(graph.removeVertex(2));
        Assertions.assertEquals(2, graph.numberOfComponents());
        Assertions.assertEquals(1, graph.getComponentOf(1).orElseThrow().size());
        Assertions.assertTrue(graph.getComponentOf(3).orElseThrow().containsAllVertices(Set.of(3, 4)));
    }

    @Test
    void updateVertex() {
        UndirectedGraph<Integer> graph = generateExtendedExample();

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --     6 --- 7 --- 8
         */

        Assertions.assertTrue(graph.containsAllVertices(Set.of(1, 2, 3, 4, 5, 6, 7, 8)));

        Assertions.assertThrowsExactly(InvalidVertexException.class,
                () -> graph.updateVertex(9,1));

        graph.updateVertex(2,9);
        Assertions.assertTrue(graph.containsAllVertices(Set.of(1, 9, 3, 4, 5, 6, 7, 8)));
        Assertions.assertFalse(graph.containsVertex(2));

        Assertions.assertThrowsExactly(ExistingVertexException.class,
                () -> graph.updateVertex(1,6));
        Assertions.assertThrowsExactly(ExistingVertexException.class,
                () -> graph.updateVertex(4,8));

        graph.updateVertex(7,10);
        Assertions.assertTrue(graph.containsAllVertices(Set.of(1, 9, 3, 4, 5, 6, 10, 8)));
        Assertions.assertFalse(graph.containsVertex(7));
    }

    @Test
    void clear() {
        UndirectedGraph<Integer> graph = generateExtendedExample();

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --     6 --- 7 --- 8
         */

        ConnectedComponent<Integer> component1 = graph.getComponentOf(1).orElseThrow();
        Assertions.assertTrue(component1.containsAllVertices(Set.of(1,2,3,4,5)));

        ConnectedComponent<Integer> component2 = graph.getComponentOf(8).orElseThrow();
        Assertions.assertTrue(component2.containsAllVertices(Set.of(6,7,8)));

        Assertions.assertNotEquals(component1, component2);

        graph.clear();
        Assertions.assertEquals(0, graph.size());
        Assertions.assertEquals(0, graph.numberOfEdges());
        Assertions.assertEquals(0, graph.numberOfComponents());
        Assertions.assertTrue(graph.isEmpty());

        Assertions.assertTrue(component1.isEmpty());
        Assertions.assertTrue(component2.isEmpty());
    }

    @Test
    void getComponentWithLongestPath() {
        Assertions.assertTrue(UndirectedGraph.ofEmpty().getComponentWithLongestPath().getFirst().isEmpty());

        UndirectedGraph<Integer> graph = generateExtendedExample();

        /* ASCII diagram of created graph :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --     6 --- 7 --- 8
         */

        ConnectedComponent<Integer> component1 = graph.getComponentOf(1).orElseThrow();
        ConnectedComponent<Integer> component2 = graph.getComponentOf(8).orElseThrow();

        Assertions.assertEquals(component1, graph.getComponentWithLongestPath().getFirst());

        graph.removeVertex(2);
        var pair = graph.getComponentWithLongestPath();
        Assertions.assertTrue(component1.equals(pair.getFirst()) ||
                component2.equals(pair.getFirst()));
        Assertions.assertEquals(3, pair.getSecond().size());

        graph.addEdge(5, 6);
        Assertions.assertEquals(6, graph.getComponentWithLongestPath().getSecond().size());

        graph.removeEdge(3, 4);
        Assertions.assertEquals(5, graph.getComponentWithLongestPath().getSecond().size());

        graph.addEdge(1, 4);
        pair = graph.getComponentWithLongestPath();
        Assertions.assertTrue(pair.getSecond().containsAll(List.of(1,4,5,6,7,8)));
        Assertions.assertFalse(pair.getSecond().contains(3));
    }
}