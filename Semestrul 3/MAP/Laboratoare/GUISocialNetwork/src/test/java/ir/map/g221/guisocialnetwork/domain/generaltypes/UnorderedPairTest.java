package ir.map.g221.guisocialnetwork.domain.generaltypes;

import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import org.junit.jupiter.api.Test;

class UnorderedPairTest {

    @Test
    void testEquals() {
        UnorderedPair<Integer, Integer> u1 = UnorderedPair.of(12, 4);
        UnorderedPair<Integer, Integer> u2 = UnorderedPair.of(4, 12);
        UnorderedPair<Integer, Integer> u3 = UnorderedPair.of(12, 4);

        assert(u1.equals(u2));
        assert(u2.equals(u1));
        assert(u1.equals(u3));
        assert(u2.equals(u3));
    }
}