package org.example.practic.factory;

import org.example.practic.business.CityService;
import org.example.practic.business.TicketService;
import org.example.practic.business.TrainStationService;
import org.example.practic.persistence.DatabaseConnection;

import java.sql.SQLException;

public class BuildContainer {
    private final DatabaseConnection databaseConnection;
    private final CityService cityService;
    private final TrainStationService trainStationService;
    private final TicketService ticketService;
    public BuildContainer(DatabaseConnection databaseConnection, CityService cityService,
                          TrainStationService trainStationService,
                          TicketService ticketService
    ) {
        this.databaseConnection = databaseConnection;
        this.cityService = cityService;
        this.trainStationService = trainStationService;
        this.ticketService = ticketService;
    }

    public CityService getCityService() {
        return cityService;
    }

    public TrainStationService getTrainStationService() {
        return trainStationService;
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
