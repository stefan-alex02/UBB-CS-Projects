package ir.map.g221.tests;

import ir.map.g221.domain.ExpressionParser;
import java.util.Scanner;

public class ParsingTests {
    public static void RunTests() {
        try {
            var x = ExpressionParser.parseComplexNumber("-90.098+8.9909*i");
            assert x.getRe() == -90.098 : "Failed";
            assert x.getIm() == 8.9909 : "Failed";
        }
        catch(Exception e) {
            assert false;
        }
    }
}
