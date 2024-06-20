package ro.mpp2024.networking.objectprotocol.request;

import ro.mpp2024.networking.objectprotocol.dto.GameDTO;

public record SaveGameRequest(GameDTO game) implements Request {
}
