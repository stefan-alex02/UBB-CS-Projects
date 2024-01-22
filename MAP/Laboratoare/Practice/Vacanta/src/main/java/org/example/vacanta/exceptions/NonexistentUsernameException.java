package org.example.vacanta.exceptions;

public class NonexistentUsernameException extends RuntimeException {
    public NonexistentUsernameException(String message) {
        super(message);
    }
}
