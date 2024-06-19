package ro.mpp2024.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ro.mpp2024.client.guiutils.MessageAlerter;
import ro.mpp2024.domain.User;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameServices;

public class LoginController {
    private GameServices server;
    private GameController gameController;

    @FXML TextField textFieldUsername;
    @FXML GridPane gridPane;

    private Parent parent;

    @FXML
    public void initialize() {
    }

    public void setServer(GameServices server) {
        this.server = server;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void handleLoginButton(ActionEvent actionEvent) {
        if (textFieldUsername.getText().isEmpty()) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "Username must not be empty.");
            return;
        }

        String username = textFieldUsername.getText();
        User currentUser = new User(0, username);

        try {
            User user = server.login(currentUser, gameController);

            login(user, actionEvent);
        }
        catch (GameException e) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    e.getMessage());
        }
        catch (Exception e) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "An internal error occurred while trying to log in.");
        }
    }

    protected void login(User user, ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Player : " + user.getAlias());
        stage.setScene(new Scene(parent));

        stage.setOnCloseRequest(event -> {
            gameController.logout();
            System.exit(0);
        });

        stage.show();
        gameController.setUser(user);
        gameController.initializeGame();
        gameController.initializeScoresModel();
        gameController.startGame();

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
