package ir.map.g221.seminar7_v3.domain.validation;

import ir.map.g221.seminar7_v3.exceptions.ValidationException;

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
