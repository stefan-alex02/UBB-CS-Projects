package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PendingFriendRequestsOfUserQuery extends CustomQuery<Long, FriendRequest> {

    /**
     * Creates a custom query for finding the pending friend requests sent to a user.
     * @param settings an array containing the id of the user (as Long)
     */
    public PendingFriendRequestsOfUserQuery(Object... settings) {
        super(settings);
        if (settings.length != 1) {
            throw new IllegalArgumentException("Wrong number of setting arguments");
        }
        if (!(settings[0] instanceof Long)) {
            throw new IllegalArgumentException("Given argument(s) is of wrong type");
        }
        this.queryString = "SELECT FR.id, FR.from_id, " +
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
                "WHERE FR.to_id = ? AND FR.status = 'PENDING'";
    }

    @Override
    public void fillStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(1, (long)settings[0]);
    }
}
