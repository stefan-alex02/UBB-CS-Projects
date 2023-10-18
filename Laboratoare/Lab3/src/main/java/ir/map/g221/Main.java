package ir.map.g221;

import ir.map.g221.domain.Friendship;
import ir.map.g221.domain.User;
import ir.map.g221.domain.general_types.UnorderedPair;
import ir.map.g221.ui.UserConsole;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        User u1 = new User(123L, "Dan", "Danescu");
        User u2 = new User(456L, "Ion", "Ionescu");
        Friendship f1 = new Friendship(UnorderedPair.create(u1.getId(), u2.getId()), LocalDateTime.now());
        Friendship f2 = new Friendship(UnorderedPair.create(u2.getId(), u1.getId()), LocalDateTime.now());

        UserConsole.getInstance().run();
    }
}