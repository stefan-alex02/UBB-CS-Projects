package ir.map.g221.guisocialnetwork.controllers.othercontrollers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationAlerter {
    public static void playSound(SoundFile soundFile) {
        Media pick = new Media(soundFile.getFilePath());
        MediaPlayer player = new MediaPlayer(pick);
        player.setVolume(0.2);

        player.play();
    }

    public static void displayNotification(String title, String message, NotificationImage image) {
        Notifications.create()
                .darkStyle()
                .title(title)
                .graphic(image.getImageView())
                .text(message)
                .hideAfter(Duration.seconds(10))
                .show();
    }
}
