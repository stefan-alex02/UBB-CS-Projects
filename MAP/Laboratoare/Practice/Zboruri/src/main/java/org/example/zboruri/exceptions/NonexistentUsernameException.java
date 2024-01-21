package org.example.zboruri.exceptions;

public class NonexistentUsernameException extends RuntimeException {
    public NonexistentUsernameException(String message) {
        super(message);
    }
}
