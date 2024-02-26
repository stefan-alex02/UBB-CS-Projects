package org.example.practic.factory;

import org.example.practic.business.CityService;
import org.example.practic.business.TicketService;
import org.example.practic.business.TrainStationService;
import org.example.practic.domain.City;
import org.example.practic.domain.Ticket;
import org.example.practic.domain.TrainStation;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;
import org.example.practic.persistence.dbrepos.CititesDBRepository;
import org.example.practic.persistence.dbrepos.TicketDBRepository;
import org.example.practic.persistence.dbrepos.TrainStationDBRepository;
import org.example.practic.utils.ThreeTuple;
import org.example.practic.validation.TicketValidator;

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
            String url="jdbc:postgresql://localhost:5432/practic";
            String username = "postgres";
            String password = "postgres";

            DatabaseConnection.setInstance(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Repository<String, City> cityRepository =
                new CititesDBRepository(DatabaseConnection.getSingleInstance());
        Repository<ThreeTuple<String, String, String>, TrainStation> trainStationRepository =
                new TrainStationDBRepository(DatabaseConnection.getSingleInstance());
        Repository<Long, Ticket> ticketRepository =
                new TicketDBRepository(DatabaseConnection.getSingleInstance(),
                        TicketValidator.getInstance());

        CityService cityService  =
                new CityService(cityRepository);
        TrainStationService trainStationService =
                new TrainStationService(trainStationRepository);
        TicketService ticketService =
                new TicketService(ticketRepository);

        return new BuildContainer(
                DatabaseConnection.getSingleInstance(), cityService,
                trainStationService, ticketService);
    }
}
