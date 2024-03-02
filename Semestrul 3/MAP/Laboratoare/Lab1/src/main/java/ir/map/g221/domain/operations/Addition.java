package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public class Addition implements Operation {
    private static Addition singleInstance = null;

    public static Addition getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new Addition();
        }

        return singleInstance;
    }

    @Override
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) {
        return new ComplexNumber(
                c1.re() + c2.re(),
                c1.im() + c2.im());
    }
}
