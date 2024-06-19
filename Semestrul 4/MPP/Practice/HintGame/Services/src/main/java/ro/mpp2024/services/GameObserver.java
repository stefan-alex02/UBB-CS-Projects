package ro.mpp2024.services;

import ro.mpp2024.domain.Game;

import java.util.Collection;

public interface GameObserver {
    void newFinishedGame(Collection<Game> games) throws GameException;
}
