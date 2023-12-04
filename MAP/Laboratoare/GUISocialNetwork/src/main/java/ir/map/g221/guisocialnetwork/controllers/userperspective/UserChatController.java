package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.guiutils.*;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserChatController extends AbstractTabController implements Observer {
    private User user;
    private User receptor;
    private ChatLogic chatLogic;
    private BuildContainer buildContainer = null;
    private final ObservableList<User> receptorsModel = FXCollections.observableArrayList();
    private final ObservableList<HBox> messageHBoxes = FXCollections.observableArrayList();
    private final Bijection<Message, HBox> messageHBoxBijection = new Bijection<>();
    private final Bijection<HBox, MessageLabel> hBoxMessageLabelBijection = new Bijection<>();

    @FXML ListView<User> userListView;
    @FXML ListView<HBox> chatListView;
    @FXML TextArea textAreaMessage;

    @FXML Button buttonSend;
    @FXML Button buttonReply;
    @FXML Button buttonEdit;
    @FXML Button buttonDelete;
    @FXML Button buttonShare;

    EventHandler<MessageChangeEvent> messageNotificationHandler =
            e -> {
                switch (e.getChangeEventType()) {
                    case ADD :
                        if (e.getNewData().getTo().contains(user)) {

                            if (isSelected() && e.getNewData().belongsToConversation(user, receptor)) {
                                NotificationAlerter.playSound(SoundFile.MESSAGE_SOUND_2);
                            }
                            else {
                                NotificationAlerter.playSound(SoundFile.MESSAGE_SOUND_1);
                                if (!isSelected()) {
                                    NotificationAlerter.displayNotification("New message",
                                            "New message from " +
                                                    e.getNewData().getFrom().getFirstName() + " " +
                                                    e.getNewData().getFrom().getLastName(),
                                            Image.MESSAGE);
                                }
                            }
                        }
                        break;
                    default:;
                }
            };

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
//        chatListView.getSelectionModel().selectedItemProperty().addListener(
//                (obs, oldSelection, newSelection) -> {
//            hBoxMessageLabelBijection.imageOf(newSelection).onMouseClicked();
//        });
        buttonReply.setGraphic(Image.REPLY.createImageView());
        buttonEdit.setGraphic(Image.EDIT.createImageView());
        buttonDelete.setGraphic(Image.DELETE.createImageView());
        buttonShare.setGraphic(Image.SHARE.createImageView());
        userListView.setItems(receptorsModel);
        chatListView.setItems(messageHBoxes);
    }

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getUserService().addObserver(this);
        buildContainer.getMessageService().addObserver(this);

        chatLogic = new ChatLogic(user, buttonSend, buttonReply,
                buttonEdit, buttonDelete, buttonShare, textAreaMessage,
                messageHBoxBijection, hBoxMessageLabelBijection);
        receptorsModel.setAll(buildContainer.getMessageService().getConversationUsers(user.getId()));
        closeChat();
    }

    private void addReceptor(User user) {
        if (!receptorsModel.contains(user)) {
            receptorsModel.add(user);
        }
//        userListView.refresh();
        /// TODO test adding a new conversation by sending a message from new friend
    }

    private void removeReceptor(User receptor) {
        if (this.receptor.equals(receptor)) {
            closeChat();
        }
        receptorsModel.remove(receptor);
    }

    private void reloadChat(User receptor) {
        if (!receptor.equals(this.receptor)) {
            this.receptor = receptor;
            userListView.getSelectionModel().select(receptor);
        }
        chatLogic.deselectLabel();
        textAreaMessage.setDisable(false);
        buttonSend.setDisable(false);
        buttonShare.setDisable(false);

        messageHBoxBijection.clear();
        hBoxMessageLabelBijection.clear();

        List<Message> messageList = buildContainer.getMessageService()
                .getConversation(user.getId(), receptor.getId());
        List<HBox> hBoxes = new ArrayList<>();
        messageList.forEach(message -> {
                    HBox hBox = createHBoxWithMessage(message);
                    messageHBoxBijection.putPair(message, hBox);
                    hBoxes.add(hBox);
                });
        messageHBoxes.setAll(hBoxes);
        chatListView.scrollTo(messageHBoxes.size() - 1);
    }

    private void closeChat() {
        messageHBoxBijection.clear();
        hBoxMessageLabelBijection.clear();

        messageHBoxes.clear();
        chatLogic.deselectLabel();
        textAreaMessage.setText("");
        receptor = null;
        textAreaMessage.setDisable(true);
        buttonSend.setDisable(true);
        buttonShare.setDisable(true);
    }

    private void addMessage(Message message) {
        /// TODO apply these functions after modifying the repository save() return logic
        messageHBoxBijection.putPair(message, createHBoxWithMessage(message));
        messageHBoxes.add(messageHBoxBijection.imageOf(message));
    }

    private void updateMessage(Message oldMessage, Message newMessage) {
        /// TODO apply these functions after modifying the repository save() return logic
        HBox oldHBox = messageHBoxBijection.imageOf(oldMessage);
        int messageIndex = messageHBoxes.indexOf(oldHBox);

        hBoxMessageLabelBijection.removePairOfX(oldHBox);
        messageHBoxBijection.removePairOfX(oldMessage);
        messageHBoxBijection.putPair(newMessage, createHBoxWithMessage(newMessage));

        messageHBoxes.set(messageIndex, messageHBoxBijection.imageOf(newMessage));
    }

    private void removeMessage(Message message) {
        /// TODO apply these functions after modifying the repository save() return logic
        HBox deletedHBox = messageHBoxBijection.imageOf(message);

        hBoxMessageLabelBijection.removePairOfX(deletedHBox);
        messageHBoxBijection.removePairOfX(message);

        messageHBoxes.remove(deletedHBox);
    }

    @NotNull
    private MessageLabel createMessageLabelFrom(Message message) {
        MessageLabel label;
        if (message.getFrom().equals(user)) {
            label = new MessageLabel(message.getMessage(), message,
                    "chat-message", "chat-sender-message",
                    "selected-chat-sender-message", chatLogic);
        }
        else {
            label = new MessageLabel(message.getMessage(), message,
                    "chat-message", "chat-receptor-message",
                    "selected-chat-receptor-message", chatLogic);
        }
        label.setWrapText(true);
        label.setMaxWidth(200);
//                        label.setPrefWidth(100); // Set an initial preferred width
//                        label.setMinWidth(Region.USE_PREF_SIZE);

        return label;
    }

    @NotNull
    private Label createRepliedMessageLabelFrom(ReplyMessage replyMessage) {
        Label label = new Label(replyMessage.getMessageRepliedTo().getMessage());
        label.setWrapText(true);
        label.setMaxWidth(150);

        label.getStyleClass().add("chat-message");
        if (!replyMessage.getMessageRepliedTo().belongsToConversation(user, receptor)) {
            label.getStyleClass().add("external-replied-message");
        }
        else {
            if (replyMessage.getMessageRepliedTo().getFrom().equals(user)) {
                label.getStyleClass().add("chat-sender-message");
            }
            else {
                label.getStyleClass().add("chat-receptor-message");
            }

            label.setOnMouseClicked(event -> {
                System.out.println("Replied message clicked : " + replyMessage.getMessage());

                HBox hboxReplied = messageHBoxBijection.imageOf(replyMessage.getMessageRepliedTo());
                if (hboxReplied != null) {
                    chatListView.scrollTo(hboxReplied);
                    chatLogic.setSelectedLabel(hBoxMessageLabelBijection.imageOf(hboxReplied));
                }
            });
        }

        return label;
    }

    /**
     * Creates a new Horizontal Box containing the label(s) needed to represent the message,
     * and also links the HBox to the selectable Message Label in their bijection container.
     * @param message the new message to be contained inside the HBox
     * @return the newly created and linked HBox
     */
    private HBox createHBoxWithMessage(Message message) {
        MessageLabel messageLabel = createMessageLabelFrom(message);

        HBox hBox;
        if (message instanceof ReplyMessage) {
            ImageView replyToIcon = Image.REPLY_TO_ICON.createImageView();
            Label labelReplyToIcon = new Label();
            labelReplyToIcon.getStyleClass().add("icon-label");
            labelReplyToIcon.setGraphic(replyToIcon);

            if (message.getFrom().equals(user)) {
                hBox = new HBox(createRepliedMessageLabelFrom((ReplyMessage)message), labelReplyToIcon, messageLabel);
            }
            else {
                replyToIcon.setScaleX(-1);
                hBox = new HBox(messageLabel, labelReplyToIcon, createRepliedMessageLabelFrom((ReplyMessage)message));
            }
        }
        else {
            hBox = new HBox(messageLabel);
        }
        hBoxMessageLabelBijection.putPair(hBox, messageLabel);

        hBox.setSpacing(10);
        if (message.getFrom().equals(user)) {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(3, 0, 3, 30));
        }
        else {
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(3, 30, 3, 0));
        }

        return hBox;
    }

    @FXML public void handleSendMessageButton(ActionEvent actionEvent) {
        try {
            Message selectedMessage;
            switch(chatLogic.getState()) {
                case SELECTED:
                case UNSELECTED:
                    buildContainer.getMessageService()
                            .sendMessageNow(user.getId(),
                                    List.of(receptor.getId()),
                                    textAreaMessage.getText());
                    break;
                case EDIT:
                    selectedMessage = messageHBoxBijection.preimageOf(
                            hBoxMessageLabelBijection.preimageOf(
                                    chatLogic.getSelectedLabel()));
                    buildContainer.getMessageService()
                            .editMessage(
                                    selectedMessage.getId(),
                                    textAreaMessage.getText());
                    break;
                case REPLY:
                    selectedMessage = messageHBoxBijection.preimageOf(
                            hBoxMessageLabelBijection.preimageOf(
                                    chatLogic.getSelectedLabel()));
                    buildContainer.getMessageService()
                            .sendReplyMessageNow(
                                user.getId(),
                                List.of(receptor.getId()),
                                textAreaMessage.getText(),
                                selectedMessage.getId());
                    break;
                default:;
            }
            chatLogic.deselectLabel();
        }
        catch (Exception e) {
            MessageAlerter.showErrorMessage(null, "Send error",
                    "Error sending message:\n" + e.getMessage());
        }
    }

    public void handleReplyButton(ActionEvent actionEvent) {
        chatLogic.setState(ChatLogic.MessageState.REPLY);
    }

    public void handleEditButton(ActionEvent actionEvent) {
        chatLogic.setState(ChatLogic.MessageState.EDIT);
    }

    public void handleDeleteButton(ActionEvent actionEvent) {
        buildContainer.getMessageService()
                .removeMessage(
                        messageHBoxBijection.preimageOf(
                                hBoxMessageLabelBijection.preimageOf(
                                        chatLogic.getSelectedLabel()))
                        .getId());
        chatLogic.deselectLabel();
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
                        User sender = messageChangeEvent.getNewData().getFrom();
                        if (messageChangeEvent.getNewData().belongsToConversation(user, receptor)) {
                            reloadChat(receptor);
                            chatListView.scrollTo(chatListView.getItems().size());
                        }
                        else if (messageChangeEvent.getNewData().getTo().contains(user) &&
                                !receptorsModel.contains(sender)) {
                            addReceptor(sender);
                        }

                        if (sender.equals(user)) {
                            messageChangeEvent.getNewData().getTo().forEach(receptor -> {
                                if (!receptorsModel.contains(receptor)) {
                                    addReceptor(receptor);
                                }
                            });
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
                messageNotificationHandler.handle(messageChangeEvent);
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

    @FXML void handleShareToUsers() {
        try {
            FXMLLoader userPerspectiveLoader = new FXMLLoader();
            userPerspectiveLoader.setLocation(OldMain.class.getResource("views/share-to-users.fxml"));

            AnchorPane root = userPerspectiveLoader.load();

            // Create the dialog Stage.
            Stage stage = new Stage();
            stage.setTitle("Share to users");
            stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(OldMain.class.getResource("css/style.css"))
                    .toExternalForm());
            stage.setScene(scene);

            ShareToUsersController shareToUsersController = userPerspectiveLoader.getController();
            shareToUsersController.setContent(buildContainer, user, receptor, chatLogic,
                    textAreaMessage, messageHBoxBijection.preimageOf(
                            hBoxMessageLabelBijection.preimageOf(
                                    chatLogic.getSelectedLabel())),
                    stage);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void dispose() {
        buildContainer.getUserService().removeObserver(this);
        buildContainer.getMessageService().removeObserver(this);
    }
}
