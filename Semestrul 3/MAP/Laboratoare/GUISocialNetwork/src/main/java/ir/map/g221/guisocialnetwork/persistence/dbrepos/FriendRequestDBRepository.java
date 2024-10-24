package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.entities.*;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendRequestDBRepository implements Repository<Long, FriendRequest> {
    private final Validator<FriendRequest> validator;
    private final DatabaseConnection databaseConnection;

    public FriendRequestDBRepository(DatabaseConnection databaseConnection, Validator<FriendRequest> validator) {
        this.databaseConnection = databaseConnection;
        this.validator = validator;
    }

    @Override
    public FriendRequest createEntityFrom(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");

        Long fromUserId = resultSet.getLong("from_id");
        String fromUserUsername = resultSet.getString("from_username");
        String fromUserFirstName = resultSet.getString("from_first_name");
        String fromUserLastName = resultSet.getString("from_last_name");
        String fromUserPassword = resultSet.getString("from_password");
        User fromUser = new User(fromUserId, fromUserUsername,
                fromUserFirstName, fromUserLastName, fromUserPassword);

        Long toUserId = resultSet.getLong("to_id");
        String toUserUsername = resultSet.getString("to_username");
        String toUserFirstName = resultSet.getString("to_first_name");
        String toUserLastName = resultSet.getString("to_last_name");
        String toUserPassword = resultSet.getString("to_password");
        User toUser = new User(toUserId, toUserUsername,
                toUserFirstName, toUserLastName, toUserPassword);

        FriendRequestStatus status = FriendRequestStatus.valueOf(resultSet.getString("status"));
        LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

        return new FriendRequest(id, fromUser, toUser, status, date);
    }

    @Override
    public String getTableName() {
        return "friend_requests";
    }

    @Override
    public Optional<FriendRequest> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT FR.id, FR.from_id, " +
                            "UFrom.username as from_username, " +
                            "UFrom.first_name as from_first_name, UFrom.last_name as from_last_name, " +
                            "UFrom.password as from_password, " +
                            "FR.to_id,  " +
                            "UTo.username as to_username, " +
                            "UTo.first_name as to_first_name, UTo.last_name as to_last_name, " +
                            "Uto.password as to_password, " +
                            "FR.status, FR.date " +
                            "FROM friend_requests FR " +
                            "INNER JOIN users UFrom ON UFrom.id = FR.from_id " +
                            "INNER JOIN users UTo ON UTo.id = FR.to_id " +
                            "WHERE FR.id = ?");

            statement.setLong(1, aLong);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                FriendRequest friendRequest = createEntityFrom(resultSet);
                return Optional.of(friendRequest);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "SELECT FR.id, FR.from_id, " +
                             "UFrom.username as from_username, " +
                             "UFrom.first_name as from_first_name, UFrom.last_name as from_last_name, " +
                             "UFrom.password as from_password, " +
                             "FR.to_id,  " +
                             "UTo.username as to_username, " +
                             "UTo.first_name as to_first_name, UTo.last_name as to_last_name, " +
                             "Uto.password as to_password, " +
                             "FR.status, FR.date " +
                             "FROM friend_requests FR " +
                             "INNER JOIN users UFrom ON UFrom.id = FR.from_id " +
                             "INNER JOIN users UTo ON UTo.id = FR.to_id ");

            ResultSet resultSet = statement.executeQuery();

            Set<FriendRequest> friendRequests = new HashSet<>();
            while (resultSet.next())
            {
                FriendRequest friendRequest = createEntityFrom(resultSet);
                friendRequests.add(friendRequest);
            }

            return friendRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "select COUNT(*) AS FRIEND_REQUEST_COUNT from friend_requests");

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("FRIEND_REQUEST_COUNT") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(
                     "INSERT INTO friend_requests(from_id, to_id, status, date) " +
                             "VALUES (?,?,?,?)");

            statement.setLong(1, entity.getFrom().getId());
            statement.setLong(2, entity.getTo().getId());
            statement.setString(3, entity.getStatus().name());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendRequest> delete(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM friend_requests FR " +
                             "WHERE FR.id = ?");

            statement.setLong(1, aLong);

            Optional<FriendRequest> foundFriendRequest = findOne(aLong);

            int response = 0;
            if (foundFriendRequest.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundFriendRequest;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendRequest> update(FriendRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        validator.validate(entity);

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE friend_requests " +
                            "SET from_id = ?, to_id = ?, status = ?, date = ? " +
                            "WHERE id = ?");

            statement.setLong(1, entity.getFrom().getId());
            statement.setLong(2, entity.getTo().getId());
            statement.setString(3, entity.getStatus().name());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            statement.setLong(5, entity.getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
