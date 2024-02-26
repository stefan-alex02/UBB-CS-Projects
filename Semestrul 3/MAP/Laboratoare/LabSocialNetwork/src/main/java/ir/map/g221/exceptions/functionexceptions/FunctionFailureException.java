package ir.map.g221.exceptions.functionexceptions;

public abstract class FunctionFailureException extends RuntimeException {
    public FunctionFailureException(String message) {
        super(message);
    }
}
