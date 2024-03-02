package ir.map.g221.business.DTOs;

import ir.map.g221.domain.ComplexPart;

public class ParsedResult {
    private final Double value;
    private final ComplexPart complexPart;
    private final int readCharacters;


    public ParsedResult(Double value, int readCharacters, ComplexPart complexPart) {
        this.value = value;
        this.readCharacters = readCharacters;
        this.complexPart = complexPart;
    }

    public static ParsedResult createEmpty() {
        return new ParsedResult(0.0, 0, ComplexPart.REAL_PART);
    }

    public ComplexPart getComplexPart() {
        return complexPart;
    }

    public Double getValue() {
        return value;
    }

    public int getReadCharacters() {
        return readCharacters;
    }
}
