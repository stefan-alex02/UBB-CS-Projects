package ro.mpp2024.networking.objectprotocol.dto;


import java.io.Serializable;
import java.util.Arrays;

public record GameDTO(
    int id,
    UserDTO user,
    TrialDTO[] trials,
    int score,
    int duration,
    ConfigurationDTO[] configurations,
    boolean won
) implements Serializable {
    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", user=" + user +
                ", trials=" + Arrays.toString(trials) +
                ", score=" + score +
                ", duration=" + duration +
                ", configurations=" + Arrays.toString(configurations) +
                ", won=" + won +
                '}';
    }
}
