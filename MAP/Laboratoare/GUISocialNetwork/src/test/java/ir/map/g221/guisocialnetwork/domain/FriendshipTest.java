package ir.map.g221.guisocialnetwork.domain;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class FriendshipTest {
    @Test
    void testEquals() {
        User u2 = new User(2L, "", "");
        User u3 = new User(3L, "", "");
        User u5 = new User(5L, "", "");

        Friendship f1 = new Friendship(u2, u5, LocalDateTime.now());
        Friendship f2 = new Friendship(u5, u2, LocalDateTime.of(2001, 12, 12, 12, 12));
        Friendship f3 = new Friendship(u5, u3, LocalDateTime.now());

        assert(f1.equals(f1));
        assert(f1.equals(f2));
        assert(f2.equals(f1));
        assert(!f2.equals(f3));
    }
}