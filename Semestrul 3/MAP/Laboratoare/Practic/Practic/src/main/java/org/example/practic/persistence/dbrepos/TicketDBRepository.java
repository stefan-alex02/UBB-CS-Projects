package org.example.practic.persistence.dbrepos;

import org.example.practic.domain.City;
import org.example.practic.domain.DummyEntity;
import org.example.practic.domain.Ticket;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;
import org.example.practic.validation.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TicketDBRepository implements Repository<Long, Ticket> {
    private final DatabaseConnection databaseConnection;
    private final Validator<Ticket> validator;

    public TicketDBRepository(DatabaseConnection databaseConnection, Validator<Ticket> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    protected static Ticket getEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String train = resultSet.getString("train_id");
        String city = resultSet.getString("departure_city_id");
        LocalDate date = resultSet.getDate("date").toLocalDate();

        return new Ticket(id, train, city, date);
    }

    @Override
    public Optional<Ticket> findOne(Long aString) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Ticket> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from tickets T");

            ResultSet resultSet = statement.executeQuery();

            Set<Ticket> entitySet = new HashSet<>();
            while (resultSet.next()) {
                Ticket entity = getEntity(resultSet);

                entitySet.add(entity);
            }

            return entitySet;
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
                    "INSERT INTO tickets(train_id, departure_city_id, date) " +
                            "VALUES (?, ?, ?)");

            statement.setString(1, entity.getTrainId());
            statement.setString(2, entity.getDepartureCityId());
            statement.setDate(3, Date.valueOf(entity.getDate()));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
