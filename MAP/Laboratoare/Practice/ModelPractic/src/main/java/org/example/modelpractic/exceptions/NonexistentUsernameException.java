package org.example.modelpractic.exceptions;

public class NonexistentUsernameException extends RuntimeException {
    public NonexistentUsernameException(String message) {
        super(message);
    }
}
