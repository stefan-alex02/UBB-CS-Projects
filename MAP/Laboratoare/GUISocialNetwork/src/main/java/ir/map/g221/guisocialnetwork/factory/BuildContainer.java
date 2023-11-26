package ir.map.g221.guisocialnetwork.factory;

import ir.map.g221.guisocialnetwork.business.CommunityHandler;
import ir.map.g221.guisocialnetwork.business.FriendshipService;
import ir.map.g221.guisocialnetwork.business.UserService;
import ir.map.g221.guisocialnetwork.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final CommunityHandler communityHandler;
    private final UserInterface ui;

    private final SampleGenerator sampleGenerator;

    public BuildContainer(UserService userService,
                          FriendshipService friendshipService,
                          CommunityHandler communityHandler,
                          UserInterface ui,
                          SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.friendshipService = friendshipService;
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

    public SampleGenerator getSampleGenerator() {
        return sampleGenerator;
    }

    public CommunityHandler getCommunityService() {
        return communityHandler;
    }
}
