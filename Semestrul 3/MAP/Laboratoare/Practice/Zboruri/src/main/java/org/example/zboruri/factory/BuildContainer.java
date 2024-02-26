package org.example.zboruri.factory;

import org.example.zboruri.business.ClientService;
import org.example.zboruri.business.FlightService;
import org.example.zboruri.business.TicketService;
import org.example.zboruri.persistence.DatabaseConnection;

import java.sql.SQLException;

public class BuildContainer {
    private final DatabaseConnection databaseConnection;
    private final ClientService clientService;
    private final FlightService flightService;
    private final TicketService ticketService;

    public BuildContainer(DatabaseConnection databaseConnection, ClientService clientService,
                          FlightService flightService, TicketService ticketService) {
        this.databaseConnection = databaseConnection;
        this.clientService = clientService;
        this.flightService = flightService;
        this.ticketService = ticketService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public FlightService getFlightService() {
        return flightService;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

    public void closeSQLConnection() throws SQLException {
        databaseConnection.closeConnection();
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
