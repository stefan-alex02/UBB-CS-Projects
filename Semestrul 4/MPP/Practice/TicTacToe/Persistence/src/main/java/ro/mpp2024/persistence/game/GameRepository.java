package ro.mpp2024.persistence.game;

import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.Repository;
import ro.mpp2024.persistence.exception.ConflictException;

import java.util.Collection;
import java.util.Optional;

public interface GameRepository extends Repository<Game, Integer> {
    void addTrial(Game entity, Trial trial) throws ConflictException;
    Collection<Game> findGamesOfUser(User user);
}
