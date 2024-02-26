package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NonFriendsOfUserQuery extends CustomQuery<Long, User>{

    /**
     * Creates a custom query for finding the user that are not friends with a user.
     * @param settings an array containing the id of the user (as Long)
     */
    public NonFriendsOfUserQuery(Object... settings) {
        super(settings);
        if (settings.length != 1) {
            throw new IllegalArgumentException("Wrong number of setting arguments");
        }
        if (!(settings[0] instanceof Long)) {
            throw new IllegalArgumentException("Given argument(s) is of wrong type");
        }
        this.queryString = "SELECT U.id AS id, " +
                "U.username AS username, " +
                "U.first_name AS first_name, " +
                "U.last_name AS last_name, " +
                "U.password AS password " +
                "FROM users U " +
                "WHERE U.id <> ? AND id NOT IN(" +
                "   SELECT F.id1 from friendships F WHERE F.id2 = ?" +
                "   UNION " +
                "   SELECT F.id2 from friendships F WHERE F.id1 = ?)";
    }

    @Override
    public void fillStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(1, (long)settings[0]);
        statement.setLong(2, (long)settings[0]);
        statement.setLong(3, (long)settings[0]);
    }
}
