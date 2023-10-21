package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public class Division implements Operation {
    private static Division singleInstance = null;

    public static Division getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new Division();
        }

        return singleInstance;
    }

    @Override
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) throws Exception {
        if (Math.pow(c2.re(),2) + Math.pow(c2.im(),2) == 0) {
            throw new Exception("Division by zero.");
        }

        return new ComplexNumber(
                (c1.re() * c2.re() + c1.im() * c2.im()) /
                        (Math.pow(c2.re(),2) + Math.pow(c2.im(),2)),
                (c1.im() * c2.re() - c1.re() * c2.im()) /
                        (Math.pow(c2.re(),2) + Math.pow(c2.im(),2)));
    }
}
