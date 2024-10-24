package org.example.validation;


import org.example.domain.User;
import org.example.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UserValidator implements Validator<User>{
    private static UserValidator instance = null;

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        if (instance == null) {
            instance = new UserValidator();
        }
        return instance;
    }

    @Override
    public void validate(User entity) throws ValidationException {
        List<String> errors = new ArrayList<>();

        if (entity.getId() < 0) {
            errors.add("Id must not be a negative number.");
        }

        if (entity.getFirstName().isEmpty()) {
            errors.add("First name must not be empty.");
        }

        if (entity.getLastName().isEmpty()) {
            errors.add("Last name must not be empty.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
