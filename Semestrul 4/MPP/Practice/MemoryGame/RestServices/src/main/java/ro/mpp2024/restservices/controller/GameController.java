package ro.mpp2024.restservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.persistence.game.GameRepository;

import java.util.Collection;
import java.util.Optional;

@ComponentScan("ro.mpp2024.persistence")
@RestController
@RequestMapping("memory/games")
@CrossOrigin
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        Optional<Game> optionalGame = gameRepository.getByID(id);
        if (optionalGame.isPresent())
            return ResponseEntity.ok(optionalGame.get());
        else
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConfiguration(@PathVariable Integer id,
                                                 @RequestBody Collection<Configuration> configurations){
        Optional<Game> optionalGame = gameRepository.getByID(id);
        if (optionalGame.isEmpty()) {
            return new ResponseEntity<>("Specified game was not found", HttpStatus.NOT_FOUND);
        }
        if (configurations.size() != 10) {
            return new ResponseEntity<>("Invalid configuration size", HttpStatus.BAD_REQUEST);
        }

        try {
            Game game = optionalGame.get();
            game.setConfigurations(configurations);
            game.getConfigurations().forEach(c -> {
                c.setGame(game);
                c.getId().setGameId(game.getId());
            });
            Optional<Game> updatedGame = gameRepository.update(game);

            if (updatedGame.isPresent())
                return ResponseEntity.ok(updatedGame.get().getConfigurations());
            else
                return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
