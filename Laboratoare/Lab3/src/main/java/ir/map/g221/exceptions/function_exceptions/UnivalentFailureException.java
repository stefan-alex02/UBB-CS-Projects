package ir.map.g221.exceptions.function_exceptions;

public class UnivalentFailureException extends FunctionFailureException{
    public UnivalentFailureException() {
        super("Function must be univalent.");
    }
}
