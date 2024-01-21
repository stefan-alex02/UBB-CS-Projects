package org.example.modelpractic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.modelpractic.controllers.DriverController;
import org.example.modelpractic.controllers.MessageAlerter;
import org.example.modelpractic.controllers.PersonController;
import org.example.modelpractic.domain.Driver;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.domain.PersonType;
import org.example.modelpractic.exceptions.NonexistentUsernameException;
import org.example.modelpractic.factory.BuildContainer;

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
            Person person = container.getPersonService().getPersonByUsername(textFieldUsername.getText());

            FXMLLoader loader = new FXMLLoader();
            Stage stage = new Stage();

            if (person.getPersonType() == PersonType.CLIENT) {
                loader.setLocation(LoginApplication.class.getResource("views/person-view.fxml"));

                AnchorPane root = loader.load();

                // Create the dialog Stage.
                stage.setTitle("Client " + person.getName());
                stage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                stage.setScene(scene);

                PersonController personController = loader.getController();
                personController.setContent(container, person, stage);

                stage.show();
            }
            else {
                loader.setLocation(LoginApplication.class.getResource("views/driver-view.fxml"));

                AnchorPane root = loader.load();

                // Create the dialog Stage.
                stage.setTitle("Taximetrist " + person.getName());
                stage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                stage.setScene(scene);

                DriverController driverController = loader.getController();
                driverController.setContent(container, (Driver) person, stage);

                stage.show();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NonexistentUsernameException e) {
            MessageAlerter.showErrorMessage(null, "Login error", e.getMessage());
        }
    }
}