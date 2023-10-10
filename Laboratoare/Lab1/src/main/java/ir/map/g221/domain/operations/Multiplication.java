package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public class Multiplication implements Operation {
    private static Multiplication singleInstance = null;

    public static Multiplication getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new Multiplication();
        }

        return singleInstance;
    }

    @Override
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) {
        return new ComplexNumber(
                c1.re() * c2.re() - c1.im() * c2.im(),
                c1.re() * c2.im() + c1.im() * c2.re());
    }
}
