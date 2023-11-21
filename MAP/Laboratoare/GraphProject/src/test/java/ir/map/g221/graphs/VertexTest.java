package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VertexTest {

    @Test
    void testEquals() {
        Vertex<Integer> vertex1 = Vertex.of(6);
        Vertex<Integer> vertex2 = Vertex.of(6);
        Vertex<Integer> vertex3 = Vertex.of(7);

        Assertions.assertEquals(vertex1, 6);
        Assertions.assertNotEquals(vertex1, 8);
        Assertions.assertEquals(vertex1, vertex2);
        Assertions.assertNotEquals(vertex1, vertex3);
    }
}