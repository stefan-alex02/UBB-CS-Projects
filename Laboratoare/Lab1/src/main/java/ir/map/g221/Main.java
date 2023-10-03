package ir.map.g221;

import ir.map.g221.domain.ExpressionParser;
import ir.map.g221.tests.ParsingTests;

public class Main {

    public static void main(String[] args) {
        ParsingTests.RunTests();

        try {
            System.out.println(ExpressionParser.parse(args).evaluate());
        }
        catch (Exception e) {
            System.out.println("An error has occurred!");
        }
    }
}