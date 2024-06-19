package ro.mpp2024.networking.objectprotocol.dto;

import ro.mpp2024.domain.User;

import java.io.Serializable;

public record UserDTO(int id, String alias) implements Serializable {
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                '}';
    }
}
