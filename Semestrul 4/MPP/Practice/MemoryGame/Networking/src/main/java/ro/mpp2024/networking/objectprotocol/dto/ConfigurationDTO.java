package ro.mpp2024.networking.objectprotocol.dto;

import java.io.Serializable;

public record ConfigurationDTO(int position, String word) implements Serializable {
}
