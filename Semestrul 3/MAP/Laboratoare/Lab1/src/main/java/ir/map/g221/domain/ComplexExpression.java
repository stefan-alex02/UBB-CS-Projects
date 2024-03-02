package ir.map.g221.domain;

import ir.map.g221.domain.operations.Operation;

import java.util.ArrayList;
import java.util.List;

public class ComplexExpression {
    private List<ComplexNumber> numbers;
    private Operation operation;

    public ComplexExpression(List<ComplexNumber> numbers, Operation operation) {
        this.numbers = numbers;
        this.operation = operation;
    }

    public static ComplexExpression createExpression(List<ComplexNumber> numbers, Operation operation) {
        return new ComplexExpression(numbers, operation);
    }

    public ComplexNumber evaluate() throws Exception {
        if (numbers.isEmpty()) {
            throw new Exception("Invalid number of operands!");
        }

        ComplexNumber result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = operation.calculate(result, numbers.get(i));
        }

        return result;
    }
}
