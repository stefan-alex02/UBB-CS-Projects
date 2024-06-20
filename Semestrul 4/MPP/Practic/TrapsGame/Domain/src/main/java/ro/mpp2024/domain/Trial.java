package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "trials")
@Check(constraints = "row BETWEEN 1 AND 5 AND column BETWEEN 1 AND 5")
public class Trial implements Entity<Trial.TrialId> {
    @Embeddable
    public static class TrialId implements Serializable {
        private Integer gameId;
        private Integer row;
        private Integer column;

        public TrialId() {}

        public TrialId(Integer gameId,
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
            TrialId trialId = (TrialId) o;
            return Objects.equals(gameId, trialId.gameId) && Objects.equals(row, trialId.row) && Objects.equals(column, trialId.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(gameId, row, column);
        }

        @Override
        public String toString() {
            return "TrialId{" +
                    "gameId=" + gameId +
                    ", row=" + row +
                    ", column=" + column +
                    '}';
        }
    }

    private TrialId id;
    private Game game;

    public Trial(TrialId id, Game game) {
        this.id = id;
        this.game = game;
    }

    public Trial() {
    }

    @Override
    public void setId(TrialId trialId) {
        this.id = trialId;
    }

    @EmbeddedId
    @Override
    public TrialId getId() {
        return id;
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
        Trial trial = (Trial) o;
        return Objects.equals(id, trial.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Trial{" +
                "id=" + id  +
                '}';
    }
}

