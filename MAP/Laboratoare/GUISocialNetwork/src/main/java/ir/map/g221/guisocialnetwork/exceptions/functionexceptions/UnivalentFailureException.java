package ir.map.g221.guisocialnetwork.exceptions.functionexceptions;

public class UnivalentFailureException extends FunctionFailureException{
    public UnivalentFailureException() {
        super("Function must be univalent.");
    }
}
