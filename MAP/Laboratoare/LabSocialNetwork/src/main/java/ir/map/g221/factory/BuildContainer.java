package ir.map.g221.factory;

import ir.map.g221.business.FriendshipService;
import ir.map.g221.business.UserService;
import ir.map.g221.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final UserInterface ui;
    private final SampleGenerator sampleGenerator;

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
