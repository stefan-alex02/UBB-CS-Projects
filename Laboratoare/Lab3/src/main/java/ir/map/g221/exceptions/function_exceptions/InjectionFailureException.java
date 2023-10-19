package ir.map.g221.exceptions.function_exceptions;

public class InjectionFailureException extends FunctionFailureException {
    public InjectionFailureException() {
        super("Bijective function must also be injective.");
    }
}
