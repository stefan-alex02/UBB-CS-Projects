package ro.mpp2024.networking.objectprotocol.dto;

import ro.mpp2024.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DTOUtils {
    public static User getFromDTO(UserDTO entity) {
        return new User(entity.id(), entity.alias());
    }

    public static UserDTO getDTO(User entity) {
        return new UserDTO(entity.getId(), entity.getAlias());
    }

    public static TrialDTO getDTO(Trial entity) {
        return new TrialDTO(entity.getId().getRow(), entity.getId().getColumn(), entity.getSymbol());
    }

    public static Trial getFromDTO(TrialDTO entity) {
        return new Trial(new Trial.TrialId(null,
                entity.row(),
                entity.column()), entity.symbol(), null);
    }

    public static TrialDTO[] getDTO(Trial[] trials){
        TrialDTO[] pairsDTO = new TrialDTO[trials.length];
        for (int i = 0; i < trials.length; i++)
            pairsDTO[i] = getDTO(trials[i]);
        return pairsDTO;
    }

    public static Trial[] getFromDTO(TrialDTO[] trialsDTO){
        Trial[] trials = new Trial[trialsDTO.length];
        for (int i = 0; i < trialsDTO.length; i++){
            trials[i] = getFromDTO(trialsDTO[i]);
        }
        return trials;
    }

    public static Game getFromDTO(GameDTO dto) {
        var trials = getFromDTO(dto.trials());
        var user = getFromDTO(dto.user());
        Game game = new Game(dto.id(),
                user,
                new ArrayList<>(Arrays.asList(trials)),
                dto.score(),
                dto.duration());
        game.getTrials().forEach(p -> p.setGame(game));
        return game;
    }

    public static GameDTO getDTO(Game entity) {
        TrialDTO[] trialsDTO = getDTO(entity.getTrials().toArray(new Trial[0]));
        UserDTO user = getDTO(entity.getUser());
        return new GameDTO(entity.getId(), user, trialsDTO, entity.getScore(), entity.getDuration());
    }

    public static GameDTO[] getDTO(Collection<Game> entities) {
        return entities.stream()
                .map(DTOUtils::getDTO)
                .toArray(GameDTO[]::new);
    }

    public static GameDTO[] getDTO(Game[] games){
        GameDTO[] gamesDTO = new GameDTO[games.length];
        for (int i = 0; i < games.length; i++)
            gamesDTO[i] = getDTO(games[i]);
        return gamesDTO;
    }

    public static Game[] getFromDTO(GameDTO[] gamesDTO){
        Game[] games = new Game[gamesDTO.length];
        for (int i = 0; i < gamesDTO.length; i++){
            games[i] = getFromDTO(gamesDTO[i]);
        }
        return games;
    }
}
