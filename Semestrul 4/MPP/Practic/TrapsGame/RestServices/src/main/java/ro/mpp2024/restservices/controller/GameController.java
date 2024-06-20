package ro.mpp2024.restservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.configuration.ConfigurationRepository;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@ComponentScan("ro.mpp2024.persistence")
@RestController
@RequestMapping("trapgame/games")
@CrossOrigin
public class GameController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;

    @GetMapping
    public ResponseEntity<?> getById(@RequestParam String alias,
                                     @RequestParam String filter) {
        if (!Objects.equals(filter, "won") &&
                !Objects.equals(filter, "lost") &&
                !Objects.equals(filter, "all")) {
            return new ResponseEntity<>("Incorrect filter", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findByAlias(alias);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("No user with specified alias was found",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Game> games = gameRepository.findGamesOfUser(optionalUser.get());

        switch(filter) {
            case "won":
                games = games.stream().filter(Game::isWon).toList();
                break;
            case "lost":
                games = games.stream().filter(g -> !g.isWon()).toList();
                break;
            case "all":
            default:;
        }

        games.forEach(g -> g.getConfigurations().forEach(c -> c.setSymbol('H')));

        return ResponseEntity.ok(games);
    }

    @PutMapping("/{id}/configuration")
    public ResponseEntity<?> updateConfiguration(@PathVariable Integer id,
                                                 @RequestBody Configuration configuration) {
        if (configuration.getId().getGameId() != null) {
            return new ResponseEntity<>("Request body must not contain game id", HttpStatus.BAD_REQUEST);
        }

        if (configuration.getId().getRow() == null || configuration.getId().getColumn() == null ||
                configuration.getId().getRow() < 1 || configuration.getId().getRow() > 5 ||
                configuration.getId().getColumn() < 1 || configuration.getId().getColumn() > 5) {
            return new ResponseEntity<>("Invalid configuration", HttpStatus.BAD_REQUEST);
        }
        if (configuration.getSymbol() == null) {
            return new ResponseEntity<>("Symbol must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (configuration.getSymbol() != 'T' && configuration.getSymbol() != 'E') {
            return new ResponseEntity<>("Invalid symbol, must be 'T' or 'E'", HttpStatus.BAD_REQUEST);
        }

        Optional<Game> optionalGame = gameRepository.getByID(id);
        if (optionalGame.isEmpty()) {
            return new ResponseEntity<>("Specified game was not found", HttpStatus.NOT_FOUND);
        }
        Game game = optionalGame.get();

        configuration.getId().setGameId(game.getId());
        configuration.setGame(game);

        try {
            if (configuration.getSymbol() == 'T') {
                if (!game.getConfigurations().contains(configuration)) {
//                game.getConfigurations().add(configuration);
                    configurationRepository.add(configuration);
                }
            }
            else {
//            game.getConfigurations().remove(configuration);
                configurationRepository.delete(configuration.getId());
            }

//            gameRepository.update(game);

            return ResponseEntity.ok("Configuration saved with success");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
