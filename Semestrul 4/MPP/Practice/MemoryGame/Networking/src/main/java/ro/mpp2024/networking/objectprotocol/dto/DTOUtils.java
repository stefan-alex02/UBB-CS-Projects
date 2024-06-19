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

    public static PairDTO getDTO(Pair entity) {
        return new PairDTO(entity.getId(), entity.getPosition1(), entity.getPosition2());
    }

    public static Pair getFromDTO(PairDTO entity) {
        return new Pair(0, entity.position1(), entity.position2(), null);
    }

    public static PairDTO[] getDTO(Pair[] pairs){
        PairDTO[] pairsDTO = new PairDTO[pairs.length];
        for (int i = 0; i < pairs.length; i++)
            pairsDTO[i] = getDTO(pairs[i]);
        return pairsDTO;
    }

    public static Pair[] getFromDTO(PairDTO[] pairsDTO){
        Pair[] pairs = new Pair[pairsDTO.length];
        for (int i = 0; i < pairsDTO.length; i++){
            pairs[i] = getFromDTO(pairsDTO[i]);
        }
        return pairs;
    }

    public static Configuration getFromDTO(ConfigurationDTO entity) {
        return new Configuration(
                new Configuration.ConfigurationId(null, entity.position()),
                null,
                new Word(entity.word()));
    }

    public static ConfigurationDTO getDTO(Configuration entity) {
        return new ConfigurationDTO(entity.getId().getPosition(), entity.getWord().getId());
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
        var pairs = getFromDTO(dto.pairs());
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
        PairDTO[] pairs = getDTO(entity.getPairs().toArray(new Pair[0]));
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
