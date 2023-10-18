package ir.map.g221.persistence.in_memory_concrete_repos;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.persistence.InMemoryRepository;

public class UserInMemoryRepo extends InMemoryRepository<Long, User> {
    public UserInMemoryRepo(Validator<User> validator) {
        super(validator);
    }
}
