package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "configurations")
public class Configuration implements Entity<Configuration.ConfigurationId> {
    @Embeddable
    public static class ConfigurationId implements Serializable {
        private Integer gameId;
        private Integer position;

        public ConfigurationId() {}

        public ConfigurationId(Integer gameId, @Min(1) @Max(10) Integer position) {
            this.gameId = gameId;
            this.position = position;
        }

        public Integer getGameId() {
            return gameId;
        }

        public void setGameId(Integer gameId) {
            this.gameId = gameId;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigurationId that = (ConfigurationId) o;
            return gameId.equals(that.gameId) && position.equals(that.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(gameId, position);
        }
    }

    private ConfigurationId id;
    private Game game;
    private Word word;

    public Configuration() {}

    public Configuration(ConfigurationId id, Game game, Word word) {
        this.id = id;
        this.game = game;
        this.word = word;
    }

    @EmbeddedId
    @Override
    public ConfigurationId getId() {
        return id;
    }

    public void setId(ConfigurationId id) {
        this.id = id;
    }

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @ManyToOne
    @JoinColumn(name = "word", nullable = false)
    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

