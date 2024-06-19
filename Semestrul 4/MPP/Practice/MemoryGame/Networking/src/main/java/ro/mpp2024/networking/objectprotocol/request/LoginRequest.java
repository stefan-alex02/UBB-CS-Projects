package ro.mpp2024.networking.objectprotocol.request;

import ro.mpp2024.networking.objectprotocol.dto.UserDTO;

public record LoginRequest(UserDTO user) implements Request {
}
