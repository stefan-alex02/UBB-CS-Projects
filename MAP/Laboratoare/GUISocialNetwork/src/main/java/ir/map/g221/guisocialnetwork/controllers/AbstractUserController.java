package ir.map.g221.guisocialnetwork.controllers;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.SoundFile;
import ir.map.g221.guisocialnetwork.controllers.userperspective.UserPerspectiveController;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractUserController {
    protected BuildContainer buildContainer = null;

    protected void openUserPerspective(User user) {
        try {
            FXMLLoader userPerspectiveLoader = new FXMLLoader();
            userPerspectiveLoader.setLocation(OldMain.class.getResource("views/user-perspective.fxml"));

            AnchorPane root = userPerspectiveLoader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("User account : " + user.getFirstName() + " " + user.getLastName());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(OldMain.class.getResource("css/style.css"))
                    .toExternalForm());
            stage.setScene(scene);

            UserPerspectiveController userPerspectiveController = userPerspectiveLoader.getController();
            userPerspectiveController.setContent(buildContainer, user, stage);

            MessageAlerter.playSound(SoundFile.STARTUP);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
