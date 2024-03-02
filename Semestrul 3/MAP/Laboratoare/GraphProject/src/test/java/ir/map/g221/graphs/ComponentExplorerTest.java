package ir.map.g221.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentExplorerTest {
    static ConnectedComponent<Integer> generateExample() {
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

        return component;
    }

    @Test
    void getLongestPathFrom() {
        ConnectedComponent<Integer> component = generateExample();

        /* ASCII diagram of created component :
                    -- 3 --
                   /   |   \
            1 --- 2 ---+--- 5
                   \   |   /
                    -- 4 --
         */

        var path = ComponentExplorer.getLongestPathFrom(component);
        Assertions.assertTrue(path.containsAll(component.getVertices()));
        assertEquals(1, (int) path.get(0).getData());
    }
}