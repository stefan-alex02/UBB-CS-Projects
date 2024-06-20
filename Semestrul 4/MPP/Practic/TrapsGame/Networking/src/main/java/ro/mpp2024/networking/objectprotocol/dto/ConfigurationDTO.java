package ro.mpp2024.networking.objectprotocol.dto;

import java.io.Serializable;

public record ConfigurationDTO(int row, int column) implements Serializable {
    @Override
    public String toString() {
        return "ConfigurationDTO{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
