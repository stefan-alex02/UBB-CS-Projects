package ro.mpp2024.restservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.configuration.ConfigurationRepository;
import ro.mpp2024.persistence.exception.ConflictException;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserRepository;

import java.util.Collection;
import java.util.Optional;

@ComponentScan("ro.mpp2024.persistence")
@RestController
@RequestMapping("hintgame/configurations")
@CrossOrigin
public class ConfigurationController {
    @Autowired
    private ConfigurationRepository configurationRepository;

    @PostMapping
    public ResponseEntity<?> addConfiguration(@RequestBody Configuration configuration) {
        if (configuration.getId() != null) {
            return new ResponseEntity<>("Request body must not contain id", HttpStatus.BAD_REQUEST);
        }

        if (configuration.getRow() == null || configuration.getColumn() == null ||
                configuration.getRow() < 1 || configuration.getRow() > 4 ||
                configuration.getColumn() < 1 || configuration.getColumn() > 4) {
            return new ResponseEntity<>("Invalid configuration", HttpStatus.BAD_REQUEST);
        }
        if (configuration.getHint() == null || configuration.getHint().isEmpty()) {
            return new ResponseEntity<>("Hint must not be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            configurationRepository.add(configuration);

            return ResponseEntity.ok("Configuration saved with success");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
