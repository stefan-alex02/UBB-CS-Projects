package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;

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
    public List<String> findErrors(User entity) {
        List<String> errors = new ArrayList<>();

        if (entity.getId() < 0) {
            errors.add("Id must not be a negative number.");
        }

        if (entity.getUsername().isEmpty()) {
            errors.add("Username must not be empty.");
        }

        if (entity.getFirstName().isEmpty()) {
            errors.add("First name must not be empty.");
        }

        if (entity.getLastName().isEmpty()) {
            errors.add("Last name must not be empty.");
        }

        if (entity.getPassword().isEmpty()) {
            errors.add("Password must not be empty.");
        }
        else if (entity.getPassword().length() < 6) {
            errors.add("Password is too weak.");
        }

        return errors;
    }
}
