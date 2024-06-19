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
        return new TrialDTO(entity.getId().getRow(), entity.getId().getColumn());
    }

    public static Trial getFromDTO(TrialDTO entity) {
        return new Trial(new Trial.TrialId(0, entity.row(), entity.column()), null);
    }

    public static TrialDTO[] getDTO(Trial[] trials){
        TrialDTO[] pairsDTO = new TrialDTO[trials.length];
        for (int i = 0; i < trials.length; i++)
            pairsDTO[i] = getDTO(trials[i]);
        return pairsDTO;
    }

    public static Trial[] getFromDTO(TrialDTO[] pairsDTO){
        Trial[] trials = new Trial[pairsDTO.length];
        for (int i = 0; i < pairsDTO.length; i++){
            trials[i] = getFromDTO(pairsDTO[i]);
        }
        return trials;
    }

    public static Configuration getFromDTO(ConfigurationDTO entity) {
        return new Configuration(
                new Configuration.ConfigurationId(null, entity.row(), entity.column()),
                null);
    }

    public static ConfigurationDTO getDTO(Configuration entity) {
        return new ConfigurationDTO(entity.getId().getRow(), entity.getId().getColumn());
    }

    public static ConfigurationDTO[] getDTO(Configuration[] configurations){
        ConfigurationDTO[] configurationDTOS = new ConfigurationDTO[configurations.length];
        for (int i = 0; i < configurations.length; i++)
            configurationDTOS[i] = getDTO(configurations[i]);
        return configurationDTOS;
    }

    public static Configuration[] getFromDTO(ConfigurationDTO[] configurationDTOS){
        Configuration[] configurations = new Configuration[configurationDTOS.length];
        for (int i = 0; i < configurationDTOS.length; i++){
            configurations[i] = getFromDTO(configurationDTOS[i]);
        }
        return configurations;
    }

    public static Game getFromDTO(GameDTO dto) {
        var pairs = getFromDTO(dto.trials());
        var user = getFromDTO(dto.user());
        var configurations = DTOUtils.getFromDTO(dto.configurations());
        Game game = new Game(dto.id(),
                user,
                new ArrayList<>(Arrays.asList(pairs)),
                dto.score(),
                dto.duration(),
                List.of(configurations));
        game.getPairs().forEach(p -> p.setGame(game));
        game.getConfigurations().forEach(c -> c.setGame(game));
        return game;
    }

    public static GameDTO getDTO(Game entity) {
        TrialDTO[] pairs = getDTO(entity.getPairs().toArray(new Trial[0]));
        UserDTO user = getDTO(entity.getUser());
        ConfigurationDTO[] configurations = getDTO(entity.getConfigurations().toArray(new Configuration[0]));
        return new GameDTO(entity.getId(), user, pairs, entity.getScore(), entity.getDuration(), configurations);
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
