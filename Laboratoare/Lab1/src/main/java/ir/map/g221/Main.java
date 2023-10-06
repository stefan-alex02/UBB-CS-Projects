package ir.map.g221;

import ir.map.g221.domain.ComplexExpression;
import ir.map.g221.business.ExpressionParser;
import ir.map.g221.tests.ExpressionEvaluationTests;
import ir.map.g221.tests.ParsingTests;

public class Main {

    public static void main(String[] args) {
        ParsingTests.RunTests();
        ExpressionEvaluationTests.RunTests();
        System.out.println("All tests passed successfully!");

        try {
            var expression = build(args);
            System.out.println(expression.evaluate());
        }
        catch (Exception e) {
            System.out.println("Error while calculating expression:\n" +
                    e.getMessage());
        }
    }

    private static ComplexExpression build(String[] args) throws Exception {
        try {
            return ExpressionParser.parseExpression(args);
        }
        catch(Exception e) {
            System.out.println("Error while attempting to parse argument strings.");
            throw e;
        }
    }
}