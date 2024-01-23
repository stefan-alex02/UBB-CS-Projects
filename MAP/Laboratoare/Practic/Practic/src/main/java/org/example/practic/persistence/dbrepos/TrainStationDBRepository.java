package org.example.practic.persistence.dbrepos;

import org.example.practic.domain.DummyEntity;
import org.example.practic.domain.TrainStation;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;
import org.example.practic.utils.ThreeTuple;
import org.example.practic.validation.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TrainStationDBRepository implements Repository<ThreeTuple<String, String, String>, TrainStation> {
    private final DatabaseConnection databaseConnection;

    public TrainStationDBRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected static TrainStation getEntity(ResultSet resultSet) throws SQLException {
        String trainId = resultSet.getString("train_id");
        String departureId = resultSet.getString("departure_city_id");
        String destinationId = resultSet.getString("destination_city_id");

        return new TrainStation(new ThreeTuple<>(
                trainId,
                departureId,
                destinationId
                ));
    }

    @Override
    public Optional<TrainStation> findOne(ThreeTuple<String, String, String> tuple) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<TrainStation> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from train_stations T");

            ResultSet resultSet = statement.executeQuery();

            Set<TrainStation> entitySet = new HashSet<>();
            while (resultSet.next()) {
                TrainStation entity = getEntity(resultSet);

                entitySet.add(entity);
            }

            return entitySet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<TrainStation> save(TrainStation entity) {
        throw new UnsupportedOperationException();
    }
}
