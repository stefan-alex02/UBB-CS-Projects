package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
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

public class FriendListController extends AbstractTabController implements Observer {
    private User user;
    private BuildContainer buildContainer;
    private boolean isLoaded = false;
    private final ObservableList<FriendshipDetails> friendshipDetailsModel = FXCollections.observableArrayList();
    @FXML TableView<FriendshipDetails> tableView;
    @FXML Pagination pagination;

    @FXML ComboBox<Integer> pageSizeComboBox;
    private Pageable currentPageable;
    private Page<FriendshipDetails> currentuserPage;

    @FXML
    TableColumn<FriendshipDetails, String> tableColumnFirstName;
    @FXML TableColumn<FriendshipDetails, String> tableColumnLastName;
    @FXML TableColumn<FriendshipDetails, LocalDateTime> tableColumnFriendsFrom;
    @FXML TableColumn<FriendshipDetails, Void> tableColumnMessageFriend;
    @FXML TableColumn<FriendshipDetails, Void> tableColumnRemoveFriend;

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

        pageSizeComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 8, 10, 20));
        pageSizeComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> updatePagination(newValue));

        tableView.setItems(friendshipDetailsModel);
    }

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendshipService().addObserver(this);
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getUserService().addObserver(this);

        int DEFAULT_PAGE_SIZE = 2;
        updatePagination(DEFAULT_PAGE_SIZE);
    }

    private int getPageCount(int pageSize) {
        return (int) Math.max(
                Math.ceil(
                        (double) buildContainer.getFriendshipService()
                                .getNumberOfFriendsOfUser(user.getId()) / pageSize
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
            currentuserPage = buildContainer.getFriendshipService()
                    .getFriendshipDetailsOfUser(user.getId(), currentPageable);
            friendshipDetailsModel.setAll(currentuserPage.getContent().toList());
        }
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = getPageCount(pageSize);
        pagination.setPageCount(pageCount);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            currentuserPage = buildContainer.getFriendshipService()
                    .getFriendshipDetailsOfUser(user.getId(), currentPageable);
            friendshipDetailsModel.setAll(currentuserPage.getContent().toList());
            return tableView;
        });
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER:
                UserChangeEvent userChangeEvent = (UserChangeEvent) event;
                if (userChangeEvent.getChangeEventType() == ChangeEventType.DELETE &&
                        !userChangeEvent.getOldData().equals(user)) {
                        initModels();
                }
                break;
            case FRIENDSHIP:
                initModels();
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
        buildContainer.getFriendshipService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
