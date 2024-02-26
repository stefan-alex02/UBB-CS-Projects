package ir.map.g221.guisocialnetwork.controllers.userperspective;

import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.domain.entities.dtos.FriendshipDetails;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PageableImplementation;
import ir.map.g221.guisocialnetwork.utils.events.ChangeEventType;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class SearchUsersController extends AbstractTabController implements Observer {
    private User user;
    private boolean isLoaded = false;
    private BuildContainer buildContainer;
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();
    public TableView<User> tableView;
    @FXML Pagination pagination;
    @FXML ComboBox<Integer> pageSizeComboBox;
    private Pageable currentPageable;
    private Page<User> currentUserPage;

    @FXML TableColumn<User, String> tableColumnFirstName;
    @FXML TableColumn<User, String> tableColumnLastName;
    @FXML TableColumn<User, Integer> tableColumnNoOfFriends;
    @FXML TableColumn<User, Void> tableColumnSendRequest;

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
                return new TableCell<>() {
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
            }
        });
        tableColumnSendRequest.setSortable(false);

        pageSizeComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 8, 10, 20));
        pageSizeComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> updatePagination(newValue));

        tableView.setItems(usersModel);
    }

    public void setContent(BuildContainer buildContainer, User user) {
        this.buildContainer = buildContainer;
        this.user = user;
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getFriendshipService().addObserver(this);
        buildContainer.getUserService().addObserver(this);

        int DEFAULT_PAGE_SIZE = 2;
        updatePagination(DEFAULT_PAGE_SIZE);
    }

    private int getPageCount(int pageSize) {
        return (int) Math.max(
                Math.ceil(
                        (double) buildContainer.getFriendshipService()
                                .getNumberOfNonFriendsOfUser(user.getId()) / pageSize
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
            currentUserPage = buildContainer.getFriendshipService()
                    .getNonFriendsOfUser(user.getId(), currentPageable);
            usersModel.setAll(currentUserPage.getContent().toList());
        }
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = getPageCount(pageSize);
        pagination.setPageCount(pageCount);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            currentUserPage = buildContainer.getFriendshipService()
                    .getNonFriendsOfUser(user.getId(), currentPageable);
            usersModel.setAll(currentUserPage.getContent().toList());
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
            default:
                ;
        }
    }

    public void dispose() {
        buildContainer.getFriendRequestService().removeObserver(this);
        buildContainer.getFriendshipService().removeObserver(this);
        buildContainer.getUserService().removeObserver(this);
    }
}
