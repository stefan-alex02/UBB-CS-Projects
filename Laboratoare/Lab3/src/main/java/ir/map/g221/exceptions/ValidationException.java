package ir.map.g221.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errorList;

    public ValidationException(List<String> errorList) {
        super(String.join("\n", errorList));

        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return String.join("\n", errorList);
    }
}
