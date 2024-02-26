package ir.map.g221.business;

import ir.map.g221.business.DTOs.ParsedResult;
import ir.map.g221.domain.ComplexPart;
import ir.map.g221.domain.ComplexExpression;
import ir.map.g221.domain.ComplexNumber;
import ir.map.g221.domain.operations.*;

import java.util.*;

public class ExpressionParser {
    private final static String[] operations = { "+", "-", "'*'", "/" };
    private final static Map<String, Operation> operationDictionary = new Hashtable<>(){{
        put("+", Addition.getSingleInstance());
        put("-", Subtraction.getSingleInstance());
        put("'*'", Multiplication.getSingleInstance());
        put("/", Division.getSingleInstance());
    }};

    private static boolean validateInput(ArrayList<String> args, String operation) {
        if (!Arrays.asList(operations).contains(operation)) {
            return false;
        }

        return args.stream().allMatch(ExpressionParser::validateComplexNumber);
    }

    public static boolean validateComplexNumber(String complexNumberString) {
        return complexNumberString.matches("(^[+-]?[0-9]+(\\.[0-9]+)?[+-]([0-9]+(\\.[0-9]+)?\\*)?i$)" +
                "|(^[+-]?[0-9]+(\\.[0-9]+)?$)|(^[+-]?([0-9]+(\\.[0-9]+)?\\*)?i$)");
    }

    private static ParsedResult parseDouble(String s) throws Exception {
        int firstIndex = 0, secondIndex = 0;
        boolean negative = false;
        double result;

        // Case 0 : the string is empty :
        if (s.isEmpty()) {
            return ParsedResult.createEmpty();
        }

        // Case 1 : the string starts with 'i' character :
        if (s.charAt(0) == 'i') {
            return new ParsedResult(1.0, 1, ComplexPart.IMAGINARY_PART);
        }

        // Case 2 : the string starts with a sign : (which must be followed by (an)other character(s) :
        boolean hasSign = s.charAt(0) == '+' || s.charAt(0) == '-';
        if (hasSign) {
            // Subcase 2.1 : if the sign is followed by 'i', return the value :
            if (s.charAt(1) == 'i') {
                return new ParsedResult((s.charAt(0) == '-' ? -1.0 : 1.0),
                        2,
                        ComplexPart.IMAGINARY_PART);
            }

            // Subcase 2.1 : Incrementing the second index for parsing the number in the algorithm below :
            secondIndex++;
        }

        // Case 3 : the string begins with some digits :
        // Going through all the string till the end of the number, in order to determine the last digit character :
        while (secondIndex < s.length() &&
                (s.charAt(secondIndex) >= '0' && s.charAt(secondIndex) <= '9' || s.charAt(secondIndex) == '.')) {
            char indexedChar = s.charAt(secondIndex);
            secondIndex++;
        }

        // By reaching the end of the number, we can parse the double :
        result = Double.parseDouble(s.substring(firstIndex, secondIndex));

        // If we reached the end of the string or if the index past the number is at a sign, then the parsed number
        // is a real part :
        if (secondIndex >= s.length() || s.charAt(secondIndex) == '+' || s.charAt(secondIndex) == '-') {
            return new ParsedResult(result,
                    secondIndex,
                    ComplexPart.REAL_PART);
        }

        // If not, then it must be an imaginary part, followed by a '*' :
        if (s.charAt(secondIndex) == '*') {
            return new ParsedResult(result,
                    secondIndex,
                    ComplexPart.IMAGINARY_PART);
        }

        // This part of code should not be executed:
        throw new Exception("String could not be parsed correctly.");
    }

    public static ComplexNumber parseComplexNumber(String str) throws Exception {
        double re, im;
        ParsedResult parsedResult;

        // Step 1 : parse the first number :
        parsedResult = parseDouble(str);

        // Step 2.1 : if the number represents an imaginary part, then return the complex number :
        if (parsedResult.getComplexPart() == ComplexPart.IMAGINARY_PART) {
            return new ComplexNumber(0, parsedResult.getValue());
        }

        // Step 2.2 : if the number represents a real part instead, remove the parsed-characters from the string :
        str = str.substring(parsedResult.getReadCharacters());

        // Assign the parsed result to a local variable :
        re = parsedResult.getValue();

        // Step 3 : parse the second number (if it exists) :
        parsedResult = parseDouble(str);

        // Step 4.1 : if there were no read characters, then there is no imaginary part :
        if (parsedResult.getReadCharacters() == 0) {
            return new ComplexNumber(re, 0);
        }

        // Step 4.2 : if there are some parsed characters, assign the result to a local variable and
        //            return the number :
        im = parsedResult.getValue();

        return new ComplexNumber(re, im);
    }

    public static ComplexExpression parseExpression(String[] args) throws Exception {
        ArrayList<String> notParsedNumbers = new ArrayList<>(Arrays.asList(args).subList(0, args.length - 1));
        String operation = args[args.length - 1];

        if (!validateInput(notParsedNumbers, operation)) {
            throw new Exception("Invalid expression syntax!");
        }

        List<ComplexNumber> parsedNumbers = new ArrayList<>();
        for (var notParsedNumber : notParsedNumbers) {
            parsedNumbers.add(parseComplexNumber(notParsedNumber));
        };

        return ComplexExpression.createExpression(parsedNumbers, operationDictionary.get(operation));
    }
}
