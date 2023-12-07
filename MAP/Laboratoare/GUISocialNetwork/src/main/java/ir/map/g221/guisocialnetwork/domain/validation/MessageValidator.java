package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.domain.entities.Message;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageValidator implements Validator<Message> {
    private static MessageValidator instance = null;

    protected MessageValidator() {
    }
    public static MessageValidator getInstance() {
        if (instance == null) {
            instance = new MessageValidator();
        }
        return instance;
    }

    @Override
    public List<String> findErrors(Message message) {
        List<String> errors = new ArrayList<>();

        if (message.getId() < 0) {
            errors.add("Id must not be a negative number.");
        }

        if (message.getFrom() == null) {
            errors.add("Sender cannot be null.");
        }

        if (message.getTo().isEmpty()) {
            errors.add("Receivers list cannot be empty.");
        }
        else if (message.getTo().contains(message.getFrom())) {
            errors.add("Cannot send message to sender themselves.");
        }

        if (message.getMessage().isEmpty()) {
            errors.add("Message text cannot be empty.");
        }

        if (message.getDate() == null) {
            errors.add("Date cannot be null.");
        }
        else if (message.getDate().isBefore(LocalDate.parse("1900-01-01").atStartOfDay())) {
            errors.add("Message date is too old.");
        }

        return errors;
    }
}
