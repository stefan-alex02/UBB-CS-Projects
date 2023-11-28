package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.othercontrollers.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
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

import java.time.LocalDateTime;

public class FriendListController implements Observer {
    private User user;
    private BuildContainer buildContainer;
    private final ObservableList<FriendshipDetails> friendshipDetailsModel = FXCollections.observableArrayList();
    @FXML TableView<FriendshipDetails> tableView;

    @FXML
    TableColumn<FriendshipDetails, String> tableColumnFirstName;
    @FXML TableColumn<FriendshipDetails, String> tableColumnLastName;
    @FXML TableColumn<FriendshipDetails, LocalDateTime> tableColumnFriendsFrom;
    @FXML TableColumn<FriendshipDetails, Void> tableColumnRemoveFriend;

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
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

        tableColumnRemoveFriend.setCellFactory(new Callback<>() {
            @Override
            public TableCell<FriendshipDetails, Void> call(final TableColumn<FriendshipDetails, Void> param) {
                final TableCell<FriendshipDetails, Void> tableCell = new TableCell<>() {
                    private final Button button = new Button("Remove friend");

                    {
                        button.setOnAction(event -> {
                            User friend = getTableView().getItems().get(getIndex()).getFriend();
                            System.out.println("Deleting friendship with: " + friend.toString());

                            try {
                                buildContainer.getFriendshipService()
                                        .removeFriendship(user.getId(), friend.getId());
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
        if (event.getEventType() == EventType.USER &&
                !(((UserChangeEvent)event).getChangeEventType() == ChangeEventType.DELETE &&
                ((UserChangeEvent)event).getOldData().equals(user)) ||
            event.getEventType() == EventType.FRIENDSHIP ||
            event.getEventType() == EventType.OPENED && friendshipDetailsModel.isEmpty()) {
            initUserModel();
        }
    }

    public void dispose() {
        buildContainer.getFriendshipService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
