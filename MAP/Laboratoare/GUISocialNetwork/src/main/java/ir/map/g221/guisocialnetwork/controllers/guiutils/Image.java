package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.OldMain;
import javafx.scene.image.ImageView;

public enum Image {
    USERS_1("images/users.png"),
    EDIT("images/edit.png", 18),
    DELETE("images/delete.png", 18),
    REPLY("images/reply.png", 18);


    private final String filePath;
    private final int size;

    Image(String filePath) {
        this(filePath, 100);
    }

    Image(String filePath, int size) {
        this.filePath = filePath;
        this.size = size;
    }

    public ImageView createImageView() {
        ImageView imageView = new ImageView(String.valueOf(OldMain.class.getResource(filePath)));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(size);
        return imageView;
    }
}
