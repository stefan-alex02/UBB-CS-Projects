package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.ReplyMessage;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> messageValidator;
    private final Validator<ReplyMessage> replyMessageValidator;

    public MessageDBRepository(String url, String username, String password,
                               Validator<Message> messageValidator,
                               Validator<ReplyMessage> replyMessageValidator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.messageValidator = messageValidator;
        this.replyMessageValidator = replyMessageValidator;
    }

    private Set<User> createReceiversFrom(ResultSet receiversResultSet) throws SQLException {
        Set<User> receivers = new HashSet<>();
        while(receiversResultSet.next()) {
            Long receiverId = receiversResultSet.getLong("id");
            String receiverFirstName = receiversResultSet.getString("first_name");
            String receiverLastName = receiversResultSet.getString("last_name");

            receivers.add(new User(receiverId, receiverFirstName, receiverLastName));
        }

        return receivers;
    }

    private Message createMessageFrom(ResultSet messageResultSet, ResultSet receiversResultSet) throws SQLException {
        Message msg = null;

        Long id = messageResultSet.getLong("id");
        String message = messageResultSet.getString("message");
        LocalDateTime date = messageResultSet.getTimestamp("date").toLocalDateTime();

        Long fromUserId = messageResultSet.getLong("from_user_id");
        String fromUserFirstName = messageResultSet.getString("first_name");
        String fromUserLastName = messageResultSet.getString("last_name");
        User fromUser = new User(fromUserId, fromUserFirstName, fromUserLastName);

        Long replyToId = messageResultSet.getLong("reply_to_id");
        if (replyToId != 0) {
            String replyToMessage = messageResultSet.getString("reply_to_message");

            Long replyToUserId = messageResultSet.getLong("reply_to_user_id");
            String replyToUserFirstName = messageResultSet.getString("reply_to_user_first_name");
            String replyToUserLastName = messageResultSet.getString("reply_to_user_last_name");
            LocalDateTime replyToMessageDate = messageResultSet.getTimestamp("reply_to_message_date")
                    .toLocalDateTime();
            User replyToUser = new User(replyToUserId, replyToUserFirstName, replyToUserLastName);

            Message repliedMessage;
            if (messageResultSet.getLong("reply_to_reply_to_id") != 0) {
                repliedMessage = new ReplyMessage(replyToId, replyToUser, replyToMessage, replyToMessageDate);
            }
            else {
                repliedMessage = new Message(replyToId, replyToUser, replyToMessage, replyToMessageDate);
            }

            msg = new ReplyMessage(id, fromUser, createReceiversFrom(receiversResultSet), message, date, repliedMessage);
        }
        else {
            msg = new Message(id, fromUser, createReceiversFrom(receiversResultSet), message, date);
        }

        return msg;
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement messageStatement = connection.prepareStatement(
                    "SELECT M.id, M.from_user_id, U.first_name, U.last_name, M.message, M.date, " +
                            "R.reply_to_id, M2.message as reply_to_message, " +
                            "M2.from_user_id as reply_to_user_id, " +
                            "U2.first_name as reply_to_user_first_name, " +
                            "U2.last_name as reply_to_user_last_name, " +
                            "M2.date as reply_to_message_date, " +
                            "R2.reply_to_id as reply_to_reply_to_id " +
                            "FROM messages M " +
                            "INNER JOIN users U ON U.id = M.from_user_id " +
                            "LEFT JOIN reply_messages R ON R.message_id = M.id " +
                            "LEFT JOIN messages M2 ON M2.id = R.reply_to_id " +
                            "LEFT JOIN users U2 ON U2.id = M2.from_user_id " +
                            "LEFT JOIN reply_messages R2 ON R2.message_id = R.reply_to_id " +
                            "WHERE M.id = ?");
            PreparedStatement receiversStatement = connection.prepareStatement(
                    "SELECT U.id, U.first_name, U.last_name " +
                            "FROM messages M " +
                            "INNER JOIN messages_receivers MR ON MR.message_id = M.id " +
                            "INNER JOIN users U ON U.id = MR.receiver_id " +
                            "WHERE M.id = ?");
        ) {
            messageStatement.setLong(1, aLong);
            receiversStatement.setLong(1, aLong);

            ResultSet messageResultSet = messageStatement.executeQuery();
            ResultSet receiversResultSet = receiversStatement.executeQuery();
            if(messageResultSet.next()) {
                Message message = createMessageFrom(messageResultSet, receiversResultSet);

                if (message instanceof ReplyMessage replyMessage) {
                    receiversStatement.setLong(1, replyMessage.getMessageRepliedTo().getId());
                    receiversResultSet = receiversStatement.executeQuery();
                    replyMessage.getMessageRepliedTo().setTo(createReceiversFrom(receiversResultSet));
                }

                return Optional.of(message);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement messageStatement = connection.prepareStatement(
                     "SELECT M.id, M.from_user_id, U.first_name, U.last_name, M.message, M.date, " +
                             "R.reply_to_id, M2.message as reply_to_message, " +
                             "M2.from_user_id as reply_to_user_id, " +
                             "U2.first_name as reply_to_user_first_name, " +
                             "U2.last_name as reply_to_user_last_name, " +
                             "M2.date as reply_to_message_date, " +
                             "R2.reply_to_id as reply_to_reply_to_id " +
                             "FROM messages M " +
                             "INNER JOIN users U ON U.id = M.from_user_id " +
                             "LEFT JOIN reply_messages R ON R.message_id = M.id " +
                             "LEFT JOIN messages M2 ON M2.id = R.reply_to_id " +
                             "LEFT JOIN users U2 ON U2.id = M2.from_user_id " +
                             "LEFT JOIN reply_messages R2 ON R2.message_id = R.reply_to_id");
             PreparedStatement receiversStatement = connection.prepareStatement(
                     "SELECT U.id, U.first_name, U.last_name " +
                             "FROM messages M " +
                             "INNER JOIN messages_receivers MR ON MR.message_id = M.id " +
                             "INNER JOIN users U ON U.id = MR.receiver_id " +
                             "WHERE M.id = ?");
        ) {
            ResultSet messageResultSet = messageStatement.executeQuery();

            Set<Message> messages = new HashSet<>();
            while (messageResultSet.next())
            {
                receiversStatement.setLong(1, messageResultSet.getLong("id"));
                ResultSet receiversResultSet = receiversStatement.executeQuery();

                Message message = createMessageFrom(messageResultSet, receiversResultSet);

                if (message instanceof ReplyMessage replyMessage) {
                    receiversStatement.setLong(1, replyMessage.getMessageRepliedTo().getId());
                    receiversResultSet = receiversStatement.executeQuery();
                    replyMessage.getMessageRepliedTo().setTo(createReceiversFrom(receiversResultSet));
                }

                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "select COUNT(*) AS MESSAGE_COUNT from messages")
        ) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt("MESSAGE_COUNT") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }
        if (entity instanceof ReplyMessage) {
            replyMessageValidator.validate((ReplyMessage) entity);
        }
        else {
            messageValidator.validate(entity);
        }

        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement messageStatement = connection.prepareStatement(
                     "INSERT INTO messages(from_user_id, message, date) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement receiversStatement = connection.prepareStatement(
                     "INSERT INTO messages_receivers(message_id, receiver_id) VALUES (?,?)");
             PreparedStatement replyStatement = connection.prepareStatement(
                     "INSERT INTO reply_messages(message_id, reply_to_id) VALUES (?,?)");
        ) {
            messageStatement.setLong(1, entity.getFrom().getId());
            messageStatement.setString(2, entity.getMessage());
            messageStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));

            int response = messageStatement.executeUpdate();

            if (response == 0) {
                return Optional.of(entity);
            }

            ResultSet generatedKeys = messageStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                receiversStatement.setLong(1, generatedKeys.getLong(1));

                for (User receiver : entity.getTo()) {
                    receiversStatement.setLong(2, receiver.getId());
                    response = receiversStatement.executeUpdate();

                    if (response == 0) {
                        throw new SQLException("Failed to save one message receiver.");
                    }
                }

                if (entity instanceof ReplyMessage replyMessage) {
                    replyStatement.setLong(1, generatedKeys.getLong(1));
                    replyStatement.setLong(2, replyMessage.getMessageRepliedTo().getId());

                    response = replyStatement.executeUpdate();

                    if (response == 0) {
                        throw new SQLException("Failed to save reply data.");
                    }
                }
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            return Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM messages M " +
                             "WHERE M.id = ?")
        ) {
            statement.setLong(1, aLong);

            Optional<Message> foundMessage = findOne(aLong);

            int response = 0;
            if (foundMessage.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundMessage;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        if(entity == null) {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        if (entity instanceof ReplyMessage) {
            replyMessageValidator.validate((ReplyMessage) entity);
        }
        else {
            messageValidator.validate(entity);
        }

        try(var connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE messages SET " +
                            "message = ?, date = ? WHERE id = ?")
        ) {
            statement.setString(1, entity.getMessage());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setLong(3, entity.getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
