package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConversationUsersQuery extends CustomQuery<Long, User> {

    /**
     * Creates a custom query for finding the users a given user has conversations with.
     * @param settings a param array containing the id of the user (as Long)
     */
    public ConversationUsersQuery(Object... settings) {
        super(settings);
        if (settings.length != 1) {
            throw new IllegalArgumentException("Wrong number of setting arguments");
        }
        if (!(settings[0] instanceof Long)) {
            throw new IllegalArgumentException("Given argument(s) is of wrong type");
        }
        this.queryString = "SELECT U.id, U.username, U.first_name, U.last_name, U.password " +
                "FROM messages M " +
                "INNER JOIN messages_receivers MR ON MR.message_id = M.id " +
                "INNER JOIN users U ON U.id = MR.receiver_id " +
                "WHERE M.from_user_id = ? " +
                "GROUP BY U.id, U.username, U.first_name, U.last_name, U.password " +
                "UNION " +
                "SELECT U.id, U.username, U.first_name, U.last_name, U.password " +
                "FROM messages M " +
                "INNER JOIN users U ON U.id = M.from_user_id " +
                "INNER JOIN messages_receivers MR ON MR.message_id = M.id " +
                "WHERE MR.receiver_id = ? " +
                "GROUP BY U.id, U.username, U.first_name, U.last_name, U.password";
    }

    @Override
    public void fillStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(1, (long)settings[0]);
        statement.setLong(2, (long)settings[0]);
    }
}
