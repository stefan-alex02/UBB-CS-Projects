package ir.map.g221.guisocialnetwork.persistence.dbrepos;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.persistence.Repository;

import java.util.Optional;

public class FriendRequestDBRepository implements Repository<Long, FriendRequest> {
    @Override
    public Optional<FriendRequest> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        return null;
    }

    @Override
    public Integer getSize() {
        return null;
    }

    @Override
    public Optional<FriendRequest> save(FriendRequest entity) {
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<FriendRequest> update(FriendRequest entity) {
        return Optional.empty();
    }
}
