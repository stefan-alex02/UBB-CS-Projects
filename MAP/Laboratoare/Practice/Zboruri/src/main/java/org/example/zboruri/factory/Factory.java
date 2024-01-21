package org.example.zboruri.factory;

import javafx.util.Pair;
import org.example.zboruri.business.ClientService;
import org.example.zboruri.business.FlightService;
import org.example.zboruri.business.TicketService;
import org.example.zboruri.domain.Client;
import org.example.zboruri.domain.Ticket;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.Repository;
import org.example.zboruri.persistence.dbrepos.ClientDBRepository;
import org.example.zboruri.persistence.dbrepos.TicketDBRepository;
import org.example.zboruri.persistence.pagingrepos.FlightsDBPagingRepository;
import org.example.zboruri.validation.TicketValidator;

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
            String url="jdbc:postgresql://localhost:5432/zboruri";
            String username = "postgres";
            String password = "postgres";

            DatabaseConnection.setInstance(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Repository<String, Client> clientRepository =
                new ClientDBRepository(DatabaseConnection.getSingleInstance());
        FlightsDBPagingRepository flightRepository =
                new FlightsDBPagingRepository(DatabaseConnection.getSingleInstance());
        Repository<Pair<String, Long>, Ticket> ticketRepository =
                new TicketDBRepository(DatabaseConnection.getSingleInstance(),
                        TicketValidator.getInstance());

        ClientService clientService = new ClientService(clientRepository);
        TicketService ticketService = new TicketService(ticketRepository);
        FlightService flightService = new FlightService(flightRepository, ticketRepository);

        return new BuildContainer(DatabaseConnection.getSingleInstance(),
                clientService, flightService, ticketService);
    }
}
