package ir.map.g221.guisocialnetwork.persistence.pagingrepos;

import ir.map.g221.guisocialnetwork.domain.PasswordEncoder;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.UserDBRepository;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;

public class UserDBPagingRepository extends UserDBRepository implements PagingRepository<Long, User> {
    public UserDBPagingRepository(Validator<User> validator, PasswordEncoder passwordEncoder) {
        super(validator, passwordEncoder);
    }
}
