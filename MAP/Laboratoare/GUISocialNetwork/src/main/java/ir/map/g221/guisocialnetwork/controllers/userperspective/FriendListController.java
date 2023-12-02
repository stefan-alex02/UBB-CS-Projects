package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.time.LocalDateTime;

public class FriendListController extends AbstractTabController implements Observer {
    private User user;
    private BuildContainer buildContainer;
    private boolean isLoaded = false;
    private final ObservableList<FriendshipDetails> friendshipDetailsModel = FXCollections.observableArrayList();
    @FXML TableView<FriendshipDetails> tableView;

    @FXML
    TableColumn<FriendshipDetails, String> tableColumnFirstName;
    @FXML TableColumn<FriendshipDetails, String> tableColumnLastName;
    @FXML TableColumn<FriendshipDetails, LocalDateTime> tableColumnFriendsFrom;
    @FXML TableColumn<FriendshipDetails, Void> tableColumnMessageFriend;
    @FXML TableColumn<FriendshipDetails, Void> tableColumnRemoveFriend;

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendshipService().addObserver(this);
        buildContainer.getUserService().addObserver(this);
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return userCell.getValue().getFriend().getFirstName();
                    }
                });
        tableColumnLastName.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return userCell.getValue().getFriend().getLastName();
                    }
                });
        tableColumnFriendsFrom.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public LocalDateTime getValue() {
                        return userCell.getValue().getFriendsFromDate();
                    }
                });

        tableColumnMessageFriend.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendshipDetails, Void> call(TableColumn<FriendshipDetails, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Send message");

                    {
                        button.setOnAction(event -> {
                            User friend = getTableView().getItems().get(getIndex()).getFriend();
                            System.out.println("Messaging user: " + friend.toString());

                            try {
                                notifyOwnerController(
                                        ChatUserEvent.ofData(ChangeEventType.ADD, friend));
                            } catch (RuntimeException re) {
                                MessageAlerter.showErrorMessage(null,
                                        "Messaging error",
                                        re.getMessage());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                            setAlignment(javafx.geometry.Pos.CENTER);
                            button.getStyleClass().add("blue-button");
                        }
                    }
                };
            }
        });

        tableColumnRemoveFriend.setCellFactory(new Callback<>()
            {
                @Override
                public TableCell<FriendshipDetails, Void> call ( final TableColumn<FriendshipDetails, Void> param){
                    return new TableCell<>() {
                    private final Button button = new Button("Remove friend");

                    {
                        button.setOnAction(event -> {
                            User friend = getTableView().getItems().get(getIndex()).getFriend();
                            System.out.println("Deleting friendship with: " + friend.toString());

                            try {
                                buildContainer.getFriendshipService()
                                        .removeFriendship(user.getId(), friend.getId());
                            } catch (RuntimeException re) {
                                MessageAlerter.showErrorMessage(null,
                                        "Database error",
                                        re.getMessage());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                            setAlignment(javafx.geometry.Pos.CENTER);
                            button.getStyleClass().add("red-button");
                        }
                    }
                };
            }
            });

        tableColumnRemoveFriend.setSortable(false);
        tableView.setItems(friendshipDetailsModel);
        }

    private void initUserModel() {
        friendshipDetailsModel.setAll(buildContainer.getFriendshipService()
                .getFriendshipDetailsStreamOfUser(user.getId()));
        user = buildContainer.getUserService().getUser(user.getId());
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                        !userChangeEvent.getOldData().equals(user)) {
                        initUserModel();
                }
                break;
            case FRIENDSHIP:
                initUserModel();
                break;
            case OPENED:
                if (!isLoaded) {
                    isLoaded = true;
                    initUserModel();
                }
                break;
            default:;
        }
    }

    public void dispose() {
        buildContainer.getFriendshipService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
