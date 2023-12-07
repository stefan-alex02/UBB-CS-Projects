package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestValidator implements Validator<FriendRequest> {
    private static FriendRequestValidator instance = null;

    protected FriendRequestValidator() {
    }
    public static FriendRequestValidator getInstance() {
        if (instance == null) {
            instance = new FriendRequestValidator();
        }
        return instance;
    }

    @Override
    public List<String> findErrors(FriendRequest friendRequest) {
        List<String> errors = new ArrayList<>();

        if (friendRequest.getId() < 0) {
            errors.add("Id must not be a negative number.");
        }

        if (friendRequest.getFrom() == null) {
            errors.add("Sender cannot be null.");
        }

        if (friendRequest.getTo() == null) {
            errors.add("Receiver cannot be null.");
        }

        if (friendRequest.getDate() == null) {
            errors.add("Date cannot be null.");
        }
        else if (friendRequest.getDate().isBefore(LocalDate.parse("1900-01-01").atStartOfDay())) {
            errors.add("Friend request date is too old.");
        }

        return errors;
    }
}