package org.example;

import org.example.domain.User;
import org.example.persistence.Repository;
import org.example.persistence.UserDBRepository;

import java.util.Arrays;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "postgres";

        Repository<Long, User> userRepository = new UserDBRepository(url, username, password);
//        System.out.println(userRepository.findOne(2L));

//        User newUser = new User("Andrei", "Andrei");
//        System.out.println(userRepository.save(newUser));

//        try {
//            User updatedUser = new User("Andrei nou", "Nume Andrei nou");
//            updatedUser.setId(5L);
//            System.out.println(userRepository.update(updatedUser));
//        }
//        catch(RuntimeException re) {
//            System.out.println(re.getMessage());
//        }
    }
}