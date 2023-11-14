package ir.map.g221.seminar7_v3.exceptions.functionexceptions;

public class UnivalentFailureException extends FunctionFailureException{
    public UnivalentFailureException() {
        super("Function must be univalent.");
    }
}
