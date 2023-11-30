package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.othercontrollers.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.othercontrollers.NotificationAlerter;
import ir.map.g221.guisocialnetwork.controllers.othercontrollers.NotificationImage;
import ir.map.g221.guisocialnetwork.controllers.othercontrollers.SoundFile;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequestStatus;
import ir.map.g221.guisocialnetwork.domain.entities.User;
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

public class FriendRequestController implements Observer {
    private User user = null;
    private BuildContainer buildContainer = null;
    private final ObservableList<FriendRequest> friendRequestsModel = FXCollections.observableArrayList();
    public TableView<FriendRequest> tableView;

    @FXML TableColumn<FriendRequest, String> tableColumnFirstName;
    @FXML TableColumn<FriendRequest, String> tableColumnLastName;
    @FXML TableColumn<FriendRequest, LocalDateTime> tableColumnDate;
    @FXML TableColumn<FriendRequest, Void> tableColumnApprove;
    @FXML TableColumn<FriendRequest, Void> tableColumnReject;

    EventHandler<FriendRequestChangeEvent> friendRequestNotificationHandler =
        e -> {
            switch (e.getChangeEventType()) {
                case ADD :
                    if (e.getNewData().getTo().equals(user)) {
                        NotificationAlerter.playSound(SoundFile.RING_SOUND_1);

                        NotificationAlerter.displayNotification("Friend request",
                                "New friend request from " +
                                        e.getNewData().getFrom().getFirstName() + " " +
                                        e.getNewData().getFrom().getLastName(),
                                NotificationImage.USERS_1);
                    }
                    break;
                case UPDATE :
                    if (e.getNewData().getFrom().equals(user) &&
                        e.getNewData().getStatus() == FriendRequestStatus.APPROVED) {
                        NotificationAlerter.playSound(SoundFile.RING_SOUND_1);

                        NotificationAlerter.displayNotification("Friend request",
                                e.getNewData().getTo().getFirstName() + " " +
                                        e.getNewData().getTo().getLastName() +
                                        " accepted your friend request.",
                                NotificationImage.USERS_1);
                    }
                    break;
                default:;
            }
    };

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getUserService().addObserver(this);
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(friendRequest ->
            new ObservableValueBase<>() {
                @Override
                public String getValue() {
                    return friendRequest.getValue().getFrom().getFirstName();
                }
            });
        tableColumnLastName.setCellValueFactory(friendRequest ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return friendRequest.getValue().getFrom().getLastName();
                    }
                });
        tableColumnDate.setCellValueFactory(friendRequest ->
                new ObservableValueBase<>() {
                    @Override
                    public LocalDateTime getValue() {
                        return friendRequest.getValue().getDate();
                    }
                });

        tableColumnApprove.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendRequest, Void> call(final TableColumn<FriendRequest, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Approve");

                    {
                        button.setOnAction(event -> {
                            FriendRequest friendRequest = getTableView().getItems().get(getIndex());

                            try {
                                buildContainer.getFriendRequestService().approveFriendRequest(friendRequest.getId());
                            }
                            catch(RuntimeException re) {
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
                            setAlignment(javafx.geometry.Pos.CENTER); // Center the button
                            button.getStyleClass().add("green-button"); // Apply a style from CSS
                        }
                    }
                };
            }
        });

        tableColumnReject.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendRequest, Void> call(final TableColumn<FriendRequest, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Reject");

                    {
                        button.setOnAction(event -> {
                            FriendRequest friendRequest = getTableView().getItems().get(getIndex());

                            try {
                                buildContainer.getFriendRequestService().rejectFriendRequest(friendRequest.getId());
                            }
                            catch(RuntimeException re) {
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
                            setAlignment(javafx.geometry.Pos.CENTER); // Center the button
                            button.getStyleClass().add("red-button"); // Apply a style from CSS
                        }
                    }
                };
            }
        });

        tableColumnApprove.setSortable(false);
        tableColumnReject.setSortable(false);
        tableView.setItems(friendRequestsModel);
    }

    private void initUserModel() {
        friendRequestsModel.setAll(buildContainer.getFriendRequestService().getPendingFriendRequests(user.getId()));
        user = buildContainer.getUserService().getUser(user.getId());
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() != ChangeEventType.DELETE ||
                        !userChangeEvent.getOldData().equals(user)) {
                    initUserModel();
                }
                break;
            case FRIEND_REQUEST:
                if (event.getEventType() == EventType.FRIEND_REQUEST) {
                    FriendRequestChangeEvent friendRequestChangeEvent = (FriendRequestChangeEvent) event;
                    initUserModel();
                    friendRequestNotificationHandler.handle(friendRequestChangeEvent);
                }
                break;
            case OPENED:
                if (friendRequestsModel.isEmpty()) {
                    initUserModel();
                }
                break;
            default:;
        }
    }

    public void dispose() {
        buildContainer.getFriendRequestService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
