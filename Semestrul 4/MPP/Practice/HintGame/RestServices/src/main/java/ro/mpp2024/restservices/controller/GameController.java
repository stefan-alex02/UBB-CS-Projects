package ro.mpp2024.restservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.configuration.ConfigurationRepository;
import ro.mpp2024.persistence.exception.ConflictException;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@ComponentScan("ro.mpp2024.persistence")
@RestController
@RequestMapping("hintgame/games")
@CrossOrigin
public class GameController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public ResponseEntity<?> getGames(@RequestParam String alias) {
        Optional<User> optionalUser = userRepository.findByAlias(alias);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("No user with specified alias was found",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Game> games = gameRepository.findGamesOfUser(optionalUser.get());

        return ResponseEntity.ok(games);
    }
}
