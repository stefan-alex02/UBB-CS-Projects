package ir.map.g221.guisocialnetwork.domain.graphs;

import ir.map.g221.guisocialnetwork.utils.graphs.Edge;
import ir.map.g221.guisocialnetwork.utils.graphs.Node;
import ir.map.g221.guisocialnetwork.utils.graphs.UndirectedGraph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UndirectedGraphTest {
    static class DummyNode implements Node<DummyNode> {
        public Set<DummyNode> neighbours;
        private final int code;

        {
            neighbours = new HashSet<>();
        }

        DummyNode(int code) {
            this.code = code;
        }

        @Override
        public Set<DummyNode> getNeighbours() {
            return neighbours;
        }

        @Override
        public Integer getDegree() {
            return 0;
        }

        @Override
        public void pairWith(DummyNode neighbour) {
            this.neighbours.add(neighbour);
            neighbour.neighbours.add(this);
        }
    }

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
}