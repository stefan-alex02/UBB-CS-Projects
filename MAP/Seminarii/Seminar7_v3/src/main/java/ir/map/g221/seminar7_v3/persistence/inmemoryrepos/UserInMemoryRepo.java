package ir.map.g221.seminar7_v3.persistence.inmemoryrepos;

import ir.map.g221.seminar7_v3.domain.entities.User;
import ir.map.g221.seminar7_v3.domain.validation.Validator;
import ir.map.g221.seminar7_v3.exceptions.ValidationException;

import java.util.Optional;

public class UserInMemoryRepo extends InMemoryRepository<Long, User> {
    public UserInMemoryRepo(Validator<User> validator) {
        super(validator);
    }

    @Override
    public Optional<User> save(User entity) throws IllegalArgumentException, ValidationException {
        checkForNullEntity(entity);
        validator.validate(entity);

        if (entity.getId() == 0) {
            Long id = entity.getId() + 1;
            entity.setId(id);
            while(entities.containsKey(entity.getId())) {
                entity.setId(++id);
            }
        }

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }
}
