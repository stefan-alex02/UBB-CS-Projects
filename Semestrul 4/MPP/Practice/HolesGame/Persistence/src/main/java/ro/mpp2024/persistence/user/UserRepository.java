package ro.mpp2024.persistence.user;

import ro.mpp2024.domain.User;
import ro.mpp2024.persistence.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Integer> {
    Optional<User> findByAlias(String alias);
}
