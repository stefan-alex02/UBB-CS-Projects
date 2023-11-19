package ir.map.g221.seminar7_v3.factory;

import ir.map.g221.seminar7_v3.business.UserService;
import ir.map.g221.seminar7_v3.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final UserInterface ui;
    private final SampleGenerator sampleGenerator;

    public UserService getUserService() {
        return userService;
    }

    public SampleGenerator getSampleGenerator() {
        return sampleGenerator;
    }

    public BuildContainer(UserService userService, UserInterface ui, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.ui = ui;
        this.sampleGenerator = sampleGenerator;
    }

    public UserInterface getUi() {
        return ui;
    }
}
