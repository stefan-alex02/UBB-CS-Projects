package ir.map.g221.guisocialnetwork.controllers.othercontrollers;

import ir.map.g221.guisocialnetwork.OldMain;
import javafx.scene.image.ImageView;

public enum NotificationImage {
    USERS_1("images/users.png");
    private final ImageView imageView;

    NotificationImage(String filePath) {
        this.imageView = new ImageView(String.valueOf(OldMain.class.getResource(filePath)));
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitHeight(100);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
