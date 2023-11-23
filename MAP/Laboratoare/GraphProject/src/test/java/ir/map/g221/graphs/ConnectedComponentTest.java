package ir.map.g221.graphs;

import ir.map.g221.graphexceptions.InvalidComponentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ConnectedComponentTest {

    @Test
    void size() {
        Graph<Integer> graphComponent = ConnectedComponent.ofVertex(5);

        Assertions.assertEquals(1, graphComponent.size());
    }

    @Test
    void hasVertex() {
        Graph<Integer> graphComponent = ConnectedComponent.ofVertex(5);

        Assertions.assertTrue(graphComponent.containsVertex(5));
        Assertions.assertFalse(graphComponent.containsVertex(7));
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
        Assertions.assertTrue(graphComponentE.containsVertex(7));
    }

    @Test
    void removeEdge() {
        ConnectedComponent<Integer> graphComponent = ConnectedComponent.ofVertex(5);
        graphComponent.expand(5, 6);
        graphComponent.expand(5, 7);
        graphComponent.expand(7, 8);

        Assertions.assertThrowsExactly(InvalidComponentException.class, () -> graphComponent.removeEdge(5, 7));
        Assertions.assertThrowsExactly(InvalidComponentException.class, () -> graphComponent.removeEdge(5, 6));
        Assertions.assertFalse(graphComponent.removeEdge(5, 8));
        Assertions.assertEquals(4, graphComponent.size());
        Assertions.assertEquals(3, graphComponent.numberOfEdges());

        graphComponent.addEdge(6, 8);
        Assertions.assertEquals(4, graphComponent.numberOfEdges());

        Assertions.assertTrue(graphComponent.removeEdge(5, 7));
        Assertions.assertEquals(3, graphComponent.numberOfEdges());
    }

    @Test
    void testRemoveEdge() {
        ConnectedComponent<Integer> componentA = ConnectedComponent.ofVertex(2);
        componentA.expand(2, 1);
        componentA.expand(2, 3);
        componentA.expand(2, 4);
        componentA.expand(2, 5);

        componentA.addEdge(3, 5);
        componentA.addEdge(4, 5);
        componentA.addEdge(3, 4);

        /* ASCII diagram of created component A :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Assertions.assertEquals(7, componentA.numberOfEdges());

        Assertions.assertTrue(ConnectedComponent.removeEdge(componentA, 2, 3).isEmpty());
        Assertions.assertTrue(ConnectedComponent.removeEdge(componentA, 2, 3).isEmpty());

        Assertions.assertTrue(ConnectedComponent.removeEdge(componentA, 2, 4).isEmpty());
        Assertions.assertEquals(5, componentA.numberOfEdges());

        var components = ConnectedComponent.removeEdge(componentA, 1, 2);
        Assertions.assertTrue(components.isPresent());

        Assertions.assertEquals(1, components.get().getFirst().size());
        Assertions.assertEquals(0, components.get().getFirst().numberOfEdges());
        Assertions.assertTrue(components.get().getFirst().containsVertex(1));

        Assertions.assertEquals(4, components.get().getSecond().size());
        Assertions.assertEquals(4, components.get().getSecond().numberOfEdges());
        Assertions.assertTrue(components.get().getSecond().containsAllVertices(Set.of(2,3,4,5)));

        ConnectedComponent<Integer> componentB = components.get().getSecond();
        Assertions.assertTrue(ConnectedComponent.removeEdge(componentB, 3, 4).isEmpty());
        componentB.expand(4, 6);

        Assertions.assertEquals(5, componentB.size());
        Assertions.assertEquals(4, componentB.numberOfEdges());

        /* ASCII diagram of obtained component B :
                  3 --
                      \
                 2 --- 5
                      /
            6 --- 4 --
         */

        components = ConnectedComponent.removeEdge(componentB, 4, 5);
        Assertions.assertTrue(components.isPresent());

        Assertions.assertEquals(2, components.get().getFirst().size());
        Assertions.assertEquals(1, components.get().getFirst().numberOfEdges());
        Assertions.assertTrue(components.get().getFirst().containsAllVertices(Set.of(4, 6)));

        Assertions.assertEquals(3, components.get().getSecond().size());
        Assertions.assertEquals(2, components.get().getSecond().numberOfEdges());
        Assertions.assertTrue(components.get().getSecond().containsAllVertices(Set.of(2, 5, 3)));
    }

    @Test
    void containsAllVertices() {
        ConnectedComponent<Integer> component = ConnectedComponent.ofVertex(2);
        component.expand(2, 1);
        component.expand(2, 3);
        component.expand(2, 4);
        component.expand(2, 5);

        component.addEdge(3, 5);
        component.addEdge(4, 5);
        component.addEdge(3, 4);

        /* ASCII diagram of created component :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        Assertions.assertTrue(component.containsAllVertices(Set.of(1)));
        Assertions.assertTrue(component.containsAllVertices(Set.of(1,2,3)));
        Assertions.assertTrue(component.containsAllVertices(Set.of(1,2,3,4,5)));
        Assertions.assertFalse(component.containsAllVertices(Set.of(6)));
        Assertions.assertFalse(component.containsAllVertices(Set.of(1,2,3,4,5,6)));
    }
}