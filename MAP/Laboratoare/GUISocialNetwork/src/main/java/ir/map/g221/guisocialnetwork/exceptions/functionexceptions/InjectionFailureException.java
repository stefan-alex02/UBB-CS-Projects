package ir.map.g221.guisocialnetwork.exceptions.functionexceptions;

public class InjectionFailureException extends FunctionFailureException {
    public InjectionFailureException() {
        super("Bijective function must also be injective.");
    }
}
