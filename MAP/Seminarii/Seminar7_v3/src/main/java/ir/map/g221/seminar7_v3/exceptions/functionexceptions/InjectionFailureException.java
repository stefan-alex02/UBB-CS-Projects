package ir.map.g221.seminar7_v3.exceptions.functionexceptions;

public class InjectionFailureException extends FunctionFailureException {
    public InjectionFailureException() {
        super("Bijective function must also be injective.");
    }
}
