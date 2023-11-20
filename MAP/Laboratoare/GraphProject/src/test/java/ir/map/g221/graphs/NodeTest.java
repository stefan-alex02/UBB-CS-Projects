package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void testEquals() {
        Node<Integer> node1 = Node.of(6);
        Node<Integer> node2 = Node.of(6);
        Node<Integer> node3 = Node.of(7);

        Assertions.assertEquals(node1, 6);
        Assertions.assertNotEquals(node1, 8);
        Assertions.assertEquals(node1, node2);
        Assertions.assertNotEquals(node1, node3);
    }
}