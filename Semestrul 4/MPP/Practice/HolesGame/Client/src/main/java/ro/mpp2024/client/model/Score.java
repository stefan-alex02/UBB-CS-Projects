package ro.mpp2024.client.model;

import ro.mpp2024.domain.Game;

public class Score {
    private String alias;
    private int score;
    private int duration;

    public Score(String alias, int score, int duration) {
        this.alias = alias;
        this.score = score;
        this.duration = duration;
    }

    public static Score fromGame(Game game) {
        return new Score(game.getUser().getAlias(), game.getScore(), game.getDuration());
    }

    public String getAlias() {
        return alias;
    }

    public int getScore() {
        return score;
    }

    public int getDuration() {
        return duration;
    }
}
