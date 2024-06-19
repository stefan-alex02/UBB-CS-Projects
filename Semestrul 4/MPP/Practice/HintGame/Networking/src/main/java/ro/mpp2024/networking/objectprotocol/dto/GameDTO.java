package ro.mpp2024.networking.objectprotocol.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

public record GameDTO(
    int id,
    UserDTO user,
    TrialDTO[] trials,
    ConfigurationDTO configuration,
    LocalDateTime startDateTime,
    String hint
) implements Serializable {
    @Override
    public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", user=" + user +
                ", trials=" + Arrays.toString(trials) +
                '}';
    }
}
