package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.controllers.guiutils.Image;
import ir.map.g221.guisocialnetwork.controllers.guiutils.SoundFile;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequestStatus;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PageableImplementation;
import ir.map.g221.guisocialnetwork.utils.events.*;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.time.LocalDateTime;

public class FriendRequestController extends AbstractTabController implements Observer {
    private User user = null;
    private boolean isLoaded = false;
    private BuildContainer buildContainer = null;
    private final ObservableList<FriendRequest> friendRequestsModel = FXCollections.observableArrayList();
    public TableView<FriendRequest> tableView;
    @FXML
    Pagination pagination;
    @FXML ComboBox<Integer> pageSizeComboBox;
    private Pageable currentPageable;
    private Page<FriendRequest> currentFriendRequestPage;

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
                            MessageAlerter.playSound(SoundFile.RING_SOUND_1);

                        if (!isSelected()) {
                            MessageAlerter.displayNotification("Friend request",
                                    "New friend request from " +
                                            e.getNewData().getFrom().getFirstName() + " " +
                                            e.getNewData().getFrom().getLastName(),
                                    Image.USERS_1);
                        }
                    }
                    break;
                case UPDATE :
                    if (e.getNewData().getFrom().equals(user) &&
                        e.getNewData().getStatus() == FriendRequestStatus.APPROVED) {
                        MessageAlerter.playSound(SoundFile.RING_SOUND_1);

                        MessageAlerter.displayNotification("Friend request",
                                e.getNewData().getTo().getFirstName() + " " +
                                        e.getNewData().getTo().getLastName() +
                                        " accepted your friend request.",
                                Image.USERS_1);
                    }
                    break;
                default:;
            }
    };

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

        pageSizeComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 8, 10, 20));
        pageSizeComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> updatePagination(newValue));

        tableView.setItems(friendRequestsModel);
    }

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getUserService().addObserver(this);

        int DEFAULT_PAGE_SIZE = 2;
        updatePagination(DEFAULT_PAGE_SIZE);
    }

    private int getPageCount(int pageSize) {
        return (int) Math.max(
                Math.ceil(
                        (double) buildContainer.getFriendRequestService()
                                .getNumberOfPendingFriendRequests(user.getId()) / pageSize
                ), 1);
    }

    private void initModels() {
        refreshPage();
        user = buildContainer.getUserService().getUser(user.getId());
    }

    private void refreshPage() {
        int pageCount = getPageCount(currentPageable.getPageSize());
        if (pagination.getPageCount() != pageCount) {
            updatePagination(currentPageable.getPageSize());
        }
        else {
            currentFriendRequestPage = buildContainer.getFriendRequestService()
                    .getPendingFriendRequests(user.getId(), currentPageable);
            friendRequestsModel.setAll(currentFriendRequestPage.getContent().toList());
        }
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = getPageCount(pageSize);
        pagination.setPageCount(pageCount);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            currentFriendRequestPage = buildContainer.getFriendRequestService()
                    .getPendingFriendRequests(user.getId(), currentPageable);
            friendRequestsModel.setAll(currentFriendRequestPage.getContent().toList());
            return tableView;
        });
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() != ChangeEventType.DELETE ||
                        !userChangeEvent.getOldData().equals(user)) {
                    initModels();
                }
                break;
            case FRIEND_REQUEST:
                if (event.getEventType() == EventType.FRIEND_REQUEST) {
                    FriendRequestChangeEvent friendRequestChangeEvent = (FriendRequestChangeEvent) event;
                    initModels();
                    friendRequestNotificationHandler.handle(friendRequestChangeEvent);
                }
                break;
            case OPENED:
                if (!isLoaded) {
                    isLoaded = true;
                    initModels();
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
