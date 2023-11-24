package ir.map.g221.guisocialnetwork.persistence.inmemoryrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;

public class FriendshipInMemoryRepo extends InMemoryRepository<UnorderedPair<Long, Long>, Friendship> {
    public FriendshipInMemoryRepo(Validator<Friendship> validator) {
        super(validator);
    }
}
