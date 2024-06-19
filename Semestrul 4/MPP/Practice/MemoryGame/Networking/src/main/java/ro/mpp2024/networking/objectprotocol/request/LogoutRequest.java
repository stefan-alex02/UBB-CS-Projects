package ro.mpp2024.networking.objectprotocol.request;

import ro.mpp2024.networking.objectprotocol.dto.UserDTO;

public record LogoutRequest(UserDTO user) implements Request {
}
