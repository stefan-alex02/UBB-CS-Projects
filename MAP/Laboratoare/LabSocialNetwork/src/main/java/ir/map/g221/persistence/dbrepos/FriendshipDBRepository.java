package ir.map.g221.persistence.dbrepos;

import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.domain.validation.FriendshipValidator;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.persistence.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<UnorderedPair<Long, Long>, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Friendship> findOne(UnorderedPair<Long, Long> unorderedPair) {
//        try(Connection connection = DriverManager.getConnection(url, username, password);
//            PreparedStatement statement = connection.prepareStatement(
//                    "");
//        ) {
//            statement.setLong(1, unorderedPair.getFirst());
//            statement.setLong(2, unorderedPair.getSecond());
//            ResultSet resultSet = statement.executeQuery();
//            if(resultSet.next()) {
//                String firstName = resultSet.getString("first_name");
//                String lastName = resultSet.getString("last_name");
//                User u = new User(aLong, firstName,lastName);
//                return Optional.of(u);
//            }
//            return Optional.empty();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT F.id1, " +
                             "U1.first_name AS first_name1, " +
                             "U1.last_name AS last_name1, " +
                             "F.id2, " +
                             "U2.first_name AS first_name2," +
                             "U2.last_name AS last_name2, " +
                             "F.friends_from " +
                             "FROM friendships F " +
                             "INNER JOIN users U1 ON U1.id = F.id1 " +
                             "INNER JOIN users U2 ON U2.id = F.id2"
             );
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
            {
                Long id1 = resultSet.getLong("id1");
                String firstName1 = resultSet.getString("first_name1");
                String lastName1 = resultSet.getString("last_name1");
                User user1 = new User(id1, firstName1, lastName1);

                Long id2 = resultSet.getLong("id2");
                String firstName2 = resultSet.getString("first_name2");
                String lastName2 = resultSet.getString("last_name2");
                User user2 = new User(id2, firstName2, lastName2);

                LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();

                Friendship friendship = new Friendship(user1, user2, friendsFrom);
                friendships.add(friendship);

            }
            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement("select COUNT(*) AS USER_COUNT from users");
//             ResultSet resultSet = statement.executeQuery()
//        ) {
//            return resultSet.next() ? resultSet.getInt("USER_COUNT") : 0;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return 0;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insertSQL = "insert into friendships (id1, id2, friends_from) values(?,?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setLong(1, entity.getId().getFirst());
            statement.setLong(2, entity.getId().getSecond());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFromDate()));
            int response = statement.executeUpdate();

            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(UnorderedPair<Long, Long> longLongUnorderedPair) {
//        if (aLong == null) {
//            throw new IllegalArgumentException("Id cannot be null");
//        }
//
//        String deleteSQL="delete from users where id=?";
//        try (var connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement(deleteSQL);
//        ) {
//            statement.setLong(1, aLong);
//
//            Optional<User> foundUser = findOne(aLong);
//
//            int response = 0;
//            if (foundUser.isPresent()) {
//                response = statement.executeUpdate();
//            }
//
//            return response == 0 ? Optional.empty() : foundUser;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
//        if(entity==null)
//        {
//            throw new IllegalArgumentException("Entity cannot be null!");
//        }
//        String updateSQL="update users set first_name=?,last_name=? where id=?";
//        try(var connection= DriverManager.getConnection(url, username, password);
//            PreparedStatement statement=connection.prepareStatement(updateSQL);)
//        {
//            statement.setString(1,entity.getFirstName());
//            statement.setString(2,entity.getLastName());
//            statement.setLong(3,entity.getId());
//
//            int response= statement.executeUpdate();
//            return response==0 ? Optional.of(entity) : Optional.empty();
//        }
//        catch (SQLException e)
//        {
//            throw new RuntimeException(e);
//        }
        return Optional.empty();
    }
}
