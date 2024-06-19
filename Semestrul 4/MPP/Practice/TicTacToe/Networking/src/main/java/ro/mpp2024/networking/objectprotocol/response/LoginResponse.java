package ro.mpp2024.networking.objectprotocol.response;

import ro.mpp2024.networking.objectprotocol.dto.UserDTO;

public record LoginResponse(UserDTO userDTO) implements Response {
}
