package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class FriendshipDetailsOfUserQuery extends MappableCustomQuery<UnorderedPair<Long, Long>,
        Friendship, FriendshipDetails>{

    private final Long userID;

    /**
     * Creates a custom query for finding the friends of a user.
     * @param settings an array containing the id of the user (as Long)
     */
    public FriendshipDetailsOfUserQuery(Object... settings) {
        super(settings);
        if (settings.length != 1) {
            throw new IllegalArgumentException("Wrong number of setting arguments");
        }
        if (!(settings[0] instanceof Long)) {
            throw new IllegalArgumentException("Given argument(s) is of wrong type");
        }
        userID = (long)settings[0];
        this.queryString = "SELECT F.id1, " +
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
                "WHERE F.id1 = ? OR F.id2 = ?";
    }

    @Override
    public void fillStatement(PreparedStatement statement) throws SQLException {
        statement.setLong(1, userID);
        statement.setLong(2, userID);
    }

    @Override
    public FriendshipDetails map(Friendship entity) {
        return FriendshipDetails.of(entity,
                Objects.equals(entity.getFirstUser().getId(), userID) ?
                        entity.getFirstUser() :
                        entity.getSecondUser());
    }
}
