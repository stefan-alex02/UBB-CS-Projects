package org.example.modelpractic.exceptions;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super("Validation Error: " + message);
    }
}
