package ir.map.g221.domain;

import ir.map.g221.domain.operations.*;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class ExpressionParser {
    private final static String[] operations = { "+", "-", "'*'", "/" };
    private final static Map<String, Operation> operationDictionary = new Hashtable<>(){{
        put("+", new Addition());
        put("-", new Subtraction());
        put("'*'", new Multiplication());
        put("/", new Division());
    }};
    private static boolean validate(ArrayList<String> args) {
        if (!Arrays.asList(operations).contains(args.get(args.size()-1))) {
            return false;
        }

        return args.subList(0, args.size() - 1).stream()
                .allMatch(s -> s.matches("(^[/+-]?[0-9]+(.[0-9]+)?[/+-]([0-9]+(.[0-9]+)?[/*])?i$)|" +
                        "(^[/+-]?[0-9]+(.[0-9]+)?$)|(^[/+-]?([0-9]+(.[0-9]+)?[/*])?i$)"));
    }

    public static ComplexNumber parseComplexNumber(String s) throws Exception {
        double re, im;
        int i, pi;

        if (s.charAt(0) != 'i' || s.charAt(1) != 'i') {
            for (i = 1; i < s.length() &&
                    (Character.isDigit(s.charAt(i)) ||
                            s.charAt(i) == '.'); i++);
            if (i == s.length()) {
                re = Double.parseDouble(s.substring(0, i));
                im = 0;
            }
            else if (s.charAt(i) != '*') {
                re = Double.parseDouble(s.substring(0, i));

                pi = i;
                for (; i < s.length() &&
                        (Character.isDigit(s.charAt(i)) ||
                                s.charAt(i) == '-' ||
                                s.charAt(i) == '+' ||
                                s.charAt(i) == '.'); i++);

                if (s.charAt(i) != 'i') {
                    im = Double.parseDouble(s.substring(pi, i));
                }
                else {
                    im = (s.charAt(i-1) == '-' ? -1 : 1);
                }
            }
            else {
                re = 0;
                im = Double.parseDouble(s.substring(0, i));
            }
        }
        else {
            re = 0;
            im = (s.charAt(0) == '-' ? -1 : 1);
        }

        return new ComplexNumber(re, im);
    }

    public static ComplexExpression parse(String[] args) throws Exception {
        var argsArray = new ArrayList<>(Arrays.asList(args));
        if (!validate(argsArray)) {
            throw new Exception("Invalid expression syntax!");
        }

        ArrayList<ComplexNumber> numbers = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            numbers.add(parseComplexNumber(args[i]));
        };
        return ComplexExpression.createExpression(numbers, operationDictionary.get(argsArray.get(argsArray.size() - 1)));
    }
}
