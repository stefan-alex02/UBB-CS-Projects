package org.example.practic.exceptions;

public class NonexistentUsernameException extends RuntimeException {
    public NonexistentUsernameException(String message) {
        super(message);
    }
}
