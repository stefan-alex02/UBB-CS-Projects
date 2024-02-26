package org.example.zboruri.persistence.dbrepos;

import org.example.zboruri.domain.Flight;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FlightDBRepository implements Repository<Long, Flight> {
    private final DatabaseConnection databaseConnection;

    public FlightDBRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected Flight getFlight(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("flightId");

        String from = resultSet.getString("from");
        String to = resultSet.getString("to");

        LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
        LocalDateTime landingTime = resultSet.getTimestamp("landingTime").toLocalDateTime();

        Integer seats = resultSet.getInt("seats");

        return new Flight(id, from, to, departureTime, landingTime, seats);
    }

    @Override
    public Optional<Flight> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from flights F " +
                            "where F.flightId = ?");

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Flight flight = getFlight(resultSet);

                return Optional.of(flight);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Flight> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from flights F ");

            ResultSet resultSet = statement.executeQuery();

            Set<Flight> flightSet = new HashSet<>();
            while (resultSet.next()) {
                Flight flight = getFlight(resultSet);

                flightSet.add(flight);
            }

            return flightSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        throw new UnsupportedOperationException();
    }
}
