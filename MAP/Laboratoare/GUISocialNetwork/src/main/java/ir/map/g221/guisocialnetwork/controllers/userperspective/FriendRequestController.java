package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.othercontrollers.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Popup;
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

    String musicFile = "sounds/notification1.mp3";     // For example
    private final Popup popup = new Popup();
    EventHandler<FriendRequestChangeEvent> newFriendRequestHandler =
            e -> {
                if (!popup.isShowing()) {
                    popup.getContent().add(new Label("New friend request"));
                    popup.show(null);
                }
                else
                    popup.hide();
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
                final TableCell<FriendRequest, Void> tableCell = new TableCell<>() {
                    private final Button button = new Button("Approve");

                    {
                        button.setOnAction(event -> {
                            FriendRequest friendRequest = getTableView().getItems().get(getIndex());
                            System.out.println("Approve for: " + friendRequest.toString());

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
                return tableCell;
            }
        });

        tableColumnReject.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendRequest, Void> call(final TableColumn<FriendRequest, Void> param) {
                final TableCell<FriendRequest, Void> tableCell = new TableCell<>() {
                    private final Button button = new Button("Reject");

                    {
                        button.setOnAction(event -> {
                            FriendRequest friendRequest = getTableView().getItems().get(getIndex());
                            System.out.println("Approve for: " + friendRequest.toString());

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
                return tableCell;
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
                    initUserModel();
                }
                break;
            default:;
        }
        if (event.getEventType() == EventType.USER &&
                !(((UserChangeEvent)event).getChangeEventType() == ChangeEventType.DELETE &&
                ((UserChangeEvent)event).getOldData().equals(user)) ||
            event.getEventType() == EventType.FRIEND_REQUEST ||
            event.getEventType() == EventType.OPENED && friendRequestsModel.isEmpty()
        ) {
            if (event.getEventType() == EventType.FRIEND_REQUEST) {

            }
            initUserModel();
        }
    }

    public void dispose() {
        buildContainer.getFriendRequestService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
