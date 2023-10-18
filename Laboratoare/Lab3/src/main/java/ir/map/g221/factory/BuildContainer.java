package ir.map.g221.factory;

import ir.map.g221.business.UserService;
import ir.map.g221.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final UserInterface ui;
    private final SampleGenerator sampleGenerator;

    public BuildContainer(UserService userService, UserInterface ui, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.ui = ui;
        this.sampleGenerator = sampleGenerator;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserInterface getUi() {
        return ui;
    }
}
