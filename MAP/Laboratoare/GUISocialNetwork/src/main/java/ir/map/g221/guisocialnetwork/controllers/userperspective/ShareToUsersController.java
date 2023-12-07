package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.ChatLogic;
import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.Set;

public class ShareToUsersController extends AbstractTabController implements Observer {
    private User user;
    private User receptor;
    private ChatLogic chatLogic;
    private BuildContainer buildContainer = null;
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();

    @FXML ListView<User> listViewUsers;
    private Message selectedMessage;
    private TextArea textAreaMessage;
    private Stage stage;


    @FXML
    public void initialize() {
        listViewUsers.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                ListCell<User> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            setText(user.getFirstName() + " " + user.getLastName());
                            setAlignment(Pos.BASELINE_CENTER);
                        }
                    }
                };
                cell.getStyleClass().add("user-list-cell");
                return cell;
            }
        });
        listViewUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewUsers.setItems(usersModel);
    }

    public void setContent(BuildContainer buildContainer, User user, User receptor,
                           ChatLogic chatLogic, TextArea textAreaMessage,
                           Message selectedMessage,
                           Stage stage) {
        this.buildContainer = buildContainer;
        this.user = user;
        this.receptor = receptor;
        this.chatLogic = chatLogic;
        this.textAreaMessage = textAreaMessage;
        this.selectedMessage = selectedMessage;
        this.stage = stage;
        this.buildContainer.getUserService().addObserver(this);
        this.buildContainer.getFriendshipService().addObserver(this);
        this.buildContainer.getFriendRequestService().addObserver(this);

        initUserModel();
    }

    public void initUserModel() {
        if (chatLogic.getState() == ChatLogic.MessageState.REPLY) {
            Set<User> commonFriends = buildContainer.getFriendshipService()
                    .getFriendsOfUser(user.getId());
            commonFriends.retainAll(buildContainer
                    .getFriendshipService()
                    .getFriendsOfUser(receptor.getId()));
            commonFriends.add(receptor);

            usersModel.setAll(commonFriends);
        }
        else {
            usersModel.setAll(buildContainer.getFriendshipService().getFriendsOfUser(user.getId()));
        }
    }

    @FXML void handleShare() {
        try {
            List<Long> receptorsIDs = listViewUsers.getSelectionModel()
                    .getSelectedItems()
                    .stream()
                    .map(User::getId)
                    .toList();
            switch(chatLogic.getState()) {
                case SELECTED:
                    // Note: a message reply will be shared as a normal message, and this is intended
                    // (FEATURE, NOT A BUG)
                    buildContainer.getMessageService()
                            .sendMessageNow(user.getId(),
                                    receptorsIDs,
                                    selectedMessage.getMessage());
                    break;
                case UNSELECTED:
                    buildContainer.getMessageService()
                            .sendMessageNow(user.getId(),
                                    receptorsIDs,
                                    textAreaMessage.getText());
                    break;
                case REPLY:
                    buildContainer.getMessageService()
                            .sendReplyMessageNow(
                                    user.getId(),
                                    receptorsIDs,
                                    textAreaMessage.getText(),
                                    selectedMessage.getId());
                    break;
                default:;
            }
            chatLogic.deselectLabel();
            stage.close();
        }
        catch (Exception e) {
            MessageAlerter.showErrorMessage(null, "Send error",
                    "Error sending message:\n" + e.getMessage());
        }
    }

    @Override
    public void update(Event event) {
        switch (event.getEventType()) {
            case USER:
            case FRIENDSHIP:
                initUserModel();
                break;
            default:;
        }
    }
}
