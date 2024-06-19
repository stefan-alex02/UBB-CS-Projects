package ro.mpp2024.networking.objectprotocol.dto;


import java.io.Serializable;

public record PairDTO(Integer id, int position1, int position2) implements Serializable {
    @Override
    public String toString() {
        return "PairDTO{" +
                "id=" + id +
                ", position1=" + position1 +
                ", position2=" + position2 +
                '}';
    }
}
