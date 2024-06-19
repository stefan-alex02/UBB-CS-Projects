package ro.mpp2024.client.model;

import ro.mpp2024.domain.Game;

import java.time.LocalDateTime;

public class Score {
    private final String alias;
    private final LocalDateTime startDateTime;
    private final int trialsCount;
    private final String hint;

    public Score(String alias, LocalDateTime startDateTime, int trialsCount, String hint) {
        this.alias = alias;
        this.startDateTime = startDateTime;
        this.trialsCount = trialsCount;
        this.hint = hint;
    }

    public static Score fromGame(Game game) {
        return new Score(game.getUser().getAlias(),
                game.getStartDateTime(),
                game.getTrialsCount(),
                game.getHint());
    }

    public String getAlias() {
        return alias;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public int getTrialsCount() {
        return trialsCount;
    }

    public String getHint() {
        return hint;
    }
}
