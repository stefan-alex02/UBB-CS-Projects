package ir.map.g221.guisocialnetwork.controllers;

import ir.map.g221.guisocialnetwork.business.UserService;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class EditUserController {
    @FXML
    public TextField textFieldId;
    @FXML
    public TextField textFieldFirstName;
    @FXML
    public TextField textFieldLastName;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    private UserService userService;
    private Stage dialogStage;
    private Optional<User> user;

    public void setService(UserService userService, Stage stage, Optional<User> user) {
        this.userService = userService;
        this.dialogStage = stage;
        this.user = user;
        textFieldId.setEditable(false);
        textFieldId.setDisable(true);
        user.ifPresent(this::setFields);
    }

    private void setFields(User user) {
        textFieldId.setText(user.getId().toString());
        textFieldUsername.setText(user.getUsername());
        textFieldFirstName.setText(user.getFirstName());
        textFieldLastName.setText(user.getLastName());
    }

    @FXML
    public void handleSaveButton() {
        final String username = textFieldUsername.getText();
        final String firstName = textFieldFirstName.getText();
        final String lastName = textFieldLastName.getText();
        final String password = user.isPresent() && textFieldPassword.getText().isEmpty() ?
                user.get().getPassword() : textFieldPassword.getText();

        this.user.ifPresentOrElse(
                u -> updateUser(new User(u.getId(), username, firstName, lastName, password)),
                () -> saveUser(new User(username, firstName, lastName, password)));
    }

    private void updateUser(User user)
    {
        try {
            this.userService.updateUser(user.getId(), user.getUsername(),
                    user.getFirstName(), user.getLastName(), user.getPassword());
            MessageAlerter.showMessage(null, Alert.AlertType.INFORMATION,"User update info",
                    "User was modified with success.");
            dialogStage.close();
        }
        catch (ValidationException e) {
            MessageAlerter.showErrorMessage(null, "Validation error", e.getMessage());
        }
        catch (RuntimeException e) {
            MessageAlerter.showErrorMessage(null, "There has been an error", e.getMessage());
        }
    }

    private void saveUser(User user)
    {
        try {
            this.userService.addUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getPassword());
            MessageAlerter.showMessage(null, Alert.AlertType.INFORMATION,"User save info",
                    "User saved with success.");
            dialogStage.close();
        }
        catch (ValidationException e) {
            MessageAlerter.showErrorMessage(null, "Validation error", e.getMessage());
        }
        catch (RuntimeException e) {
            MessageAlerter.showErrorMessage(null, "There has been an error", e.getMessage());
        }
    }

    @FXML
    public void handleCancelButton(){
        dialogStage.close();
    }
}
