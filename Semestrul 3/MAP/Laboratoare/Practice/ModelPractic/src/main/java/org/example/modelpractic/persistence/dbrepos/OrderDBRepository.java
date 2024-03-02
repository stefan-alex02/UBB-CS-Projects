package org.example.modelpractic.persistence.dbrepos;

import org.example.modelpractic.domain.Driver;
import org.example.modelpractic.domain.Order;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.persistence.DatabaseConnection;
import org.example.modelpractic.persistence.Repository;
import org.example.modelpractic.validation.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class OrderDBRepository implements Repository<Long, Order> {
    private final DatabaseConnection databaseConnection;
    private final Validator<Order> validator;

    public OrderDBRepository(DatabaseConnection databaseConnection, Validator<Order> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    private Order getOrder(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");

        long person_id = resultSet.getLong("person_id");
        String person_username = resultSet.getString("person_username");
        String person_name = resultSet.getString("person_name");

        Person person = new Person(person_id, person_username, person_name);

        long driver_id = resultSet.getLong("driver_id");
        String driver_username = resultSet.getString("driver_username");
        String driver_name = resultSet.getString("driver_name");
        String indicativ_masina = resultSet.getString("indicativ_masina");

        Driver driver = new Driver(driver_id, driver_username, driver_name, indicativ_masina);

        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

        return new Order(id, person, driver, date);
    }

    @Override
    public Optional<Order> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select O.id,\n" +
                            "       P.id as person_id, P.username as person_username, P.name as person_name,\n" +
                            "       D.person_id as driver_id, PD.username as driver_username,\n" +
                            "            PD.name as driver_name, D.indicativ_masina,\n" +
                            "       O.date\n" +
                            "       from orders O\n" +
                            "inner join persons P on O.person_id = P.id\n" +
                            "inner join drivers D on O.driver_id = D.person_id\n" +
                            "inner join persons PD on PD.id = D.person_id\n" +
                            "where O.id = ?");

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Order order = getOrder(resultSet);

                return Optional.of(order);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Order> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select O.id,\n" +
                            "       P.id as person_id, P.username as person_username, P.name as person_name,\n" +
                            "       D.person_id as driver_id, PD.username as driver_username,\n" +
                            "            PD.name as driver_name, D.indicativ_masina,\n" +
                            "       O.date\n" +
                            "       from orders O\n" +
                            "inner join persons P on O.person_id = P.id\n" +
                            "inner join drivers D on O.driver_id = D.person_id\n" +
                            "inner join persons PD on PD.id = D.person_id\n");

            ResultSet resultSet = statement.executeQuery();

            Set<Order> orderSet = new HashSet<>();
            while (resultSet.next()) {
                Order order = getOrder(resultSet);

                orderSet.add(order);
            }

            return orderSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> save(Order entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(
                    "INSERT INTO orders(person_id, driver_id, date) " +
                            "VALUES (?,?,?)");

            statement.setLong(1, entity.getPerson().getID());
            statement.setLong(2, entity.getTaxiDriver().getID());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
