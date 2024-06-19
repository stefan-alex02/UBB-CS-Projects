package ro.mpp2024.networking.objectprotocol.response;

import ro.mpp2024.networking.objectprotocol.dto.GameDTO;

public record GetGamesResponse(GameDTO[] games) implements Response {
}
