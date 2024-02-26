package org.example.zboruri.persistence.dbrepos;

import org.example.zboruri.domain.Client;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ClientDBRepository implements Repository<String, Client> {
    private final DatabaseConnection databaseConnection;

    public ClientDBRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    protected static Client getClient(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("username");
        String name = resultSet.getString("name");

        return new Client(username, name);
    }

    @Override
    public Optional<Client> findOne(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from clients C " +
                            "where C.username = ?");

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Client client = getClient(resultSet);

                return Optional.of(client);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Client> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from clients C");

            ResultSet resultSet = statement.executeQuery();

            Set<Client> personSet = new HashSet<>();
            while (resultSet.next()) {
                Client client = getClient(resultSet);

                personSet.add(client);
            }

            return personSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Client> save(Client entity) {
        throw new UnsupportedOperationException();
    }
}
