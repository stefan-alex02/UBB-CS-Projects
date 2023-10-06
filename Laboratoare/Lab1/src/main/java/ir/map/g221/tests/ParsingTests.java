package ir.map.g221.tests;

import ir.map.g221.domain.ExpressionParser;
import java.util.Scanner;

public class ParsingTests {
    public static void RunTests() {
        assertCorrectInput("-90.098+8.9909*i", -90.098, 8.9909);
        assertCorrectInput("+1.002*i", 0, 1.002);
        assertCorrectInput("-i", 0, -1);

        assertWrongInput("+*i");
        assertWrongInput("1+1");
        assertWrongInput("1.00a+2*i");
        assertWrongInput("+");
    }

    private static void assertCorrectInput(String input, double re, double im) {
        try {
            var number = ExpressionParser.parseComplexNumber(input);
            assert number.getRe() == re : "Failed test";
            assert number.getIm() == im : "Failed test";
        }
        catch(Exception e) {
            assert false : "Failed test";
        }
    }

    private static void assertWrongInput(String input) {
        try {
            ExpressionParser.parseComplexNumber(input);
            assert false : "Failed test";
        }
        catch(Exception e) {
            assert true;
        }
    }
}
