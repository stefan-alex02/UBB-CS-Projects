package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MessageLabel extends Label {
    private final Message message;
    private final String baseStyle;
    private final String unselectedStyle;
    private final String selectedStyle;
    private MessageLogic messageLogic;

    EventHandler<MouseEvent> clickEventHandler = event -> {
        if (this.messageLogic.isSelected(this)) {
            this.messageLogic.deselectLabel();
        }
        else {
            this.messageLogic.setSelectedLabel(this);
        }
    };

    public MessageLabel(String text, Message message, String baseStyle, String unselectedStyle, String selectedStyle, MessageLogic messageLogic) {
        super(text);
        this.message = message;
        this.baseStyle = baseStyle;
        this.unselectedStyle = unselectedStyle;
        this.selectedStyle = selectedStyle;
        this.messageLogic = messageLogic;

        this.getStyleClass().add(baseStyle);
        this.setUnselectedStyle();

        this.setOnMouseClicked(clickEventHandler);
    }

    public void onMouseClicked() {
        clickEventHandler.handle(null);
    }

    public Message getMessage() {
        return message;
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
