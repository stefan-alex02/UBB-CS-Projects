package ir.map.g221.guisocialnetwork.controllers.guiutils;

import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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

    public static void playSound(SoundFile soundFile) {
        Media pick = new Media(soundFile.getFilePath());
        MediaPlayer player = new MediaPlayer(pick);
        player.setVolume(soundFile.getIntensity());

        player.play();
    }

    public static void displayNotification(String title, String message, Image image) {
        Notifications.create()
                .darkStyle()
                .title(title)
                .graphic(image.createImageView())
                .text(message)
                .hideAfter(Duration.seconds(10))
                .show();
    }
}
