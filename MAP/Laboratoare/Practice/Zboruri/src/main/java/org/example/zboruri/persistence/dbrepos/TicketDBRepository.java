package org.example.zboruri.persistence.dbrepos;

import javafx.util.Pair;
import org.example.zboruri.domain.Flight;
import org.example.zboruri.domain.Ticket;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.Repository;
import org.example.zboruri.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TicketDBRepository implements Repository<Pair<String, Long>, Ticket> {
    private final DatabaseConnection databaseConnection;
    private final Validator<Ticket> validator;

    public TicketDBRepository(DatabaseConnection databaseConnection, Validator<Ticket> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    private Ticket getTicket(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("username");
        Long flightId = resultSet.getLong("flightId");

        LocalDateTime purchaseTime = resultSet.getTimestamp("purchaseTime").toLocalDateTime();

        return new Ticket(new Pair<>(username, flightId), purchaseTime);
    }

    @Override
    public Optional<Ticket> findOne(Pair<String, Long> longStringPair) {
        if (longStringPair == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from tickets T " +
                            "where T.username = ? and T.\"flightId\" = ?");

            statement.setString(1, longStringPair.getKey());
            statement.setLong(2, longStringPair.getValue());

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Ticket ticket = getTicket(resultSet);

                return Optional.of(ticket);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Ticket> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from tickets T ");

            ResultSet resultSet = statement.executeQuery();

            Set<Ticket> ticketSet = new HashSet<>();
            while (resultSet.next()) {
                Ticket ticket = getTicket(resultSet);

                ticketSet.add(ticket);
            }

            return ticketSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(
                    "INSERT INTO tickets(username, \"flightId\", \"purchaseTime\") " +
                            "VALUES (?,?,?)");

            statement.setString(1, entity.getID().getKey());
            statement.setLong(2, entity.getID().getValue());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
