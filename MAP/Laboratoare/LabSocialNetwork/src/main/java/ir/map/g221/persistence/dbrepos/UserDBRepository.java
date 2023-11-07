package ir.map.g221.persistence.dbrepos;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.persistence.Repository;

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
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users " +
                    "where id = ?");

        ) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User u = new User(aLong, firstName,lastName);
                return Optional.of(u);
            }
            return Optional.empty();
        } catch (SQLException e) {
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
                User user=new User(id, firstName,lastName);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        String insertSQL="insert into users (first_name,last_name) values(?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL);)
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            int response=statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> delete(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteSQL="delete from users where id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL);
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
        if(entity==null)
        {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        String updateSQL="update users set first_name=?,last_name=? where id=?";
        try(var connection= DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(updateSQL);)
        {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setLong(3,entity.getId());

            int response= statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
