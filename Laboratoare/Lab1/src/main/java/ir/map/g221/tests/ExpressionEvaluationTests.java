package ir.map.g221.tests;

import ir.map.g221.domain.ComplexExpression;
import ir.map.g221.domain.ComplexNumber;
import ir.map.g221.domain.operations.Addition;
import ir.map.g221.domain.operations.Division;
import ir.map.g221.domain.operations.Operation;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpressionEvaluationTests {
    public static void RunTests() {
        assertCorrectExpression(
                new ArrayList<>(Arrays.asList(
                        new ComplexNumber(2, 0),
                        new ComplexNumber(-1, 2)
                )),
                Addition.getSingleInstance(),
                new ComplexNumber(1, 2));

        assertWrongExpression(
                new ArrayList<>(Arrays.asList(
                        new ComplexNumber(2, 2),
                        new ComplexNumber(0, 0)
                )),
                Division.getSingleInstance());


    }

    private static void assertCorrectExpression(ArrayList<ComplexNumber> numbers, Operation operation, ComplexNumber expected) {
        try {
            var expression = ComplexExpression.createExpression(numbers, operation);
            var actual = expression.evaluate();
            assert expected.equals(actual): "Failed test";
        }
        catch(Exception e) {
            assert false : "Failed test";
        }
    }

    private static void assertWrongExpression(ArrayList<ComplexNumber> numbers, Operation operation) {
        try {
            var expression = ComplexExpression.createExpression(numbers, operation);
            expression.evaluate();
            assert false : "Failed test";
        }
        catch(Exception e) {
            assert true;
        }
    }
}
