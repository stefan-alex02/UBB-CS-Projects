package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@jakarta.persistence.Entity
@Table(name = "games")
public class Game implements Entity<Integer> {
    private int id;
    private User user;
    private Collection<Trial> trials = new ArrayList<>();
    private Configuration configuration;
    private LocalDateTime startDateTime;
    private String hint = "";

    public Game(int id,
                User user,
                Collection<Trial> trials, Configuration configuration,
                LocalDateTime startDateTime, String hint) {
        this.id = id;
        this.user = user;
        this.trials = new ArrayList<>(trials);
        this.configuration = configuration;
        this.startDateTime = startDateTime;
        this.hint = hint;
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

    @JsonProperty
    @Transient
    public Integer getTrialsCount() {
        return trials.size();
    }

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    @JsonIgnore
    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
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

    @Column(name = "start_date_time")
    @JsonIgnore
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime duration) {
        this.startDateTime = duration;
    }

    @Column
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", user=" + user +
                ", trials=" + trials +
                ", configuration=" + configuration +
                ", startDateTime=" + startDateTime +
                ", hint='" + hint + '\'' +
                '}';
    }
}

