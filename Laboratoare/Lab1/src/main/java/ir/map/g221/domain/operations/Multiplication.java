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
                c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm(),
                c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe());
    }
}
