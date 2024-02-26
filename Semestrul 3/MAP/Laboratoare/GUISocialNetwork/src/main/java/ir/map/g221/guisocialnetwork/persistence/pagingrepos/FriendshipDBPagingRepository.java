package ir.map.g221.guisocialnetwork.persistence.pagingrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.FriendshipDBRepository;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;

public class FriendshipDBPagingRepository extends FriendshipDBRepository implements PagingRepository<UnorderedPair<Long, Long>, Friendship> {
    public FriendshipDBPagingRepository(Validator<Friendship> validator) {
        super(validator);
    }
}
