package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import javafx.scene.control.Label;

public class MessageLabel extends Label {
    private final Message message;

    public MessageLabel(String text, Message message) {
        super(text);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
