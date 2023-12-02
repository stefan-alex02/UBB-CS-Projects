package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.Image;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageLabel;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageLogic;
import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.ReplyMessage;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.generictypes.Bijection;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class UserChatController extends AbstractTabController implements Observer {
    private User user;
    private User receptor;
    private MessageLogic messageLogic;
    private BuildContainer buildContainer = null;
    private final ObservableList<User> receptorsModel = FXCollections.observableArrayList();
    private final ObservableList<HBox> messageHBoxes = FXCollections.observableArrayList();
    private final Bijection<Message, HBox> messageHBoxBijection = new Bijection<>();

    @FXML ListView<User> userListView;
    @FXML ListView<HBox> chatListView;
    @FXML TextArea textAreaMessage;

    @FXML Button buttonSend;
    @FXML Button buttonReply;
    @FXML Button buttonEdit;
    @FXML Button buttonDelete;

    @FXML
    public void initialize() {
        userListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            setText(user.getFirstName() + " " + user.getLastName());
                        }
                    }
                };
                cell.setOnMouseClicked(event -> {
                    if (!cell.isEmpty()) {
                        reloadChat(cell.getItem());
                    }
                });
                cell.getStyleClass().add("user-list-cell");
                return cell;
            }
        });
        chatListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<HBox> call(ListView<HBox> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(item);
                        }
                        if (!getStyleClass().contains("chat-hbox")) {
                            getStyleClass().add("chat-hbox");
                        }
                    }
                };
            }
        });
        buttonReply.setGraphic(Image.REPLY.createImageView());
        buttonEdit.setGraphic(Image.EDIT.createImageView());
        buttonDelete.setGraphic(Image.DELETE.createImageView());
        userListView.setItems(receptorsModel);
        chatListView.setItems(messageHBoxes);
    }

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getUserService().addObserver(this);
        buildContainer.getMessageService().addObserver(this);

        messageLogic = new MessageLogic(user, buttonSend, buttonReply,
                buttonEdit, buttonDelete, textAreaMessage);
        receptorsModel.setAll(buildContainer.getMessageService().getConversationUsers(user.getId()));
        closeChat();
    }

    private void addReceptor(User user) {
        if (!receptorsModel.contains(user)) {
            receptorsModel.add(user);
        }
        /// TODO test adding a new conversation by sending a message from new friend
    }

    private void removeReceptor(User user) {
        if (receptor.equals(user)) {
            closeChat();
        }
        receptorsModel.remove(user);
    }

    private void reloadChat(User receptor) {
        if (!receptor.equals(this.receptor)) {
            this.receptor = receptor;
            userListView.getSelectionModel().select(receptor);
        }
        messageLogic.deselectLabel();
        textAreaMessage.setDisable(false);
        buttonSend.setDisable(false);
        messageHBoxBijection.clear();
        List<Message> messageList = buildContainer.getMessageService()
                .getConversation(user.getId(), receptor.getId());
        List<HBox> hBoxes = new ArrayList<>();
        messageList.forEach(message -> {
                    HBox hBox = createHBoxWithMessage(message);
                    messageHBoxBijection.putPair(message, hBox);
                    hBoxes.add(hBox);
                });
        messageHBoxes.setAll(hBoxes);
    }

    private void closeChat() {
        messageHBoxBijection.clear();
        messageHBoxes.clear();
        messageLogic.deselectLabel();
        textAreaMessage.setText("");
        receptor = null;
        textAreaMessage.setDisable(true);
        buttonSend.setDisable(true);
    }

    private void addMessage(Message message) {
        messageHBoxBijection.putPair(message, createHBoxWithMessage(message));
        messageHBoxes.add(messageHBoxBijection.imageOf(message));
    }

    private void updateMessage(Message oldMessage, Message newMessage) {
        int messageIndex = messageHBoxes.indexOf(messageHBoxBijection.imageOf(oldMessage));
        messageHBoxBijection.removePairOfX(oldMessage);
        messageHBoxBijection.putPair(newMessage, createHBoxWithMessage(newMessage));
        messageHBoxes.set(messageIndex, messageHBoxBijection.imageOf(newMessage));
    }

    private void removeMessage(Message message) {
        messageHBoxes.remove(messageHBoxBijection.imageOf(message));
        messageHBoxBijection.removePairOfX(message);
    }

    private Label createReplyLabelFrom(ReplyMessage replyMessage) {
        Label label = new Label(replyMessage.getMessageRepliedTo().getMessage());
        label.setWrapText(true);
        label.setMaxWidth(150);
        label.getStyleClass().add("chat-message");

        return label;
    }

    private HBox createHBoxWithMessage(Message message) {
        MessageLabel label = new MessageLabel(message.getMessage(), message);
        label.setWrapText(true);
        label.setMaxWidth(200);
//                        label.setPrefWidth(100); // Set an initial preferred width
//                        label.setMinWidth(Region.USE_PREF_SIZE);
        label.getStyleClass().add("chat-message");

        label.setOnMouseClicked(event -> {
            System.out.println("Message clicked : " + message.getMessage());
            if (messageLogic.isSelected(label)) {
                messageLogic.deselectLabel();
            }
            else {
                messageLogic.setSelectedLabel(label);
            }
        });

        HBox messageContainer;
        if (message instanceof ReplyMessage) {
            messageContainer = new HBox(createReplyLabelFrom((ReplyMessage)message), label);
        }
        else {
            messageContainer = new HBox(label);
        }

//                        messageContainer.setMaxWidth(400);
        if (message.getFrom().equals(user)) {
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
            messageContainer.setPadding(new Insets(3, 0, 3, 30));
        }
        else {
            messageContainer.setAlignment(Pos.CENTER_LEFT);
            messageContainer.setPadding(new Insets(3, 30, 3, 0));
        }
//                        messageContainer.prefWidthProperty().bind(chatBox.widthProperty());
//                        VBox.setVgrow(messageContainer, Priority.SOMETIMES);

        messageContainer.setOnMouseEntered(event -> {
            System.out.println("Message container hovered : " + message.getMessage());
        });

        return messageContainer;
    }

    @FXML public void handleSendMessageButton(ActionEvent actionEvent) {
        try {
            Message selectedMessage;
            switch(messageLogic.getState()) {
                case SELECTED:
                case UNSELECTED:
                    buildContainer.getMessageService()
                            .sendMessageNow(user.getId(),
                                    List.of(receptor.getId()),
                                    textAreaMessage.getText());
                    break;
                case EDIT:
                    selectedMessage =
                            messageHBoxBijection.preimageOf(
                                    messageHBoxBijection.getCodomain()
                                            .stream()
                                            .filter(hBox -> hBox.getChildren()
                                                    .contains(messageLogic.getSelectedLabel()))
                                            .findFirst().orElseThrow());

                    buildContainer.getMessageService()
                            .editMessage(
                                    selectedMessage.getId(),
                                    textAreaMessage.getText());
                    break;
                case REPLY:
                    selectedMessage = messageHBoxBijection.preimageOf(
                            messageHBoxBijection.getCodomain()
                            .stream()
                            .filter(hBox -> hBox.getChildren()
                                    .contains(messageLogic.getSelectedLabel()))
                            .findFirst().orElseThrow());

                    buildContainer.getMessageService()
                            .sendReplyMessageNow(
                                user.getId(),
                                List.of(receptor.getId()),
                                textAreaMessage.getText(),
                                selectedMessage.getId());
                    break;
                default:;
            }
            messageLogic.deselectLabel();
        }
        catch (Exception e) {
            MessageAlerter.showErrorMessage(null, "Send error",
                    "Error sending message:\n" + e.getMessage());
        }
    }

    public void handleReplyButton(ActionEvent actionEvent) {
        messageLogic.setState(MessageLogic.State.REPLY);
    }

    public void handleEditButton(ActionEvent actionEvent) {
        messageLogic.setState(MessageLogic.State.EDIT);
    }

    public void handleDeleteButton(ActionEvent actionEvent) {
        Label selectedLabel = messageLogic.getSelectedLabel();
        buildContainer.getMessageService()
                .removeMessage(
                        messageHBoxBijection.preimageOf(
                                messageHBoxBijection.getCodomain()
                                        .stream()
                                        .filter(hBox -> hBox.getChildren()
                                                .contains(selectedLabel))
                                        .findFirst().orElseThrow())
                        .getId());
        messageLogic.deselectLabel();
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                        !userChangeEvent.getOldData().equals(user)) {
                    if (receptorsModel.contains(userChangeEvent.getOldData())) {
                        removeReceptor(userChangeEvent.getOldData());
                    }
                }
                break;
            case MESSAGE:
                ///TODO optimize chat updating by returning messages with their
                /// generated id from repo after calling save()

                MessageChangeEvent messageChangeEvent = (MessageChangeEvent) event;
                switch(messageChangeEvent.getChangeEventType()) {
                    case ADD:
                        if (messageChangeEvent.getNewData().belongsToConversation(user, receptor)) {
                            reloadChat(receptor);
                            chatListView.scrollTo(chatListView.getItems().size());
                        }
                        else if (messageChangeEvent.getNewData().getTo().contains(user) &&
                                !receptorsModel.contains(messageChangeEvent.getNewData().getFrom())) {
                            addReceptor(messageChangeEvent.getNewData().getFrom());
                        }
                        break;
                    case UPDATE:
                        if (messageChangeEvent.getNewData().belongsToConversation(user, receptor)) {
                            reloadChat(receptor);
                        }
                        break;
                    case DELETE:
                        if (messageChangeEvent.getOldData().belongsToConversation(user, receptor)) {
                            reloadChat(receptor);
                        }
                        break;
                    default:;
                }
                break;
            case CHAT_USER:
                ChatUserEvent chatUserEvent = (ChatUserEvent) event;
                if (chatUserEvent.getChangeEventType() == ChangeEventType.ADD) {
                    addReceptor(chatUserEvent.getUserData());
                }
                reloadChat(chatUserEvent.getUserData());
                break;
            default:;
        }
    }

    public void dispose() {
        buildContainer.getUserService().removeObserver(this);
        buildContainer.getMessageService().removeObserver(this);
    }
}
