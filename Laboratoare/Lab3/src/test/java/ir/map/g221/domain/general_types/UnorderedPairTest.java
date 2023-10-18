package ir.map.g221.domain.general_types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnorderedPairTest {

    @Test
    void testEquals() {
        UnorderedPair<Integer, Integer> u1 = UnorderedPair.create(12, 4);
        UnorderedPair<Integer, Integer> u2 = UnorderedPair.create(4, 12);
        UnorderedPair<Integer, Integer> u3 = UnorderedPair.create(12, 4);

        assert(u1.equals(u2));
        assert(u2.equals(u1));
        assert(u1.equals(u3));
        assert(u2.equals(u3));
    }
}