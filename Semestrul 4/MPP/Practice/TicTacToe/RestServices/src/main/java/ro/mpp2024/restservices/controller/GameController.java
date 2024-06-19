package ro.mpp2024.restservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.exception.ConflictException;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@ComponentScan("ro.mpp2024.persistence")
@RestController
@RequestMapping("tictactoe/games")
@CrossOrigin
public class GameController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public ResponseEntity<?> getGames(@RequestParam String alias,
                                      @RequestParam Character filter,
                                      @RequestParam String order){
        if (filter != 'w' && filter != 'l' && filter != 'd') {
            return new ResponseEntity<>("Incorrect filter", HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(order, "asc") &&
                !Objects.equals(order, "desc") &&
                !Objects.equals(order, "none")) {
            return new ResponseEntity<>("Incorrect order", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findByAlias(alias);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("No user with specified alias was found",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Game> games = gameRepository.findGamesOfUser(optionalUser.get());

        switch(filter) {
            case 'w':
                games = games.stream().filter(g -> g.getScore() == 10).toList();
                break;
            case 'l':
                games = games.stream().filter(g -> g.getScore() == -10).toList();
                break;
            case 'd':
                games = games.stream().filter(g -> g.getScore() == 5).toList();
                break;
            default:;
        }

        if (order.equals("asc")) {
            games = games.stream().sorted(Comparator.comparingInt(Game::getScore)).toList();
        }
        else if (order.equals("desc")) {
            games = games.stream().sorted((g1, g2) -> g2.getScore() - g1.getScore()).toList();
        }

        return ResponseEntity.ok(games);
    }

    @PostMapping("/{id}/trial")
    public ResponseEntity<?> markPosition(@PathVariable Integer id,
                                          @RequestBody Trial trial) {
        if (trial.getId() != null &&
                trial.getId().getGameId() != null &&
                !trial.getId().getGameId().equals(id)) {
            return new ResponseEntity<>("Ids must be equal", HttpStatus.BAD_REQUEST);
        }

        if (trial.getId().getRow() == null || trial.getId().getColumn() == null ||
            trial.getId().getRow() < 1 || trial.getId().getRow() > 3 ||
                trial.getId().getColumn() < 1 || trial.getId().getColumn() > 3) {
            return new ResponseEntity<>("Invalid trial configuration", HttpStatus.BAD_REQUEST);
        }

        if (trial.getSymbol() != null) {
            return new ResponseEntity<>("Cannot specify symbol for player", HttpStatus.BAD_REQUEST);
        }
        trial.setSymbol('X');

        Optional<Game> optionalGame = gameRepository.getByID(id);
        if (optionalGame.isEmpty()) {
            return new ResponseEntity<>("Specified game was not found", HttpStatus.NOT_FOUND);
        }

        try {
            Game game = optionalGame.get();
            gameRepository.addTrial(game, trial);

            return ResponseEntity.ok("Position saved with success");
        } catch (ConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
