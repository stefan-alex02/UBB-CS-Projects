package ir.map.g221.domain;

import ir.map.g221.domain.operations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class ExpressionParser {
    private final static String[] operations = { "+", "-", "'*'", "/" };
    private final static Map<String, Operation> operationDictionary = new Hashtable<>(){{
        put("+", Addition.getSingleInstance());
        put("-", Subtraction.getSingleInstance());
        put("'*'", Multiplication.getSingleInstance());
        put("/", Division.getSingleInstance());
    }};

    private static boolean validate(ArrayList<String> args, String operation) {
        if (!Arrays.asList(operations).contains(operation)) {
            return false;
        }

        return args.stream()
                .allMatch(s -> s.matches("(^[/+-]?[0-9]+(.[0-9]+)?[/+-]([0-9]+(.[0-9]+)?[/*])?i$)|" +
                        "(^[/+-]?[0-9]+(.[0-9]+)?$)|(^[/+-]?([0-9]+(.[0-9]+)?[/*])?i$)"));
    }

    private static Double getDouble(String s) {
        // Case 0 : string is empty

        // Case 1 : number starts with a sign (+/-) character :
        if (s.charAt(0) == '+') {

        }

        // Cases 2 : number only has decimal characters :
        return 0.0;
    }

    public static ComplexNumber parseComplexNumber(String s) throws Exception {
        // Case 1 : both real and imaginary part
        // Sub-cases : a+b*i or -a+b*i or a+i or +a+i
        // Sub-case 1.1 : number starts with a sign

        double re, im;
        int i, pi;

        if (s.charAt(0) != 'i' && (s.length() < 2 || s.charAt(1) != 'i')) {
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

    public static ComplexExpression parseExpression(String[] args) throws Exception {
        ArrayList<String> notParsedNumbers = new ArrayList<>(Arrays.asList(args).subList(0, args.length - 1));
        String operation = args[args.length - 1];

        if (!validate(notParsedNumbers, operation)) {
            throw new Exception("Invalid expression syntax!");
        }

        ArrayList<ComplexNumber> parsedNumbers = new ArrayList<>();
        for (var notParsedNumber : notParsedNumbers) {
            parsedNumbers.add(parseComplexNumber(notParsedNumber));
        };

        return ComplexExpression.createExpression(parsedNumbers, operationDictionary.get(operation));
    }
}
