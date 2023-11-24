package ir.map.g221.guisocialnetwork;

import ir.map.g221.guisocialnetwork.business.CommunityHandler;
import ir.map.g221.guisocialnetwork.business.UserService;
import ir.map.g221.guisocialnetwork.controllers.EditUserController;
import ir.map.g221.guisocialnetwork.controllers.MessageAlerter;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.business.SampleGenerator;
import ir.map.g221.guisocialnetwork.exceptions.SampleGeneratedException;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.EventType;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserController implements Observer {
    private UserService userService = null;
    private CommunityHandler communityHandler = null;
    private SampleGenerator sampleGenerator = null;
    private final ObservableList<User> usersModel = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;

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
        tableView.setRowFactory(tv -> new TableRow<>() {
                    @Override
                    public void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty || isSelected()) {
                        setStyle("");
                    }
                    else {
                        setStyle("-fx-background-color: #" +
                                getColorCode(communityHandler.getCommunityOfUser(item).hashCode())
                        );
                    }
                }
            }
        );
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setItems(usersModel);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        userService.addObserver(this);
        initUserModel();
    }

    public void setCommunityService(CommunityHandler communityHandler) {
        this.communityHandler = communityHandler;
    }

    public void setSampleGenerator(SampleGenerator sampleGenerator) {
        this.sampleGenerator = sampleGenerator;
    }

    @Override
    public void update(Event event) {
        if (event.getEventType() == EventType.USER) {
            initUserModel();
        }
    }

    private void initUserModel() {
        usersModel.setAll(userService.getAllUsers());
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
                selectedUsersIds.forEach(id -> userService.removeUser(id));
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
            sampleGenerator.generateSample();
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
            loader.setLocation(getClass().getResource("views/edituser-view.fxml"));

            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(userService, dialogStage, user);

            dialogStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
