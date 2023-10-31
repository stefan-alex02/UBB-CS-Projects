package ir.map.g221.persistence.inmemoryrepos;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.validation.Validator;

public class UserInMemoryRepo extends InMemoryRepository<Long, User> {
    public UserInMemoryRepo(Validator<User> validator) {
        super(validator);
    }
}
