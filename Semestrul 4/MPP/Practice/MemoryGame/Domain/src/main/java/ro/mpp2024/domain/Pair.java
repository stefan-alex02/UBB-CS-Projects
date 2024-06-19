package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@jakarta.persistence.Entity
@Table(name = "pairs")
@Check(constraints = "position1 BETWEEN 1 AND 10 AND position2 BETWEEN 1 AND 10 AND position1 <> position2")
public class Pair implements Entity<Integer> {
    private Integer id;
    private Integer position1;
    private Integer position2;
    private Game game;

    public Pair(Integer id, @Min(1) @Max(10) Integer position1, @Min(1) @Max(10) Integer position2, Game game) {
        this.id = id;
        this.position1 = position1;
        this.position2 = position2;
        this.game = game;
    }

    public Pair() {
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "game_id")
    @JsonBackReference
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Column(nullable = false)
    @Min(1)
    @Max(10)
    @NotNull
    public Integer getPosition1() {
        return position1;
    }

    public void setPosition1(Integer position1) {
        this.position1 = position1;
    }

    @Column(nullable = false)
    @Min(1)
    @Max(10)
    @NotNull
    public Integer getPosition2() {
        return position2;
    }

    public void setPosition2(Integer position2) {
        this.position2 = position2;
    }

    @PrePersist
    @PreUpdate
    private void validatePositions() {
        if (position1.equals(position2)) {
            throw new IllegalArgumentException("Positions must be different");
        }
    }
}

