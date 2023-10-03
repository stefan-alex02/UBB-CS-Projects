package ir.map.g221.domain.operations;

import ir.map.g221.domain.ComplexNumber;

public interface Operation {
    ComplexNumber calculate(ComplexNumber c1, ComplexNumber c2);
}
