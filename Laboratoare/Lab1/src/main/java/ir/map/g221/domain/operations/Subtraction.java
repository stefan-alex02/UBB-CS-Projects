package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public class Subtraction implements Operation {
    private static Subtraction singleInstance = null;

    public static Subtraction getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new Subtraction();
        }

        return singleInstance;
    }

    @Override
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) {
        return new ComplexNumber(
                c1.re() - c2.re(),
                c1.im() - c2.im());
    }
}
