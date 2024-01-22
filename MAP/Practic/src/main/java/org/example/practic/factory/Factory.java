package org.example.practic.factory;

import org.example.practic.business.DummyEntityService;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.paging.PagingRepository;
import org.example.practic.persistence.pagingrepos.DummyEntityDBPagingRepository;
import org.example.practic.validation.DummyEntityValidator;

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

        DummyEntityDBPagingRepository dummyEntityRepository =
                new DummyEntityDBPagingRepository(PagingRepository.databaseConnection,
                        DummyEntityValidator.getInstance());

        DummyEntityService dummyEntityService =
                new DummyEntityService(dummyEntityRepository);

        return new BuildContainer(
                DatabaseConnection.getSingleInstance(), dummyEntityService);
    }
}
