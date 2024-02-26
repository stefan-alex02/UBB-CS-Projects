package org.example.zboruri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.zboruri.controllers.ClientController;
import org.example.zboruri.controllers.MessageAlerter;
import org.example.zboruri.domain.Client;
import org.example.zboruri.exceptions.NonexistentUsernameException;
import org.example.zboruri.factory.BuildContainer;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField textFieldUsername;

    protected BuildContainer container;

    public void setContent(BuildContainer container) {
        this.container = container;
    }

    @FXML
    protected void onLoginButtonClick() {
        try {
            Client client = container.getClientService().getClientByUsername(textFieldUsername.getText());

            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage();

            loader.setLocation(LoginApplication.class.getResource("views/client-view.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            stage.setTitle("Client " + client.getName());
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            ClientController clientController = loader.getController();
            clientController.setContent(container, client, stage);

            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NonexistentUsernameException e) {
            MessageAlerter.showErrorMessage(null, "Login error", e.getMessage());
        }
    }
}