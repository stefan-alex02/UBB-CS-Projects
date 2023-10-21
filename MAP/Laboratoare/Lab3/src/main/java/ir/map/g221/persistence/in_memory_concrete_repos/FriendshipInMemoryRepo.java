package ir.map.g221.persistence.in_memory_concrete_repos;

import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.general_types.UnorderedPair;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.persistence.InMemoryRepository;

public class FriendshipInMemoryRepo extends InMemoryRepository<UnorderedPair<Long, Long>, Friendship> {
    public FriendshipInMemoryRepo(Validator<Friendship> validator) {
        super(validator);
    }
}
