package ir.map.g221.domain;

import ir.map.g221.domain.graphs.Node;
import ir.map.g221.domain.graphs.UnorderedGraph;
import ir.map.g221.domain.general_types.UnorderedPair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UnorderedGraphTest {
    static class DummyNode implements Node<DummyNode> {
        public Set<DummyNode> neighbours;
        private int code;

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
    }

    @Test
    void getAllComponentsAndLongestPath() {
        List<DummyNode> dummies = new ArrayList<>() {{
            for (int i = 1; i <= 7; i++) {
                add(new DummyNode(i));
            }
        }};
        dummies.get(0).neighbours.add(dummies.get(2));
        dummies.get(2).neighbours.add(dummies.get(0));

        dummies.get(0).neighbours.add(dummies.get(3));
        dummies.get(3).neighbours.add(dummies.get(0));

        dummies.get(0).neighbours.add(dummies.get(6));
        dummies.get(6).neighbours.add(dummies.get(0));

        dummies.get(2).neighbours.add(dummies.get(6));
        dummies.get(6).neighbours.add(dummies.get(2));

        dummies.get(1).neighbours.add(dummies.get(4));
        dummies.get(4).neighbours.add(dummies.get(1));

        UnorderedGraph<DummyNode> graph = new UnorderedGraph<>(dummies);

        graph.generateEdges(new ArrayList<>() {{
            add(UnorderedPair.of(dummies.get(0), dummies.get(2)));
            add(UnorderedPair.of(dummies.get(0), dummies.get(3)));
            add(UnorderedPair.of(dummies.get(1), dummies.get(4)));
            add(UnorderedPair.of(dummies.get(2), dummies.get(6)));
            add(UnorderedPair.of(dummies.get(6), dummies.get(0)));
        }});

        var components = graph.getAllComponents();
        assert(components.size() == 3);
        assert(components.stream().anyMatch(comp -> comp.size() == 4));
        assert(components.stream().anyMatch(comp -> comp.size() == 2));
        assert(components.stream().anyMatch(comp -> comp.size() == 1));

        var compWithLongestPath = graph.getComponentWithLongestPath();
        assert(compWithLongestPath.size() == 4);
        assert(compWithLongestPath.getLongestPath().length() == 4);
    }
}