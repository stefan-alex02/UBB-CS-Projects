package ir.map.g221.seminar7_v3.business;

import ir.map.g221.seminar7_v3.domain.entities.User;
import ir.map.g221.seminar7_v3.domain.generaltypes.ObjectTransformer;
import ir.map.g221.seminar7_v3.exceptions.ValidationException;
import ir.map.g221.seminar7_v3.persistence.Repository;

import java.util.Collection;

public class UserService {
    private final Repository<Long, User> userRepository;

    public UserService(Repository<Long, User> userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        userRepository.save(new User(firstName, lastName));
    }

    public Collection<User> getAll() {
        return ObjectTransformer.iterableToCollection(userRepository.findAll());
    }

}
