package org.example.practic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.practic.controllers.ClientController;
import org.example.practic.controllers.MessageAlerter;
import org.example.practic.exceptions.NonexistentUsernameException;
import org.example.practic.factory.BuildContainer;

import java.io.IOException;

public class StartController {

    protected BuildContainer container;

    public void setContent(BuildContainer container) {
        this.container = container;
    }

    @FXML
    protected void onLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage();


            loader.setLocation(StartApplication.class.getResource("views/entity-view.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            stage.setTitle("Window");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            ClientController controller = loader.getController();
            controller.setContent(container, stage);

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException | NonexistentUsernameException e) {
            MessageAlerter.showErrorMessage(null, "Login error", e.getMessage());
        }
    }
}