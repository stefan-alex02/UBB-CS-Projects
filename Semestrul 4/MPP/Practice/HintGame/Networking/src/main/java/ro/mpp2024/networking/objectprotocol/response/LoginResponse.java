package ro.mpp2024.networking.objectprotocol.response;

import ro.mpp2024.networking.objectprotocol.dto.GameDTO;

public record LoginResponse(GameDTO gameDTO) implements Response {
}
