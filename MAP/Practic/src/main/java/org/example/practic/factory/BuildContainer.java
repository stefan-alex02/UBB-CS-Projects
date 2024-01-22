package org.example.practic.factory;

import org.example.practic.business.DummyEntityService;
import org.example.practic.persistence.DatabaseConnection;

import java.sql.SQLException;

public class BuildContainer {
    private final DatabaseConnection databaseConnection;
    private final DummyEntityService dummyEntityService;

    public BuildContainer(DatabaseConnection databaseConnection, DummyEntityService dummyEntityService) {
        this.databaseConnection = databaseConnection;
        this.dummyEntityService = dummyEntityService;
    }

    public DummyEntityService getDummyEntityService() {
        return dummyEntityService;
    }

    public void closeSQLConnection() throws SQLException {
        databaseConnection.closeConnection();
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
