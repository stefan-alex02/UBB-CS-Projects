package ir.map.g221.domain.entities;

import ir.map.g221.domain.generaltypes.UnorderedPair;

import java.time.LocalDateTime;

public class Friendship extends Entity<UnorderedPair<Long, Long>> {
    private final LocalDateTime creationDate;

    public Friendship(UnorderedPair<Long, Long> longLongPair, LocalDateTime creationDate) {
        super(longLongPair);
        this.creationDate = creationDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
