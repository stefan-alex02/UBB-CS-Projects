package ir.map.g221.domain.validation;

import ir.map.g221.domain.Friendship;
import ir.map.g221.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship entity) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (entity.getId().getFirst() < 0) {
            errors.add("First Id must not be a negative number.");
        }

        if (entity.getId().getSecond() < 0) {
            errors.add("Second Id must not be a negative number.");
        }

        if (entity.getId().getFirst().equals(entity.getId().getSecond())) {
            errors.add("The Ids must not have the same value.");
        }

        if (entity.getCreationDate().isBefore(LocalDate.parse("1900-01-01").atStartOfDay())) {
            errors.add("Creation date is too old.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
