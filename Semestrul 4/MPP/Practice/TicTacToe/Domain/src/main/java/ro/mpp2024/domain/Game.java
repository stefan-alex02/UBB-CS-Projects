package ro.mpp2024.domain;

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
    private Integer score;
    private Integer duration;

    public Game(int id,
                User user,
                Collection<Trial> trials,
                Integer score,
                Integer duration) {
        this.id = id;
        this.user = user;
        this.trials = new ArrayList<>(trials);
        this.score = score;
        this.duration = duration;
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

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    public Collection<Trial> getTrials() {
        return trials;
    }

    public void setTrials(Collection<Trial> trials) {
        this.trials = new ArrayList<>(trials);
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
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
}

