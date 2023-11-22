package ir.map.g221.graphs;

import ir.map.g221.generictypes.UnorderedPair;
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


        Assertions.assertTrue(undirectedGraph.hasVertex(5));
        Assertions.assertTrue(undirectedGraph.hasVertex(7));
        Assertions.assertFalse(undirectedGraph.hasVertex(8));
    }

    @Test
    void testAddVertices() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.ofEmpty();

        Assertions.assertTrue(undirectedGraph.addVertices(Set.of(5, 8, 9)));
        Assertions.assertEquals(3, undirectedGraph.size());
        Assertions.assertTrue(undirectedGraph.hasVertex(5));
        Assertions.assertFalse(undirectedGraph.hasVertex(6));

        Assertions.assertTrue(undirectedGraph.addVertices(Set.of(5, 6)));
        Assertions.assertEquals(4, undirectedGraph.size());
        Assertions.assertTrue(undirectedGraph.hasVertex(5));
        Assertions.assertTrue(undirectedGraph.hasVertex(6));

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
}