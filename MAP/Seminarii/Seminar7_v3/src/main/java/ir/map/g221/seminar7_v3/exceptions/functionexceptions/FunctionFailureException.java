package ir.map.g221.seminar7_v3.exceptions.functionexceptions;

public abstract class FunctionFailureException extends RuntimeException {
    public FunctionFailureException(String message) {
        super(message);
    }
}
