package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.domain.entities.ReplyMessage;

import java.util.List;

public class ReplyMessageValidator implements Validator<ReplyMessage> {
    private static ReplyMessageValidator instance = null;

    private ReplyMessageValidator() {
    }

    public static ReplyMessageValidator getInstance() {
        if (instance == null) {
            instance = new ReplyMessageValidator();
        }
        return instance;
    }

    @Override
    public List<String> findErrors(ReplyMessage replyMessage) {
        List<String> errors = MessageValidator.getInstance().findErrors(replyMessage);

        if (replyMessage.getMessageRepliedTo() == null) {
            errors.add("Replied message cannot be null.");
        }
        else if (replyMessage.getMessageRepliedTo() == replyMessage) {
            errors.add("Replied message cannot be the reply message itself.");
        }

        return errors;
    }
}
