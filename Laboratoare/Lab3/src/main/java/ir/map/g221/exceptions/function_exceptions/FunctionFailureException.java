package ir.map.g221.exceptions.function_exceptions;

public abstract class FunctionFailureException extends RuntimeException {
    public FunctionFailureException(String message) {
        super(message);
    }
}
