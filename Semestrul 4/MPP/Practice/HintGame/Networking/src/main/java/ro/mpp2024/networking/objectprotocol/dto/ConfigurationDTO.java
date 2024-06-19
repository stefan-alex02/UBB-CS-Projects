package ro.mpp2024.networking.objectprotocol.dto;

import java.io.Serializable;

public record ConfigurationDTO(int id, int row, int column, String hint) implements Serializable {
}
