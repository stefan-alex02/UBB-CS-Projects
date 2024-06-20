package ro.mpp2024.client.guiutils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlerter {
    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    public static void showErrorMessage(Stage owner, String headerText, String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setHeaderText(headerText);
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }
}
