package org.example.vacanta.factory;

import org.example.modelpractic.business.DriverService;
import org.example.modelpractic.business.OrderService;
import org.example.modelpractic.business.PersonService;
import org.example.modelpractic.domain.Order;
import org.example.modelpractic.persistence.DatabaseConnection;
import org.example.modelpractic.persistence.Repository;
import org.example.modelpractic.persistence.dbrepos.OrderDBRepository;
import org.example.modelpractic.persistence.pagingrepos.PersonDBPagingRepository;
import org.example.modelpractic.validation.OrderValidator;

import java.sql.SQLException;

public class Factory {
    private static Factory instance = null;

    private Factory() {
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public BuildContainer build() {
        try {
            String url="jdbc:postgresql://localhost:5432/taxi";
            String username = "postgres";
            String password = "postgres";

            DatabaseConnection.setInstance(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PersonDBPagingRepository personRepo =
                new PersonDBPagingRepository(DatabaseConnection.getSingleInstance());
        Repository<Long, Order> orderRepo =
                new OrderDBRepository(DatabaseConnection.getSingleInstance(), OrderValidator.getInstance());

        PersonService personService = new PersonService(personRepo);
        OrderService orderService = new OrderService(orderRepo);
        DriverService driverService = new DriverService(personRepo, orderRepo);

        return new BuildContainer(
                DatabaseConnection.getSingleInstance(), personService, orderService, driverService);
    }
}
