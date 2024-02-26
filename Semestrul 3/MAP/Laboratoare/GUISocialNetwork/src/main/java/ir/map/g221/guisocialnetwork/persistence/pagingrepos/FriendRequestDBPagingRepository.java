package ir.map.g221.guisocialnetwork.persistence.pagingrepos;

import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.FriendRequestDBRepository;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;

public class FriendRequestDBPagingRepository extends FriendRequestDBRepository implements PagingRepository<Long, FriendRequest> {
    public FriendRequestDBPagingRepository(DatabaseConnection databaseConnection, Validator<FriendRequest> validator) {
        super(databaseConnection, validator);
    }
}
