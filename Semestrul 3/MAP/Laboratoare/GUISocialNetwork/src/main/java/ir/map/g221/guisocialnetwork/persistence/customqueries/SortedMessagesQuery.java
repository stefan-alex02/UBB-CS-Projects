package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.Message;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SortedMessagesQuery extends CustomQuery<Long, Message> {
    /**
     * Creates a custom query for finding the conversation messages between two users.
     * @param settings an array containing the id of one user, then the id of the other user
     */
    public SortedMessagesQuery(Object... settings) {
        super(settings);
        if (settings.length != 2) {
            throw new IllegalArgumentException("Wrong number of setting arguments");
        }
        if (!(settings[0] instanceof Long && settings[1] instanceof Long)) {
            throw new IllegalArgumentException("Given arguments are of wrong type");
        }
        this.queryString = "SELECT M.id, M.from_user_id, U.username, U.first_name, U.last_name, U.password, " +
                "M.message, M.date, " +
                "R.reply_to_id, M2.message as reply_to_message, " +
                "M2.from_user_id as reply_to_user_id, " +
                "U2.username as reply_to_user_username, " +
                "U2.first_name as reply_to_user_first_name, " +
                "U2.last_name as reply_to_user_last_name, " +
                "U2.password as reply_to_user_password, " +
                "M2.date as reply_to_message_date, " +
                "R2.reply_to_id as reply_to_reply_to_id " +
                "FROM messages M " +
                "INNER JOIN users U ON U.id = M.from_user_id " +
                "LEFT JOIN reply_messages R ON R.message_id = M.id " +
                "LEFT JOIN messages M2 ON M2.id = R.reply_to_id " +
                "LEFT JOIN users U2 ON U2.id = M2.from_user_id " +
                "LEFT JOIN reply_messages R2 ON R2.message_id = R.reply_to_id " +
                "WHERE (M.from_user_id = ? AND ? IN (" +
                "   SELECT MR.receiver_id " +
                "   FROM messages_receivers MR " +
                "   WHERE MR.message_id = M.id)) OR " +
                "   (M.from_user_id = ? AND ? IN (" +
                "   SELECT MR.receiver_id " +
                "   FROM messages_receivers MR " +
                "   WHERE MR.message_id = M.id)) " +
                "ORDER BY M.date DESC";
    }

    @Override
    public void fillStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(1, (long)settings[0]);
        statement.setLong(2, (long)settings[1]);
        statement.setLong(3, (long)settings[1]);
        statement.setLong(4, (long)settings[0]);
    }
}
