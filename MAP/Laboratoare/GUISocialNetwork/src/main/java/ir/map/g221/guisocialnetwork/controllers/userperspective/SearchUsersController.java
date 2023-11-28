package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.othercontrollers.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.EventType;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
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

public class SearchUsersController implements Observer {
    private User user;
    private BuildContainer buildContainer;
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();
    public TableView<User> tableView;

    @FXML TableColumn<User, String> tableColumnFirstName;
    @FXML TableColumn<User, String> tableColumnLastName;
    @FXML TableColumn<User, Integer> tableColumnNoOfFriends;
    @FXML TableColumn<User, Void> tableColumnSendRequest;

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getFriendshipService().addObserver(this);
        buildContainer.getUserService().addObserver(this);

//        initUserModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return userCell.getValue().getFirstName();
                    }
                });
        tableColumnLastName.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return userCell.getValue().getLastName();
                    }
                });
        tableColumnNoOfFriends.setCellValueFactory(userCell ->
                new ObservableValueBase<>() {
                    @Override
                    public Integer getValue() {
                        return buildContainer.getCommunityHandler()
                                .getNoOfFriendOfUser(userCell.getValue());
                    }
                });

        tableColumnSendRequest.setCellFactory(new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> tableCell = new TableCell<>() {
                    private final Button button = new Button("Add friend");

                    {
                        button.setOnAction(event -> {
                            User userTo = getTableView().getItems().get(getIndex());
                            System.out.println("Sending request to: " + userTo.toString());

                            try {
                                buildContainer.getFriendRequestService()
                                        .sendFriendRequestNow(user.getId(), userTo.getId());
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
                            button.getStyleClass().add("blue-button"); // Apply a style from CSS
                        }
                    }
                };
                return tableCell;
            }
        });

        tableColumnSendRequest.setSortable(false);
        tableView.setItems(usersModel);
    }

    private void initUserModel() {
        usersModel.setAll(buildContainer.getFriendshipService().getNonFriendsOfUser(user.getId()));
        user = buildContainer.getUserService().getUser(user.getId());
    }

    @Override
    public void update(Event event) {
        if (event.getEventType() == EventType.USER &&
                !(((UserChangeEvent)event).getChangeEventType() == ChangeEventType.DELETE &&
                ((UserChangeEvent)event).getOldData().equals(user)) ||
            event.getEventType() == EventType.FRIENDSHIP ||
            event.getEventType() == EventType.OPENED && usersModel.isEmpty()) {
            initUserModel();
        }
    }

    public void dispose() {
        buildContainer.getFriendRequestService().removeObserver(this);
        buildContainer.getFriendshipService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
