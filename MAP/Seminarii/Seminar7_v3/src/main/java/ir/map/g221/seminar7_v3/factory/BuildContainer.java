package ir.map.g221.seminar7_v3.factory;

import ir.map.g221.seminar7_v3.business.FriendshipService;
import ir.map.g221.seminar7_v3.business.UserService;
import ir.map.g221.seminar7_v3.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final UserInterface ui;
    private final SampleGenerator sampleGenerator;

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public SampleGenerator getSampleGenerator() {
        return sampleGenerator;
    }

    public BuildContainer(UserService userService, FriendshipService friendshipService, UserInterface ui, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.ui = ui;
        this.sampleGenerator = sampleGenerator;
    }

    public UserInterface getUi() {
        return ui;
    }
}
