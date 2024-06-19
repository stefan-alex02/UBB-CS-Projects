package ro.mpp2024.networking.objectprotocol.dto;


import java.io.Serializable;

public record TrialDTO(int row, int column, Character symbol) implements Serializable {
    @Override
    public String toString() {
        return "TrialDTO{" +
                "row=" + row +
                ", column=" + column +
                ", symbol=" + symbol +
                '}';
    }
}
