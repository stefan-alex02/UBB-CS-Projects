package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public class Multiplication implements Operation {
    @Override
    public ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2) {
        return new ComplexNumber(
                c1.getRe() * c2.getRe() - c1.getIm() * c2.getIm(),
                c1.getRe() * c2.getIm() + c1.getIm() * c2.getRe());
    }
}
