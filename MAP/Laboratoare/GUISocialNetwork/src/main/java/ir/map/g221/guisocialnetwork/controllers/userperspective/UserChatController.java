package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.othercontrollers.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UserChatController implements Observer {
    private User user;
    private User receptor;
    private Label selectedLabelMessage = null;
    private BuildContainer buildContainer = null;
    private final ObservableList<User> receptorsModel = FXCollections.observableArrayList();

    @FXML ListView<User> listView;
    @FXML VBox chatBox;
    @FXML TextArea textAreaMessage;
    @FXML Button sendButton;

    @FXML
    public void initialize() {
        listView.setCellFactory(new Callback<>() {
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
                    if (event.getClickCount() == 2 && (!cell.isEmpty()) ) {
                        textAreaMessage.setDisable(false);
                        sendButton.setDisable(false);
                        reloadChat(cell.getItem());
                    }
                });
                return cell;
            }
        });
        listView.setItems(receptorsModel);
    }

    private HBox createHBoxWithMessage(Message message) {
        Label label = new Label(message.getMessage());
        label.setWrapText(true);
        label.setMaxWidth(200); // Set a maximum width
//                        label.setPrefWidth(100); // Set an initial preferred width
//                        label.setMinWidth(Region.USE_PREF_SIZE);
        label.getStyleClass().add("chat-message");

        label.setOnMouseClicked(event -> {
            System.out.println("Message clicked : " + message.getMessage());
            if (selectedLabelMessage == null) {
                label.getStyleClass().add("selected-chat-message");
                selectedLabelMessage = label;
            }
            else if (selectedLabelMessage.equals(label)){
                label.getStyleClass().remove("selected-chat-message");
                selectedLabelMessage = null;
            }
            else {
                selectedLabelMessage.getStyleClass().remove("selected-chat-message");
                label.getStyleClass().add("selected-chat-message");
                selectedLabelMessage = label;
            }
        });

        HBox messageContainer = new HBox(label);
//                        messageContainer.setMaxWidth(400);
        if (message.getFrom().equals(user)) {
            messageContainer.setAlignment(Pos.CENTER_RIGHT);
            messageContainer.setPadding(new Insets(0, 0, 0, 30));
        }
        else {
            messageContainer.setAlignment(Pos.CENTER_LEFT);
            messageContainer.setPadding(new Insets(0, 30, 0, 0));
        }
//                        messageContainer.prefWidthProperty().bind(chatBox.widthProperty());
//                        VBox.setVgrow(messageContainer, Priority.SOMETIMES);

        messageContainer.setOnMouseEntered(event -> {
            System.out.println("Message container hovered : " + message.getMessage());
        });
        return messageContainer;
    }

    private void reloadChat(User receptor) {
        this.receptor = receptor;
        chatBox.getChildren().setAll(
                buildContainer.getMessageService()
                    .getConversation(user.getId(), receptor.getId())
                    .stream()
                    .map(this::createHBoxWithMessage)
                    .toList()
        );
    }

    @FXML private void handleSendMessageButton() {
        try {
            buildContainer.getMessageService()
                    .sendMessageNow(user.getId(), List.of(receptor.getId()), textAreaMessage.getText());
            textAreaMessage.setText("");
        }
        catch (Exception e) {
            MessageAlerter.showErrorMessage(null, "Send error",
                    "Error sending message:\n" + e.getMessage());
        }
    }

    public void setBuildContainer(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getUserService().addObserver(this);
        buildContainer.getMessageService().addObserver(this);
        initUserModel();
    }

    private void initUserModel() {
        receptorsModel.setAll(buildContainer.getMessageService().getConversationUsers(user.getId()));
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                        !userChangeEvent.getOldData().equals(user)) {
                    if (receptorsModel.contains(userChangeEvent.getOldData())) {
                        if (receptor.equals(userChangeEvent.getOldData())) {
                            chatBox.getChildren().clear();
                            selectedLabelMessage = null;
                            textAreaMessage.setText("");
                            receptor = null;
                            textAreaMessage.setDisable(true);
                            sendButton.setDisable(true);
                        }
                        receptorsModel.remove(userChangeEvent.getOldData());
                    }
                }
                break;
            case MESSAGE:
                MessageChangeEvent messageChangeEvent = (MessageChangeEvent) event;
                if (messageChangeEvent.getNewData().belongsToConversation(user, receptor)) {
                    if (messageChangeEvent.getChangeEventType() == ChangeEventType.ADD) {
                        chatBox.getChildren()
                                .add(createHBoxWithMessage(messageChangeEvent.getNewData()));
                    }
                }
                else if (!receptorsModel.contains(messageChangeEvent.getNewData().getFrom())) {
                    receptorsModel.add(messageChangeEvent.getNewData().getFrom());
                    /// TODO test adding a new conversation by sending a message from new friend
                }
                break;
            default:;
        }
    }

    public void dispose() {
        buildContainer.getUserService().removeObserver(this);
        buildContainer.getMessageService().removeObserver(this);
    }
}
