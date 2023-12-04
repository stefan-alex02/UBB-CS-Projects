package ir.map.g221.guisocialnetwork.controllers.guiutils;

import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.generictypes.Bijection;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class ChatLogic {
    public enum MessageState {
        UNSELECTED, SELECTED, EDIT, REPLY, CLOSED;
    }
    private MessageState messageState;
    private final User user;
    private Button buttonSend;
    private Button buttonReply;
    private Button buttonEdit;
    private Button buttonDelete;
    private Button buttonShare;
    private TextArea textArea;
    private final Bijection<Message, HBox> messageHBoxBijection;
    private final Bijection<HBox, MessageLabel> hBoxMessageLabelBijection;
    private MessageLabel selectedLabel;

    public ChatLogic(User user, Button buttonSend, Button buttonReply,
                     Button buttonEdit, Button buttonDelete,
                     Button buttonShare,
                     TextArea textArea, Bijection<Message, HBox> messageHBoxBijection,
                     Bijection<HBox, MessageLabel> hBoxMessageLabelBijection) {
        this.user = user;
        this.buttonSend = buttonSend;
        this.buttonReply = buttonReply;
        this.buttonEdit = buttonEdit;
        this.buttonDelete = buttonDelete;
        this.buttonShare = buttonShare;
        this.textArea = textArea;
        this.messageHBoxBijection = messageHBoxBijection;
        this.hBoxMessageLabelBijection = hBoxMessageLabelBijection;
        this.selectedLabel = null;
        setState(MessageState.UNSELECTED);
    }

    public MessageState getState() {
        return messageState;
    }

    public void setState(MessageState messageState) {
        if (this.messageState == MessageState.EDIT) {
            textArea.setText("");
        }
        switch(messageState) {
            case CLOSED:
                buttonReply.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
                buttonShare.setDisable(false);
                textArea.setDisable(false);
                buttonSend.setDisable(true);
                buttonSend.setText("Send");
                textArea.setText("");
                break;
            case UNSELECTED:
                buttonReply.setDisable(true);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(true);
                buttonShare.setDisable(false);
                textArea.setDisable(false);
                buttonSend.setText("Send");
                break;
            case SELECTED:
                buttonReply.setDisable(false);
                buttonEdit.setDisable(false);
                buttonDelete.setDisable(false);
                buttonShare.setDisable(false);
                textArea.setDisable(true);
                buttonSend.setText("Send");
                break;
            case EDIT:
                buttonReply.setDisable(false);
                buttonEdit.setDisable(true);
                buttonDelete.setDisable(false);
                buttonShare.setDisable(true);
                textArea.setDisable(false);
                buttonSend.setText("Re-send");
                textArea.setText(selectedLabel.getText());
                break;
            case REPLY:
                buttonReply.setDisable(true);
                buttonEdit.setDisable(false);
                buttonDelete.setDisable(false);
                buttonShare.setDisable(false);
                textArea.setDisable(false);
                buttonSend.setText("Reply");
                break;
            default:;
        }
        if (selectedLabel != null && !getSelectedMessage().getFrom().equals(user)) {
            buttonEdit.setDisable(true);
            buttonDelete.setDisable(true);
        }
        this.messageState = messageState;
    }

    public MessageLabel getSelectedLabel() {
        return selectedLabel;
    }

    public Message getSelectedMessage() {
        return messageHBoxBijection.preimageOf(
                hBoxMessageLabelBijection.preimageOf(selectedLabel));
    }

    public void setSelectedLabel(MessageLabel label) {
        if (!label.equals(this.selectedLabel)) {
            if (this.selectedLabel != null) {
                this.selectedLabel.setUnselectedStyle();
            }
            label.setSelectedStyle();
            this.selectedLabel = label;
            setState(MessageState.SELECTED);
        }
    }

    public void deselectLabel() {
        if (this.selectedLabel != null) {
            this.selectedLabel.setUnselectedStyle();
        }
        this.selectedLabel = null;
        setState(MessageState.UNSELECTED);
    }

    public boolean isSelected(Label label) {
        return label.equals(selectedLabel);
    }

    public void closeChat() {
        if (this.selectedLabel != null) {
            this.selectedLabel.setUnselectedStyle();
        }
        this.selectedLabel = null;
        setState(MessageState.CLOSED);
    }
}
