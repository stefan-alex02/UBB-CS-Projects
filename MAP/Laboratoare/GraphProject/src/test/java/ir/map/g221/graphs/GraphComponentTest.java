package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphComponentTest {

    @Test
    void size() {
        Graph<Integer> graphComponent = GraphComponent.ofVertex(5);

        Assertions.assertEquals(1, graphComponent.size());
    }

    @Test
    void hasVertex() {
        Graph<Integer> graphComponent = GraphComponent.ofVertex(5);

        Assertions.assertTrue(graphComponent.hasVertex(5));
        Assertions.assertFalse(graphComponent.hasVertex(7));
    }
}