package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Set;

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

    private Set<AbstractTabController> tabControllers;

    public void setContent(BuildContainer buildContainer, User user, Stage stage) {
        this.buildContainer = buildContainer;
        this.user = user;
        this.stage = stage;
        this.buildContainer.getUserService().addObserver(this);
        this.stage.setOnCloseRequest(e -> dispose());
        this.tabControllers = Set.of(
                userChatController, friendRequestController,
                searchUsersController, friendListController);

        tabControllers.forEach(controller -> controller.setOwnerController(this));

        userChatController.setOwnerTab(chatPage);
        friendRequestController.setOwnerTab(friendRequestPage);

        userChatController.setContent(buildContainer, user);
        searchUsersController.setContent(buildContainer, user);
        friendRequestController.setContent(buildContainer, user);
        friendListController.setContent(buildContainer, user);
    }

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldTab, newTab) -> {
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
                    else if (newTab.equals(chatPage)) {
                        userChatController.update(new OpenedEvent());
                    }
                }
        );
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                        userChangeEvent.getOldData().equals(user)) {
                    dispose();
                }
                break;
            case CHAT_USER:
                tabPane.getSelectionModel().select(chatPage);
                userChatController.update(event);
                break;
            default:;
        }
    }

    private void dispose() {
        buildContainer.getUserService().removeObserver(this);

        userChatController.dispose();
        searchUsersController.dispose();
        friendRequestController.dispose();
        friendListController.dispose();
        stage.close();
    }
}
