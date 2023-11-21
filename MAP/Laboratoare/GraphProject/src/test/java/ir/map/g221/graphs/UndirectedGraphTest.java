package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UndirectedGraphTest {

    @Test
    void addVertex() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.empty();

        Assertions.assertTrue(undirectedGraph.addVertex(5));
        Assertions.assertEquals(1, undirectedGraph.size());

        Assertions.assertTrue(undirectedGraph.addVertex(7));
        Assertions.assertEquals(2, undirectedGraph.size());
    }

    @Test
    void hasVertex() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.empty();

        Assertions.assertTrue(undirectedGraph.addVertex(5));
        Assertions.assertTrue(undirectedGraph.addVertex(7));


        Assertions.assertTrue(undirectedGraph.hasVertex(5));
        Assertions.assertTrue(undirectedGraph.hasVertex(7));
        Assertions.assertFalse(undirectedGraph.hasVertex(8));
    }

    @Test
    void testAddVertices() {
        UndirectedGraph<Integer> undirectedGraph = UndirectedGraph.empty();

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
}