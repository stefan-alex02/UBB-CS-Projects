package com.example.sem7gui;


import com.example.sem7gui.domain.User;

import com.example.sem7gui.repository.Repository;
import com.example.sem7gui.repository.db.UserDBPagingRepository;
import com.example.sem7gui.repository.db.UserDBRepository;
import com.example.sem7gui.repository.paging.Pageable;
import com.example.sem7gui.repository.paging.PageableImplementation;
import com.example.sem7gui.repository.paging.PagingRepository;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "postgres";

//        Repository<Long, User> userRepository = new UserDBRepository(url, username, password);
//        userRepository.findAll().forEach(System.out::println);
        //System.out.println(userRepository.findOne(1L));

//        User u=new User("fool","l");
//        User u2=null;
//        u.setId(5l);
//       // System.out.println(userRepository.save(u));
//        try
//        {
//            System.out.println(userRepository.update(u2));
//        }
//        catch (IllegalArgumentException e)
//        {
//            System.out.println(e);
//        }

//        try {
//            Long id = 5L;
//            System.out.println(userRepository.delete(null));
//        }
//        catch(IllegalArgumentException e) {
//            System.out.println(e);
//        }

        PagingRepository<Long, User> userDBPagingRepo =
                new UserDBPagingRepository(url, username, password);
        Pageable pageable = new PageableImplementation(3, 5);
        var content = userDBPagingRepo.findAll(pageable)
                .getContent().toList();
        if(!content.isEmpty()) {
            content.forEach(System.out::println);
        }
        else {
            System.out.println("Pagina ceruta nu exista.");
        }

    }
}