package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConnectedComponentTest {

    @Test
    void size() {
        Graph<Integer> graphComponent = ConnectedComponent.ofVertex(5);

        Assertions.assertEquals(1, graphComponent.size());
    }

    @Test
    void hasVertex() {
        Graph<Integer> graphComponent = ConnectedComponent.ofVertex(5);

        Assertions.assertTrue(graphComponent.hasVertex(5));
        Assertions.assertFalse(graphComponent.hasVertex(7));
    }

    @Test
    void testEquals() {
        Graph<Integer> graphComponentA = ConnectedComponent.ofVertex(5);
        Graph<Integer> graphComponentB = ConnectedComponent.ofVertex(5);
        Graph<Integer> graphComponentC = ConnectedComponent.ofVertex(7);

        Assertions.assertEquals(graphComponentA, graphComponentA);
        Assertions.assertEquals(graphComponentA, graphComponentB);
        Assertions.assertNotEquals(graphComponentA, graphComponentC);
    }

    @Test
    void ofConnection() {
        ConnectedComponent<Integer> graphComponentA = ConnectedComponent.ofVertex(5);
        ConnectedComponent<Integer> graphComponentB = ConnectedComponent.ofVertex(6);

        ConnectedComponent<Integer> graphComponentC = ConnectedComponent.ofConnection(graphComponentA, graphComponentB, 5, 6);
        Assertions.assertEquals(2, graphComponentC.size());
        Assertions.assertEquals(1, graphComponentC.numberOfEdges());

        ConnectedComponent<Integer> graphComponentD = ConnectedComponent.ofVertex(7);
        ConnectedComponent<Integer> graphComponentE = ConnectedComponent.ofConnection(graphComponentD, graphComponentC, 7, 5);
        Assertions.assertEquals(3, graphComponentE.size());
        Assertions.assertEquals(2, graphComponentE.numberOfEdges());
        Assertions.assertTrue(graphComponentE.hasVertex(7));
    }
}