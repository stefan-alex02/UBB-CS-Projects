package org.example.practic.persistence.dbrepos;

import org.example.practic.domain.City;
import org.example.practic.domain.DummyEntity;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;
import org.example.practic.validation.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CititesDBRepository implements Repository<String, City> {
    private final DatabaseConnection databaseConnection;

    public CititesDBRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected static City getEntity(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");

        return new City(id, name);
    }

    @Override
    public Optional<City> findOne(String aString) {
        if (aString == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from cities C " +
                            "where C.id = ?");

            statement.setString(1, aString);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                City entity = getEntity(resultSet);

                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<City> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from cities C");

            ResultSet resultSet = statement.executeQuery();

            Set<City> entitySet = new HashSet<>();
            while (resultSet.next()) {
                City entity = getEntity(resultSet);

                entitySet.add(entity);
            }

            return entitySet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<City> save(City entity) {
        throw new UnsupportedOperationException();
    }
}
