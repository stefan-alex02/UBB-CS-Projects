package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.PasswordEncoder;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDBRepository implements Repository<Long, User> {
    private final Validator<User> validator;
    private final DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();
    private final PasswordEncoder passwordEncoder;

    public UserDBRepository(Validator<User> validator, PasswordEncoder passwordEncoder) {
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUserWithoutFriends(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String password = resultSet.getString("password");
        return new User(id, username, firstName, lastName, password);
    }

    @Override
    public User createEntityFrom(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String username = resultSet.getString("username");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String password = resultSet.getString("password");
        User user = new User(id, username, firstName, lastName, password);

        // Creating friends statement :
        Connection connection = databaseConnection.getConnection();
        PreparedStatement friendshipsStatement = connection.prepareStatement(
                "SELECT F.id2 AS id, " +
                        "U.username AS username, " +
                        "U.first_name AS first_name, " +
                        "U.last_name AS last_name, " +
                        "U.password AS password " +
                        "FROM friendships F " +
                        "INNER JOIN users U ON U.id = F.id2 " +
                        "WHERE F.id1 = ? " +
                        "UNION " +
                        "SELECT F.id1 AS id, " +
                        "U.username AS username, " +
                        "U.first_name AS first_name, " +
                        "U.last_name AS last_name, " +
                        "U.password AS password " +
                        "FROM friendships F " +
                        "INNER JOIN users U ON U.id = F.id1 " +
                        "WHERE F.id2 = ? " +
                        "ORDER BY id;");

        friendshipsStatement.setLong(1, id);
        friendshipsStatement.setLong(2, id);
        ResultSet friendsResultSet = friendshipsStatement.executeQuery();

        while(friendsResultSet.next()) {
            User friend = createUserWithoutFriends(friendsResultSet);
            user.addFriend(friend);
        }

        return user;
    }

    @Override
    public String getTableName() {
        return "users";
    }

    @Override
    public Optional<User> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement userStatement = connection.prepareStatement(
                    "select * from users " +
                    "where id = ?");

            userStatement.setLong(1, aLong);
            ResultSet resultSet = userStatement.executeQuery();

            if (resultSet.next()) {
                User user = createEntityFrom(resultSet);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users");

            ResultSet resultSet = statement.executeQuery();

            Set<User> users = new HashSet<>();

            while (resultSet.next()) {
                User user = createEntityFrom(resultSet);
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("select COUNT(*) AS USER_COUNT from users");
            ResultSet resultSet = statement.executeQuery();

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

        String insertSQL = "insert into users (username, first_name, last_name, password) " +
                "values(?,?,?,?)";
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(insertSQL);

            String encodedPassword = passwordEncoder.encodeToSHAHexString(entity.getPassword());

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, encodedPassword);
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
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(deleteSQL);

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

        String updateSQL = "update users set username=?,first_name=?,last_name=?,password=? " +
                "where id=?";
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(updateSQL);

            String encodedPassword = passwordEncoder.encodeToSHAHexString(entity.getPassword());

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setString(4, encodedPassword);
            statement.setLong(3, entity.getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
