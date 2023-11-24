package ir.map.g221.graphs;

import ir.map.g221.generictypes.UnorderedPair;
import ir.map.g221.graphexceptions.InvalidVertexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class UndirectedGraphTest {

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

        Assertions.assertThrowsExactly(InvalidVertexException.class,
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
}