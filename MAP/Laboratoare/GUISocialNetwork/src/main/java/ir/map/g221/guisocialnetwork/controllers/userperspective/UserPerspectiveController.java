package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserPerspectiveController implements Observer {
    private Stage stage;
    private User user;
    private BuildContainer buildContainer = null;

    @FXML TabPane tabPane;

    @FXML Tab chatPage;
    @FXML UserChatController userChatController;

    @FXML Tab friendRequestPage;
    @FXML FriendRequestController friendRequestController;

    @FXML Tab searchUsersPage;
    @FXML SearchUsersController searchUsersController;

    @FXML Tab friendListPage;
    @FXML FriendListController friendListController;

    public void setContent(BuildContainer buildContainer, User user, Stage stage) {
        this.buildContainer = buildContainer;
        this.user = user;
        this.stage = stage;
        buildContainer.getUserService().addObserver(this);
        stage.setOnCloseRequest(e -> dispose());

        searchUsersController.setContent(buildContainer, user);
        friendRequestController.setContent(buildContainer, user);
        friendListController.setContent(buildContainer, user);
    }

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        System.out.println("Tab Selection changed");
                        if (newTab.equals(searchUsersPage)) {
                            searchUsersController.update(new OpenedEvent());
                        }
                        else if (newTab.equals(friendRequestPage)) {
                            friendRequestController.update(new OpenedEvent());
                        }
                        else if (newTab.equals(friendListPage)) {
                            friendListController.update(new OpenedEvent());
                        }
                    }
                }
        );
    }

    @Override
    public void update(Event event) {
        if (event.getEventType() == EventType.USER) {
            UserChangeEvent userChangeEvent = (UserChangeEvent) event;
            if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                userChangeEvent.getOldData().equals(user)) {
                dispose();
            }
        }
    }

    private void dispose() {
        buildContainer.getUserService().removeObserver(this);

        searchUsersController.dispose();
        friendRequestController.dispose();
        friendListController.dispose();
        stage.close();
    }
}
