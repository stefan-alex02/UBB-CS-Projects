package ir.map.g221.domain;

import ir.map.g221.domain.general_types.UnorderedPair;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FriendshipTest {
    @Test
    void testEquals() {
        Friendship f1 = new Friendship(UnorderedPair.create(2L, 5L), LocalDateTime.now());
        Friendship f2 = new Friendship(UnorderedPair.create(5L, 2L), LocalDateTime.of(2001, 12, 12, 12, 12));
        Friendship f3 = new Friendship(UnorderedPair.create(5L, 3L), LocalDateTime.now());

        assert(f1.equals(f1));
        assert(f1.equals(f2));
        assert(f2.equals(f1));
        assert(!f2.equals(f3));
    }
}