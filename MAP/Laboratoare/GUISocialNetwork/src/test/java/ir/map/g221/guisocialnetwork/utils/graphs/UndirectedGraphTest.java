package ir.map.g221.guisocialnetwork.utils.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class UndirectedGraphTest {
    @Test
    void union() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 5; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph1 = new UndirectedGraph<>(new HashSet<>(dummies.subList(0, 3)));
        graph1.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(0), dummies.get(1)));
            add(Edge.of(dummies.get(0), dummies.get(2)));
        }});

        UndirectedGraph<DummyNode> graph2 = new UndirectedGraph<>(new HashSet<>(dummies.subList(3, 5)));
        graph2.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(3), dummies.get(4)));
        }});

        var components1 = graph1.getComponents();
        assert(components1.size() == 1);
        assert(components1.stream().anyMatch(comp -> comp.size() == 3));

        var components2 = graph2.getComponents();
        assert(components2.size() == 1);
        assert(components2.stream().anyMatch(comp -> comp.size() == 2));

        var union = UndirectedGraph.union(graph1, graph2);
        union.forceAddEdge(Edge.of(dummies.get(0), dummies.get(3)));

        var components3 = union.getComponents();
        assert(components3.size() == 1);
        assert(components3.stream().anyMatch(comp -> comp.size() == 5));
    }

    @Test
    void getAllComponentsAndLongestPath() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 7; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph = new UndirectedGraph<>(new HashSet<>(dummies));

        graph.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(0), dummies.get(2)));
            add(Edge.of(dummies.get(0), dummies.get(3)));
            add(Edge.of(dummies.get(1), dummies.get(4)));
            add(Edge.of(dummies.get(2), dummies.get(6)));
            add(Edge.of(dummies.get(6), dummies.get(0)));
        }});

        var components = graph.getComponents();
        assert(components.size() == 3);
        assert(components.stream().anyMatch(comp -> comp.size() == 4));
        assert(components.stream().anyMatch(comp -> comp.size() == 2));
        assert(components.stream().anyMatch(comp -> comp.size() == 1));

        var compWithLongestPath = graph.getComponentWithLongestPath();
        assert(compWithLongestPath.size() == 4);
        assert(compWithLongestPath.getLongestPath().length() == 4);
    }

    @Test
    void tryRemoveEdge() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph = new UndirectedGraph<>(new HashSet<>(dummies));

        graph.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
            add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));

            add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
        }});

        var components = graph.getComponents();
        assert(components.size() == 2);
        assert(components.stream().anyMatch(comp -> comp.size() == 6));
        assert(components.stream().anyMatch(comp -> comp.size() == 2));

        graph.forceRemoveEdge(Edge.of(dummies.get(5 - 1), dummies.get(4 - 1)));
        components = graph.getComponents();
        assert(components.size() == 3);
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 4 && comp.getNodes().containsAll(dummies.subList(1-1, 4-1)))
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 && comp.getNodes().containsAll(dummies.subList(5-1, 6-1)))
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 && comp.getNodes().containsAll(dummies.subList(7-1, 8-1)))
        );
    }

    @Test
    void forceAddEdge() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph = new UndirectedGraph<>(new HashSet<>(dummies));

        graph.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
            add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));

            add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
        }});

        var components = graph.getComponents();
        assert(components.size() == 2);
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 6 &&
                comp.getNodes().containsAll(dummies.subList(1-1, 6-1)) &&
                comp.getEdges().size() == 7)
        );
        assert(components.stream().anyMatch(comp -> comp.size() == 2));

        graph.forceAddEdge(Edge.of(dummies.get(2 - 1), dummies.get(3 - 1)));
        components = graph.getComponents();
        assert(components.size() == 2);
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 6 && comp.getNodes().containsAll(dummies.subList(1-1, 6-1)) &&
                comp.getEdges().size() == 8)
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 && comp.getNodes().containsAll(dummies.subList(7-1, 8-1)))
        );
    }

    @Test
    void forceRemoveNode() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph = new UndirectedGraph<>(new HashSet<>(dummies));

        graph.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
            add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));

            add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
        }});

        var components = graph.getComponents();
        assert(components.size() == 2);

        graph.forceRemoveNode(dummies.get(4 - 1));

        components = graph.getComponents();
        assert(components.size() == 3);
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

        graph.forceRemoveNode(dummies.get(8 - 1));
        components = graph.getComponents();
        assert(components.size() == 3);
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 1 &&
                comp.getNodes().contains(dummies.get(7 - 1)) &&
                comp.getEdges().isEmpty())
        );

        graph.forceRemoveNode(dummies.get(7 - 1));
        components = graph.getComponents();
        assert(components.size() == 2);
        Assertions.assertTrue(components.stream().noneMatch(comp ->
                comp.getNodes().contains(dummies.get(7 - 1)))
        );
    }

    @Test
    void updateNode() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 8; i++) {
                add(new DummyNode(i));
            }
        }};

        UndirectedGraph<DummyNode> graph = new UndirectedGraph<>(new HashSet<>(dummies));

        graph.forceAddEdges(new HashSet<>() {{
            add(Edge.of(dummies.get(1 - 1), dummies.get(2 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(3 - 1)));
            add(Edge.of(dummies.get(1 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(2 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(3 - 1), dummies.get(4 - 1)));
            add(Edge.of(dummies.get(4 - 1), dummies.get(5 - 1)));
            add(Edge.of(dummies.get(5 - 1), dummies.get(6 - 1)));

            add(Edge.of(dummies.get(7 - 1), dummies.get(8 - 1)));
        }});

        var components = graph.getComponents();
        assert(components.size() == 2);

        DummyNode newDummy = new DummyNode(10);
        graph.updateNode(dummies.get(4 - 1), newDummy);

        components = graph.getComponents();
        assert(components.size() == 2);
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 6 &&
                comp.getNodes().containsAll(dummies.subList(1-1, 3-1)) &&
                comp.getNodes().containsAll(dummies.subList(5-1, 6-1)) &&
                comp.getNodes().contains(newDummy) &&
                comp.getEdges().size() == 7)
        );
        Assertions.assertTrue(components.stream().anyMatch(comp ->
                comp.size() == 2 &&
                        comp.getNodes().containsAll(dummies.subList(7-1, 8-1)) &&
                        comp.getEdges().size() == 1)
        );
    }
}