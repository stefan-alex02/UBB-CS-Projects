package org.example;

import org.example.repository.dbRepository.RegizorDBRepository;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/cinema";
        String username = "postgres";
        String password = "postgres";

        RegizorDBRepository regizorDBRepository = new RegizorDBRepository(url, username, password);

        regizorDBRepository.findAll().forEach(System.out::println);
    }
}