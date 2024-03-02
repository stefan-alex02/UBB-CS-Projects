package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.LoginController;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.SoundFile;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
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

    public void handleLogout(ActionEvent actionEvent) {
        try {
            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(OldMain.class.getResource("views/login-view.fxml"));
            AnchorPane loginLayout = loginLoader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Login");
            Scene scene = new Scene(loginLayout);
            scene.getStylesheets().add(Objects.requireNonNull(OldMain.class.getResource("css/style.css"))
                    .toExternalForm());
            stage.setScene(scene);

            LoginController loginController = loginLoader.getController();
            loginController.setContent(buildContainer, stage);

            MessageAlerter.playSound(SoundFile.LOGOFF);

            stage.show();

            dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
