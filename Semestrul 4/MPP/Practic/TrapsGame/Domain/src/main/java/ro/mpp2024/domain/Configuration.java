package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "configurations")
public class Configuration implements Entity<Configuration.ConfigurationId> {
    @Embeddable
    public static class ConfigurationId implements Serializable {
        private Integer gameId;
        private Integer row;
        private Integer column;

        public ConfigurationId() {}

        public ConfigurationId(Integer gameId,
                               @Min(1) @Max(5) Integer row,
                               @Min(1) @Max(5) Integer column) {
            this.gameId = gameId;
            this.row = row;
            this.column = column;
        }

        public Integer getGameId() {
            return gameId;
        }

        public void setGameId(Integer gameId) {
            this.gameId = gameId;
        }

        @Min(1)
        @Max(5)
        @NotNull
        public Integer getRow() {
            return row;
        }

        public void setRow(Integer row) {
            this.row = row;
        }

        @Min(1)
        @Max(5)
        @NotNull
        public Integer getColumn() {
            return column;
        }

        public void setColumn(Integer column) {
            this.column = column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConfigurationId that = (ConfigurationId) o;
            return Objects.equals(gameId, that.gameId) && Objects.equals(row, that.row) && Objects.equals(column, that.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(gameId, row, column);
        }
    }

    private ConfigurationId id;
    private Character symbol;
    private Game game;

    public Configuration() {}

    public Configuration(ConfigurationId id, Game game) {
        this.id = id;
        this.symbol = 'T';
        this.game = game;
    }

    @EmbeddedId
    @Override
    public ConfigurationId getId() {
        return id;
    }

    public void setId(ConfigurationId id) {
        this.id = id;
    }

    @Transient
    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
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

