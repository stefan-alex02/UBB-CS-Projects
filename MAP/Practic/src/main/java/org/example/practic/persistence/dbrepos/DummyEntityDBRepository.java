package org.example.practic.persistence.dbrepos;

import org.example.practic.domain.DummyEntity;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;
import org.example.practic.validation.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DummyEntityDBRepository implements Repository<Long, DummyEntity> {
    private final DatabaseConnection databaseConnection;
    private final Validator<DummyEntity> validator;

    public DummyEntityDBRepository(DatabaseConnection databaseConnection, Validator<DummyEntity> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    protected static DummyEntity getEntity(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");

        return new DummyEntity(id);
    }

    @Override
    public Optional<DummyEntity> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from dummies D " +
                            "where D.id = ?");

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                DummyEntity entity = getEntity(resultSet);

                return Optional.of(entity);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<DummyEntity> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from dummies D");

            ResultSet resultSet = statement.executeQuery();

            Set<DummyEntity> entitySet = new HashSet<>();
            while (resultSet.next()) {
                DummyEntity entity = getEntity(resultSet);

                entitySet.add(entity);
            }

            return entitySet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DummyEntity> save(DummyEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(
                    "INSERT INTO dummies(id) " +
                            "VALUES (?)");

            statement.setLong(1, entity.getID());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<DummyEntity> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<DummyEntity> update(DummyEntity entity) {
        return Optional.empty();
    }
}
