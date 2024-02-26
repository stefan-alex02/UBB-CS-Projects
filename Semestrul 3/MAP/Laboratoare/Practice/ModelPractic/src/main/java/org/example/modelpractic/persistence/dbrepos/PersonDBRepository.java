package org.example.modelpractic.persistence.dbrepos;

import org.example.modelpractic.domain.Driver;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.persistence.DatabaseConnection;
import org.example.modelpractic.persistence.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersonDBRepository implements Repository<Long, Person> {
    private final DatabaseConnection databaseConnection;

    public PersonDBRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected static Person getPerson(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");

        String username = resultSet.getString("username");
        String name = resultSet.getString("name");

        long person_id = resultSet.getLong("person_id");
        if (person_id != 0) {
            String indicativMasina = resultSet.getString("indicativ_masina");

            return new Driver(id, username, name, indicativMasina);
        }

        return new Person(id, username, name);
    }

    @Override
    public Optional<Person> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from persons P " +
                            "left join drivers D on D.person_id = P.id " +
                            "where P.id = ?");

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Person person = getPerson(resultSet);

                return Optional.of(person);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Person> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from persons P " +
                            "left join drivers D on D.person_id = P.id ");

            ResultSet resultSet = statement.executeQuery();

            Set<Person> personSet = new HashSet<>();
            while (resultSet.next()) {
                Person person = getPerson(resultSet);

                personSet.add(person);
            }

            return personSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Person> save(Person entity) {
        throw new UnsupportedOperationException();
    }
}
