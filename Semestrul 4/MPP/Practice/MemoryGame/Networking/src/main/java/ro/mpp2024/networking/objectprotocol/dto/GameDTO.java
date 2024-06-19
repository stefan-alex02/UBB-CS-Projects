package ro.mpp2024.networking.objectprotocol.dto;


import java.io.Serializable;
import java.util.Arrays;

public record GameDTO(
    int id,
    UserDTO user,
    PairDTO[] pairs,
    int score,
    int duration,
    ConfigurationDTO[] configurations
) implements Serializable {
    @Override
    public String toString() {
        return "GameDTO{" +
                "user=" + user +
                ", pairs=" + Arrays.toString(pairs) +
                ", score=" + score +
                ", duration=" + duration +
                ", configuration=" + Arrays.toString(configurations) +
                '}';
    }
}
