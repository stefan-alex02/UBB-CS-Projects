package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageLabel;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageLogic;
import ir.map.g221.guisocialnetwork.domain.entities.Message;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.OpenedEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.Bijection;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

public class ShareToUsersController extends AbstractTabController implements Observer {
    private User user;
    private User receptor;
    private MessageLogic messageLogic;
    private BuildContainer buildContainer = null;
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();

    @FXML ListView<User> listViewUsers;
    private Stage stage;
    private TextArea textAreaMessage;
    private Bijection<Message, HBox> messageHBoxBijection = new Bijection<>();
    private Bijection<HBox, MessageLabel> hBoxMessageLabelBijection = new Bijection<>();


    @FXML
    public void initialize() {
        listViewUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewUsers.setItems(usersModel);
    }

    public void setContent(BuildContainer buildContainer, User user, User receptor,
                           MessageLogic messageLogic, TextArea textAreaMessage,
                           Bijection<Message, HBox> messageHBoxBijection,
                           Bijection<HBox, MessageLabel> hBoxMessageLabelBijection,
                           Stage stage) {
        this.buildContainer = buildContainer;
        this.user = user;
        this.receptor = receptor;
        this.messageLogic = messageLogic;
        this.textAreaMessage = textAreaMessage;
        this.messageHBoxBijection = messageHBoxBijection;
        this.hBoxMessageLabelBijection = hBoxMessageLabelBijection;
        this.stage = stage;
        this.buildContainer.getUserService().addObserver(this);
        this.buildContainer.getFriendshipService().addObserver(this);
        this.buildContainer.getFriendRequestService().addObserver(this);

        initUserModel();
    }

    public void initUserModel() {
        if (messageLogic.getState() == MessageLogic.State.REPLY) {
            Set<User> commonFriends = buildContainer.getFriendshipService()
                    .getFriendsOfUser(user.getId());
            commonFriends.retainAll(buildContainer
                    .getFriendshipService()
                    .getFriendsOfUser(receptor.getId()));

            usersModel.setAll(commonFriends);
        }
        else {
            usersModel.setAll(buildContainer.getFriendshipService().getFriendsOfUser(user.getId()));
        }
    }

    @FXML void handleShare() {
        try {
            Message selectedMessage;
            List<Long> receptorsIDs = listViewUsers.getSelectionModel()
                    .getSelectedItems()
                    .stream()
                    .map(User::getId)
                    .toList();
            switch(messageLogic.getState()) {
                case SELECTED:
                    selectedMessage = messageHBoxBijection.preimageOf(
                            hBoxMessageLabelBijection.preimageOf(
                                    messageLogic.getSelectedLabel()));
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
                    selectedMessage = messageHBoxBijection.preimageOf(
                            hBoxMessageLabelBijection.preimageOf(
                                    messageLogic.getSelectedLabel()));
                    buildContainer.getMessageService()
                            .sendReplyMessageNow(
                                    user.getId(),
                                    receptorsIDs,
                                    textAreaMessage.getText(),
                                    selectedMessage.getId());
                    break;
                default:;
            }
            messageLogic.deselectLabel();
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
