package ir.map.g221.guisocialnetwork.controllers;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.guiutils.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.exceptions.SampleGeneratedException;
import ir.map.g221.guisocialnetwork.persistence.paging.Page;
import ir.map.g221.guisocialnetwork.persistence.paging.Pageable;
import ir.map.g221.guisocialnetwork.persistence.paging.PageableImplementation;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.EventType;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserController extends AbstractUserController implements Observer {
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnUsername;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML Pagination pagination;
    @FXML ComboBox<Integer> pageSizeComboBox;
    private Pageable currentPageable;
    private Page<User> currentUserPage;

    private static final List<String> colorList =
            Arrays.asList("E3B5A3", "EF8C8A", "EFC38A", "F5D075", "D5E183",
                    "B8E19F", "9FE1C8", "4FE5E8", "89AAD2", "A6A6DC",
                    "C6A6DC", "DCA6D3");

    private static String getColorCode(int hashCode) {
        int sum = 0;
        while(hashCode > 0) {
            sum += hashCode % 10;
            hashCode /= 10;
        }
        return colorList.get(sum % colorList.size());
    }

    @FXML
    public void initialize() {
        tableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>() {
                @Override
                public void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty || isSelected()) {
                        setStyle("");
                    }
                    else {
                        setStyle("-fx-background-color: #" +
                                getColorCode(
                                        buildContainer.getCommunityHandler()
                                                .getCommunityOfUser(item)
                                                .hashCode())
                        );
                    }
                }};
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    openUserPerspective(row.getItem());
                }
            });
            return row;
            }
        );
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        pageSizeComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 5, 8, 10, 20));
        pageSizeComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> updatePagination(newValue));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems(usersModel);
    }

    public void setContent(BuildContainer buildContainer) {
        this.buildContainer = buildContainer;
        buildContainer.getUserService().addObserver(this);
        buildContainer.getFriendRequestService().addObserver(this);
        buildContainer.getFriendshipService().addObserver(this);

        int DEFAULT_PAGE_SIZE = 2;
        updatePagination(DEFAULT_PAGE_SIZE);
    }

    private void refreshPage() {
        int pageCount = (int) Math.ceil(
                (double) buildContainer.getUserService().getUserCount() /
                        currentPageable.getPageSize());
        if (pagination.getPageCount() != pageCount) {
            updatePagination(currentPageable.getPageSize());
        }
        else {
            currentUserPage = buildContainer.getUserService().getAllUsers(currentPageable);
            usersModel.setAll(currentUserPage.getContent().toList());
        }
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = (int) Math.ceil((double) buildContainer.getUserService().getUserCount() / pageSize);
        pagination.setPageCount(pageCount);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            currentUserPage = buildContainer.getUserService().getAllUsers(currentPageable);
            usersModel.setAll(currentUserPage.getContent().toList());
            return tableView;
        });
    }

    @Override
    public void update(Event event) {
        if (event.getEventType() == EventType.USER ||
            event.getEventType() == EventType.FRIENDSHIP) {
            refreshPage();
        }
    }

    @FXML
    private void handleAddButton(ActionEvent actionEvent) {
        showUserEditDialog(Optional.empty());
    }

    @FXML
    private void handleDeleteButton(ActionEvent actionEvent) {
        Set<Long> selectedUsersIds = tableView.getSelectionModel().getSelectedItems().stream().map(User::getId).collect(Collectors.toSet());
        if (!selectedUsersIds.isEmpty()) {
            try {
                selectedUsersIds.forEach(id -> buildContainer.getUserService().removeUser(id));
                MessageAlerter.showMessage(null,
                        Alert.AlertType.INFORMATION,
                        "Delete info",
                        "User(s) deleted successfully");
            }
            catch(Exception e) {
                MessageAlerter.showErrorMessage(null, "Delete error", "Error occurred while deleting users: " +
                        e.getMessage());
            }
        }
        else {
            MessageAlerter.showErrorMessage(null, "Selection error", "You must select at least one user.");
        }
    }

    @FXML
    private void handleUpdateButton(ActionEvent actionEvent) {
        List<User> selectedUsers = tableView.getSelectionModel().getSelectedItems();
        if (selectedUsers.size() == 1) {
            showUserEditDialog(Optional.of(selectedUsers.get(0)));
        }
        else if (selectedUsers.isEmpty()) {
            MessageAlerter.showErrorMessage(null, "Selection error", "You must select one user.");
        }
        else {
            MessageAlerter.showErrorMessage(null, "Selection error", "Only one user can be updated at once.");
        }
    }

    @FXML
    private void handleGenerateSampleButton(ActionEvent actionEvent) {
        try {
            buildContainer.getSampleGenerator().generateSample();
            MessageAlerter.showMessage(null,
                    Alert.AlertType.INFORMATION,
                    "Generated sample info",
                    "Users were generated with success.");
        }
        catch(SampleGeneratedException e) {
            MessageAlerter.showErrorMessage(null,"Sample generating error",  e.getMessage());
        }
        catch(Exception e) {
            MessageAlerter.showErrorMessage(null, "Error while generating sample", e.getMessage());
        }
    }

    public void showUserEditDialog(Optional<User> user) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(OldMain.class.getResource("views/edituser-view.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(buildContainer.getUserService(), dialogStage, user);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
