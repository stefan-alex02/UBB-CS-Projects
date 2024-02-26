package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MessageLabel extends Label {
    private final String baseStyle;
    private final String unselectedStyle;
    private final String selectedStyle;
    private ChatLogic chatLogic;

    EventHandler<MouseEvent> clickEventHandler = event -> {
        if (this.chatLogic.isSelected(this)) {
            this.chatLogic.deselectLabel();
        }
        else {
            this.chatLogic.setSelectedLabel(this);
        }
    };

    public MessageLabel(String text, Message message, String baseStyle, String unselectedStyle, String selectedStyle, ChatLogic chatLogic) {
        super(text);
        this.baseStyle = baseStyle;
        this.unselectedStyle = unselectedStyle;
        this.selectedStyle = selectedStyle;
        this.chatLogic = chatLogic;

        this.getStyleClass().add(baseStyle);
        this.setUnselectedStyle();

        this.setOnMouseClicked(clickEventHandler);
    }

    public void onMouseClicked() {
        clickEventHandler.handle(null);
    }

    public void setSelectedStyle() {
        this.getStyleClass().remove(unselectedStyle);
        this.getStyleClass().add(selectedStyle);
    }

    public void setUnselectedStyle() {
        this.getStyleClass().remove(selectedStyle);
        this.getStyleClass().add(unselectedStyle);
    }
}
