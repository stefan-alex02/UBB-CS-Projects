package ir.map.g221.domain;

import ir.map.g221.domain.general_types.UnorderedPair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class UnorderedGraphTest {

    @Test
    void getAllComponents() {
        UnorderedGraph<String> graph = new UnorderedGraph<>(7,
                new ArrayList<>() {{
                    add("n1");
                    add("n2");
                    add("n3");
                    add("n4");
                    add("n5");
                    add("n6");
                    add("n7");
                }});

        graph.generateEdges(new ArrayList<>() {{
            add(UnorderedPair.create("n1", "n3"));
            add(UnorderedPair.create("n1", "n4"));
            add(UnorderedPair.create("n2", "n5"));
            add(UnorderedPair.create("n3", "n7"));
            add(UnorderedPair.create("n7", "n1"));
        }});

        var compoenents = graph.getAllComponents();
        assert(compoenents.size() == 3);
        assert(compoenents.stream().anyMatch(comp -> comp.size() == 4));
        assert(compoenents.stream().anyMatch(comp -> comp.size() == 2));
        assert(compoenents.stream().anyMatch(comp -> comp.size() == 1));
    }
}