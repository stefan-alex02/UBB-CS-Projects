package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<UnorderedPair<Long, Long>, Friendship> {
    private final Validator<Friendship> validator;
    private final DatabaseConnection databaseConnection;

    public FriendshipDBRepository(DatabaseConnection databaseConnection, Validator<Friendship> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    private @NotNull Friendship createFriendshipFrom(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong("id1");
        String username1 = resultSet.getString("username1");
        String firstName1 = resultSet.getString("first_name1");
        String lastName1 = resultSet.getString("last_name1");
        String password1 = resultSet.getString("password1");
        User user1 = new User(id1, username1, firstName1, lastName1, password1);

        Long id2 = resultSet.getLong("id2");
        String username2 = resultSet.getString("username2");
        String firstName2 = resultSet.getString("first_name2");
        String lastName2 = resultSet.getString("last_name2");
        String password2 = resultSet.getString("password2");
        User user2 = new User(id2, username2, firstName2, lastName2, password2);

        LocalDateTime friendsFrom = resultSet.getTimestamp("friends_from").toLocalDateTime();

        return new Friendship(user1, user2, friendsFrom);
    }

    @Override
    public Optional<Friendship> findOne(UnorderedPair<Long, Long> unorderedPair) throws IllegalArgumentException {
        if (unorderedPair == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT F.id1, " +
                            "U1.username AS username1, " +
                            "U1.first_name AS first_name1, " +
                            "U1.last_name AS last_name1, " +
                            "U1.password AS password1, " +
                            "F.id2, " +
                            "U2.username AS username2, " +
                            "U2.first_name AS first_name2, " +
                            "U2.last_name AS last_name2, " +
                            "U2.password AS password2, " +
                            "F.friends_from " +
                            "FROM friendships F " +
                            "INNER JOIN users U1 ON U1.id = F.id1 " +
                            "INNER JOIN users U2 ON U2.id = F.id2 " +
                            "WHERE F.id1 = ? AND F.id2 = ?");

            statement.setLong(1, unorderedPair.getFirst());
            statement.setLong(2, unorderedPair.getSecond());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Friendship friendship = createFriendshipFrom(resultSet);
                return Optional.of(friendship);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "SELECT F.id1, " +
                             "U1.username AS username1, " +
                             "U1.first_name AS first_name1, " +
                             "U1.last_name AS last_name1, " +
                             "U1.password AS password1, " +
                             "F.id2, " +
                             "U2.username AS username2, " +
                             "U2.first_name AS first_name2," +
                             "U2.last_name AS last_name2, " +
                             "U2.password AS password2, " +
                             "F.friends_from " +
                             "FROM friendships F " +
                             "INNER JOIN users U1 ON U1.id = F.id1 " +
                             "INNER JOIN users U2 ON U2.id = F.id2"
            );
            ResultSet resultSet = statement.executeQuery();

            Set<Friendship> friendships = new HashSet<>();
            while (resultSet.next())
            {
                Friendship friendship = createFriendshipFrom(resultSet);
                friendships.add(friendship);
            }
            return friendships;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "select COUNT(*) AS FRIENDSHIP_COUNT from friendships");

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("FRIENDSHIP_COUNT") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO friendships (id1, id2, friends_from) VALUES (?,?,?)");

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
    public Optional<Friendship> delete(UnorderedPair<Long, Long> unorderedPair) throws IllegalArgumentException {
        if (unorderedPair == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM friendships F " +
                             "WHERE F.id1 = ? AND F.id2 = ?");

            statement.setLong(1, unorderedPair.getFirst());
            statement.setLong(2, unorderedPair.getSecond());

            Optional<Friendship> foundFriendship = findOne(unorderedPair);

            int response = 0;
            if (foundFriendship.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundFriendship;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) throws IllegalArgumentException {
        if(entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE friendships " +
                            "SET friends_from = ? " +
                            "WHERE id1 = ? AND id2 = ?");

            statement.setTimestamp(1, Timestamp.valueOf(entity.getFriendsFromDate()));
            statement.setLong(2, entity.getId().getFirst());
            statement.setLong(3, entity.getId().getSecond());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
