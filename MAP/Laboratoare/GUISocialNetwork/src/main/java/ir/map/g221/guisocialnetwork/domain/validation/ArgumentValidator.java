package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.exceptions.ValidationException;

public class ArgumentValidator implements Validator<Object>{
    private static ArgumentValidator instance = null;

    private ArgumentValidator() {
    }
    public static ArgumentValidator getInstance() {
        if (instance == null) {
            instance = new ArgumentValidator();
        }
        return instance;
    }

    @Override
    public void validate(Object argument) throws ValidationException {
        if (argument == null) {
            throw new IllegalArgumentException("Argument of given type must not be null.");
        }
    }
}
