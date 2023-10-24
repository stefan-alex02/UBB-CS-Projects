package ir.map.g221.persistence.inmemoryrepos;

import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.domain.validation.Validator;

public class FriendshipInMemoryRepo extends InMemoryRepository<UnorderedPair<Long, Long>, Friendship> {
    public FriendshipInMemoryRepo(Validator<Friendship> validator) {
        super(validator);
    }
}
