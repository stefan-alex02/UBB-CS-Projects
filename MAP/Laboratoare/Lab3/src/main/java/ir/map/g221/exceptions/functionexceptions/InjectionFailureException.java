package ir.map.g221.exceptions.functionexceptions;

public class InjectionFailureException extends FunctionFailureException {
    public InjectionFailureException() {
        super("Bijective function must also be injective.");
    }
}
