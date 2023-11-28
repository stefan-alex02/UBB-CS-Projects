package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<User> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement userStatement = connection.prepareStatement("select * from users " +
                    "where id = ?");
            PreparedStatement friendshipsStatement = connection.prepareStatement(
                    "SELECT F.id2 AS id, " +
                    "U.first_name AS first_name, " +
                    "U.last_name AS last_name " +
                    "FROM friendships F " +
                    "INNER JOIN users U ON U.id = F.id2 " +
                    "WHERE F.id1 = ? " +
                    "UNION " +
                    "SELECT F.id1 AS id, " +
                    "U.first_name AS first_name, " +
                    "U.last_name AS last_name " +
                    "FROM friendships F " +
                    "INNER JOIN users U ON U.id = F.id1 " +
                    "WHERE F.id2 = ? " +
                    "ORDER BY id;")
        ) {
            userStatement.setLong(1, aLong);
            ResultSet resultSet1 = userStatement.executeQuery();

            friendshipsStatement.setLong(1, aLong);
            friendshipsStatement.setLong(2, aLong);
            ResultSet resultSet2 = friendshipsStatement.executeQuery();

            if(resultSet1.next()) {
                String firstName = resultSet1.getString("first_name");
                String lastName = resultSet1.getString("last_name");
                User user = new User(aLong, firstName, lastName);

                while(resultSet2.next()) {
                    User friend = createUserFrom(resultSet2);
                    user.addFriend(friend);
                }
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {
            Set<User> users = new HashSet<>();

            while (resultSet.next())
            {
                User user = createUserFrom(resultSet);
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private User createUserFrom(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        return new User(id, firstName,lastName);
    }

    @Override
    public Integer getSize() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select COUNT(*) AS USER_COUNT from users");
             ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.next() ? resultSet.getInt("USER_COUNT") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        validator.validate(entity);

        String insertSQL = "insert into users (first_name,last_name) values(?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteSQL = "delete from users where id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL)
        ) {
            statement.setLong(1, aLong);

            Optional<User> foundUser = findOne(aLong);

            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        validator.validate(entity);

        String updateSQL = "update users set first_name=?,last_name=? where id=?";
        try(var connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(updateSQL)
        ) {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setLong(3,entity.getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
