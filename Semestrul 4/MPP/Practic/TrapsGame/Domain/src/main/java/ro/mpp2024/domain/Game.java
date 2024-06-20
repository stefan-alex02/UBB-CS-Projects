package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@jakarta.persistence.Entity
@Table(name = "games")
public class Game implements Entity<Integer> {
    private int id;
    private User user;
    private Collection<Trial> trials = new ArrayList<>();
    private Collection<Configuration> configurations = new ArrayList<>();
    private Integer score;
    private Integer duration;
    private boolean won;

    public Game(int id,
                User user,
                Collection<Trial> trials,
                Integer score,
                Integer duration,
                Collection<Configuration> configurations, boolean won) {
        this.id = id;
        this.user = user;
        this.trials = new ArrayList<>(trials);
        this.score = score;
        this.duration = duration;
        this.configurations = configurations;
        this.won = won;
    }

    public Game() {}

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    @JsonManagedReference
    public Collection<Trial> getTrials() {
        return trials;
    }

    public void setTrials(Collection<Trial> trials) {
        this.trials = new ArrayList<>(trials);
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "score")
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Column
    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    public Collection<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Collection<Configuration> configurations) {
        this.configurations = configurations;
    }
}

