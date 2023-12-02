package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MessageLogic {
    public enum State {
        UNSELECTED, SELECTED, EDIT, REPLY;
    }
    private State state;
    private final User user;
    private Button buttonSend;
    private Button buttonReply;
    private Button buttonEdit;
    private Button buttonDelete;
    private TextArea textArea;
    private MessageLabel selectedLabel;

    public MessageLogic(User user, Button buttonSend, Button buttonReply,
                        Button buttonEdit, Button buttonDelete,
                        TextArea textArea) {
        this.user = user;
        this.buttonSend = buttonSend;
        this.buttonReply = buttonReply;
        this.buttonEdit = buttonEdit;
        this.buttonDelete = buttonDelete;
        this.textArea = textArea;
        this.selectedLabel = null;
        setState(State.UNSELECTED);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        switch(state) {
            case UNSELECTED:
                buttonReply.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
                buttonSend.setText("Send");
                textArea.setText("");
                break;
            case SELECTED:
                buttonReply.setDisable(false);
                buttonEdit.setDisable(false);
                buttonDelete.setDisable(false);
                buttonSend.setText("Send");
                break;
            case EDIT:
                buttonReply.setDisable(false);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(false);
                buttonSend.setText("Re-send");
                textArea.setText(selectedLabel.getText());
                break;
            case REPLY:
                buttonReply.setDisable(true);
                buttonEdit.setDisable(false);
                buttonDelete.setDisable(false);
                buttonSend.setText("Reply");
                break;
            default:;
        }
        if (selectedLabel != null && !selectedLabel.getMessage().getFrom().equals(user)) {
            buttonEdit.setDisable(true);
            buttonDelete.setDisable(true);
        }
        this.state = state;
    }

    public MessageLabel getSelectedLabel() {
        return selectedLabel;
    }

    public void setSelectedLabel(MessageLabel label) {
        if (!label.equals(this.selectedLabel)) {
            if (this.selectedLabel != null) {
                this.selectedLabel.setUnselectedStyle();
            }
            label.setSelectedStyle();
            this.selectedLabel = label;
            setState(State.SELECTED);
        }
    }

    public void deselectLabel() {
        if (this.selectedLabel != null) {
            this.selectedLabel.setUnselectedStyle();
        }
        this.selectedLabel = null;
        setState(State.UNSELECTED);
    }

    public boolean isSelected(Label label) {
        return label.equals(selectedLabel);
    }

}
