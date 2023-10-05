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
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) {
        return new ComplexNumber(
                (c1.getRe() * c2.getRe() + c1.getIm() * c2.getIm()) /
                        (Math.pow(c2.getRe(),2) + Math.pow(c2.getIm(),2)),
                (c1.getIm() * c2.getRe() - c1.getRe() * c2.getIm()) /
                        (Math.pow(c2.getRe(),2) + Math.pow(c2.getIm(),2)));
    }
}
