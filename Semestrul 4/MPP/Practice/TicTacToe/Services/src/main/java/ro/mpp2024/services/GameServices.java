package ro.mpp2024.services;

import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.User;

import java.util.Collection;

public interface GameServices {
     User login(User user, GameObserver client) throws GameException;
     void logout(User user, GameObserver client) throws GameException;
     Collection<Game> getGames() throws GameException;
     void saveGame(Game game) throws GameException;
}
