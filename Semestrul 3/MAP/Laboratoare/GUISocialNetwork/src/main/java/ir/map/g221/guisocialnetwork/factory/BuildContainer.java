package ir.map.g221.guisocialnetwork.factory;

import ir.map.g221.guisocialnetwork.business.*;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.ui.UserInterface;

import java.sql.SQLException;

public class BuildContainer {
    private final DatabaseConnection databaseConnection;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;
    private final CommunityHandler communityHandler;
    private final UserInterface ui;

    private final SampleGenerator sampleGenerator;

    public BuildContainer(DatabaseConnection databaseConnection,
                          UserService userService,
                          FriendshipService friendshipService,
                          MessageService messageService,
                          FriendRequestService friendRequestService,
                          CommunityHandler communityHandler,
                          UserInterface ui,
                          SampleGenerator sampleGenerator) {
        this.databaseConnection = databaseConnection;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.communityHandler = communityHandler;
        this.ui = ui;
        this.sampleGenerator = sampleGenerator;
    }

    public UserInterface getUi() {
        return ui;
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public FriendRequestService getFriendRequestService() {
        return friendRequestService;
    }

    public SampleGenerator getSampleGenerator() {
        return sampleGenerator;
    }

    public CommunityHandler getCommunityHandler() {
        return communityHandler;
    }

    public void closeSQLConnection() throws SQLException {
        databaseConnection.closeConnection();
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
}
