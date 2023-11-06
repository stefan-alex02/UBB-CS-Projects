package org.example.persistence;

import org.example.domain.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User>{
    private final String url;
    private final String username;
    private final String password;

    public UserDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<User> findOne(Long aLong) {
        Optional<User> user = Optional.empty();

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");
        ) {
            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                User u = new User(firstName, lastName);
                u.setId(aLong);

                user = Optional.of(u);
            }
            return user;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                User user=new User(firstName,lastName);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        return null;
    }

    @Override
    public Optional<User> save(User entity) {
        String insertSQL = "insert into users (first_name, last_name) values(?, ?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL);
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        String updateSQL = "update users set first_name = ?, last_name = ? where id = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL);
        ) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setLong(3, entity.getId());
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
