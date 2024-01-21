package org.example.modelpractic.factory;


import org.example.modelpractic.business.DriverService;
import org.example.modelpractic.business.OrderService;
import org.example.modelpractic.business.PersonService;
import org.example.modelpractic.persistence.DatabaseConnection;

import java.sql.SQLException;

public class BuildContainer {
    private final DatabaseConnection databaseConnection;
    private final PersonService personService;
    private final OrderService orderService;
    private final DriverService driverService;

    public BuildContainer(DatabaseConnection databaseConnection, PersonService personService,
                          OrderService orderService, DriverService driverService) {
        this.databaseConnection = databaseConnection;
        this.personService = personService;
        this.orderService = orderService;
        this.driverService = driverService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public DriverService getDriverService() {
        return driverService;
    }

    public void closeSQLConnection() throws SQLException {
        databaseConnection.closeConnection();
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
