package ir.map.g221.guisocialnetwork.utils.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphComponentTest {

    @Test
    void breakByEdge() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 6; i++) {
                add(new DummyNode(i));
            }
        }};

        GraphComponent<DummyNode> graph = new GraphComponent<>(new HashSet<>(dummies),
                new HashSet<>() {{
                    add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
                    add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));
        }});

        var components = graph.breakByEdge(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));

        Assertions.assertEquals(2, components.size());
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 4 && comp.getNodes().containsAll(dummies.subList(1-1, 4-1)))
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 && comp.getNodes().containsAll(dummies.subList(5-1, 6-1)))
        );
    }

    @Test
    void breakByNode() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        GraphComponent<DummyNode> graph = new GraphComponent<>(new HashSet<>(dummies),
                new HashSet<>() {{
                    add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
                    add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));
                    add(Edge.of(dummies.get(4 - 1), dummies.get(8 - 1)));
                    add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
                }});

        var components = graph.breakByNode(dummies.get(4 - 1));

        Assertions.assertEquals(3, components.size());
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 3 &&
                comp.getNodes().containsAll(dummies.subList(1-1, 3-1)) &&
                comp.getEdges().size() == 2)
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 &&
                comp.getNodes().containsAll(dummies.subList(5-1, 6-1)) &&
                comp.getEdges().size() == 1)
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 &&
                        comp.getNodes().containsAll(dummies.subList(7-1, 8-1)) &&
                        comp.getEdges().size() == 1)
        );
    }

    @Test
    void updateNode() {

        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        GraphComponent<DummyNode> graph = new GraphComponent<>(new HashSet<>(dummies),
                new HashSet<>() {{
                    add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
                    add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
                    add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
                    add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));
                    add(Edge.of(dummies.get(4 - 1), dummies.get(8 - 1)));
                    add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
                }});

        Set<DummyNode> oldNeighbours = Set.copyOf(dummies.get(4 - 1).getNeighbours());

        DummyNode newDummy = new DummyNode(10);
        graph.updateNode(dummies.get(4 - 1), newDummy);

        Assertions.assertEquals(oldNeighbours, newDummy.getNeighbours());
    }
}