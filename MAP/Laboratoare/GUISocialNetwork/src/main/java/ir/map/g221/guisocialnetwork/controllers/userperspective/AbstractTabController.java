package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.scene.control.Tab;

public abstract class AbstractTabController implements Observer {
    private Tab ownerTab;
    private UserPerspectiveController ownerController;

    public final void setOwnerTab(Tab ownerTab) {
        this.ownerTab = ownerTab;
    }

    public UserPerspectiveController getOwnerController() {
        return ownerController;
    }

    public void setOwnerController(UserPerspectiveController ownerController) {
        this.ownerController = ownerController;
    }

    protected void notifyOwnerController(Event event) {
        ownerController.update(event);
    }

    protected final boolean isSelected() {
        return ownerTab.isSelected();
    }
}
