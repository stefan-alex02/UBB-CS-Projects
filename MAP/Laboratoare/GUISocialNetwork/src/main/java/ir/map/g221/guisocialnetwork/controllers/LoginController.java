package ir.map.g221.guisocialnetwork.controllers;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.SoundFile;
import ir.map.g221.guisocialnetwork.domain.PasswordEncoder;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController extends AbstractUserController {
    @FXML TextField textFieldUsername;
    @FXML PasswordField textFieldPassword;
    @FXML GridPane gridPane;
    private Stage dialogStage;

    @FXML
    public void initialize() {

    }

    public void setContent(BuildContainer buildContainer, Stage dialogStage) {
        this.buildContainer = buildContainer;
        this.dialogStage = dialogStage;
    }

    public void handleRegisterButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OldMain.class.getResource("views/edituser-view.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Register User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(buildContainer.getUserService(), dialogStage, Optional.empty());

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLoginButton(ActionEvent actionEvent) {
        if (textFieldUsername.getText().isEmpty()) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "Username must not be empty.", SoundFile.ERROR_1);
            return;
        }
        if (textFieldPassword.getText().isEmpty()) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "Password must not be empty.", SoundFile.ERROR_1);
            return;
        }

        Optional<User> optionalUser = buildContainer.getUserService()
                .getAllUsers()
                .stream()
                .filter(user -> user.getUsername().equals(textFieldUsername.getText()))
                .findFirst();

        if (optionalUser.isEmpty()) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "There is no user with given username.", SoundFile.ERROR_1);
            return;
        }

        String encodedInputPassword = PasswordEncoder.getInstance().encodeToSHAHexString(textFieldPassword.getText());

        if (!optionalUser.get().getPassword().equals(encodedInputPassword)) {
            MessageAlerter.showErrorMessage(null, "Login error",
                    "Wrong password! Please try again.", SoundFile.ERROR_1);
            return;
        }

        openUserPerspective(optionalUser.get());

        dialogStage.close();
    }
}
