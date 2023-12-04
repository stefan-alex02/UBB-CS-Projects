package ir.map.g221.guisocialnetwork.utils.generictypes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BijectionTest {
    private Bijection<Integer, String> createTestBijection() {
        Bijection<Integer, String> bijection = new Bijection<>(Set.of(
                Pair.of(1, "1"),
                Pair.of(3, "7"),
                Pair.of(5, "5"),
                Pair.of(7, "3"),
                Pair.of(11, "11")
        ));

        return bijection;
    }

    @Test
    void inverseFunction() {
        Bijection<String, Integer> expected = new Bijection<>(Set.of(
                Pair.of("1", 1),
                Pair.of("3", 7),
                Pair.of("5", 5),
                Pair.of("7", 3),
                Pair.of("11", 11)
        ));

        Bijection<String, Integer> actual = createTestBijection().inverseFunction();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getDomain() {
        Set<Integer> expected = Set.of(1,3,5,7,11);

        Set<Integer> actual = createTestBijection().getDomain();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getCodomain() {
        Set<String> expected = Set.of("1","3","5","7","11");

        Set<String> actual = createTestBijection().getCodomain();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void putPair() {
        Bijection<Integer, String> bijection = createTestBijection();

        bijection.putPair(12, "12");

        Assertions.assertTrue(bijection.getDomain().contains(12));
        Assertions.assertTrue(bijection.getCodomain().contains("12"));
    }

    @Test
    void removePairOfX() {
        Bijection<Integer, String> bijection = createTestBijection();

        Assertions.assertFalse(bijection.removePairOfX(17));

        Assertions.assertTrue(bijection.removePairOfX(7));

        Assertions.assertFalse(bijection.getDomain().contains(7));
        Assertions.assertFalse(bijection.getCodomain().contains("3"));
    }

    @Test
    void removePairOfY() {
        Bijection<Integer, String> bijection = createTestBijection();

        Assertions.assertFalse(bijection.removePairOfY("17"));

        Assertions.assertTrue(bijection.removePairOfY("7"));

        Assertions.assertFalse(bijection.getCodomain().contains("7"));
        Assertions.assertFalse(bijection.getDomain().contains(3));
    }

    @Test
    void imageOf() {
        Bijection<Integer, String> bijection = createTestBijection();

        assertEquals("3", bijection.imageOf(7));
        assertEquals("11", bijection.imageOf(11));
    }

    @Test
    void preimageOf() {
        Bijection<Integer, String> bijection = createTestBijection();

        assertEquals("7", bijection.imageOf(3));
        assertEquals("5", bijection.imageOf(5));
    }

    @Test
    void clear() {
        Bijection<Integer, String> bijection = createTestBijection();

        bijection.clear();

        Assertions.assertTrue(bijection.getDomain().isEmpty());
        Assertions.assertTrue(bijection.getCodomain().isEmpty());
    }
}