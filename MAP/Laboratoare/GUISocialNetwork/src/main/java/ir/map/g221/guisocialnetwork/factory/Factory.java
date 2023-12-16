package ir.map.g221.guisocialnetwork.factory;

import ir.map.g221.guisocialnetwork.business.*;
import ir.map.g221.guisocialnetwork.domain.PasswordEncoder;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.validation.*;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.FriendRequestDBRepository;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.MessageDBRepository;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.UserDBRepository;
import ir.map.g221.guisocialnetwork.persistence.dbrepos.FriendshipDBRepository;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;
import ir.map.g221.guisocialnetwork.persistence.pagingrepos.FriendRequestDBPagingRepository;
import ir.map.g221.guisocialnetwork.persistence.pagingrepos.FriendshipDBPagingRepository;
import ir.map.g221.guisocialnetwork.persistence.pagingrepos.UserDBPagingRepository;
import ir.map.g221.guisocialnetwork.ui.UserConsole;
import ir.map.g221.guisocialnetwork.ui.UserInterface;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;

import java.sql.SQLException;

public class Factory {
    private static Factory instance = null;

    private Factory() {
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public BuildContainer build() {
        try {
            String url="jdbc:postgresql://localhost:5432/socialnetwork";
            String username = "postgres";
            String password = "postgres";

            DatabaseConnection.setInstance(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PagingRepository<Long, User> userRepo =
                new UserDBPagingRepository(
                        UserValidator.getInstance(), PasswordEncoder.getInstance());
        PagingRepository<UnorderedPair<Long, Long>, Friendship> friendshipRepo =
                new FriendshipDBPagingRepository(FriendshipValidator.getInstance());
        PagingRepository<Long, FriendRequest> friendRequestRepo =
                new FriendRequestDBPagingRepository(DatabaseConnection.getSingleInstance(),
                        FriendRequestValidator.getInstance());
        PagingRepository<Long, Message> messageRepo =
                new MessageDBRepository(DatabaseConnection.getSingleInstance(), MessageValidator.getInstance(),
                        ReplyMessageValidator.getInstance());

        CommunityHandler communityHandler = new CommunityHandler(userRepo, friendshipRepo);

        UserService userService =
                new UserService(userRepo, friendshipRepo);
        FriendshipService friendshipService =
                new FriendshipService(userRepo, friendshipRepo);
        MessageService messageService =
                new MessageService(messageRepo, userRepo);
        FriendRequestService friendRequestService =
                new FriendRequestService(friendRequestRepo, userRepo, friendshipRepo);

        userService.addObserver(communityHandler);
        friendshipService.addObserver(communityHandler);
        friendRequestService.addObserver(communityHandler);

        SampleGenerator sampleGenerator = new SampleGenerator(userService, friendshipService);
        UserInterface ui = new UserConsole(userService, friendshipService, messageService, friendRequestService,
                communityHandler, sampleGenerator);

        return new BuildContainer(
                DatabaseConnection.getSingleInstance(), userService, friendshipService,
                messageService, friendRequestService,
                communityHandler, ui, sampleGenerator);
    }
}
