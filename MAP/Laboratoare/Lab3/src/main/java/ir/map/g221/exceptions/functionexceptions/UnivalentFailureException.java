package ir.map.g221.exceptions.functionexceptions;

public class UnivalentFailureException extends FunctionFailureException{
    public UnivalentFailureException() {
        super("Function must be univalent.");
    }
}
