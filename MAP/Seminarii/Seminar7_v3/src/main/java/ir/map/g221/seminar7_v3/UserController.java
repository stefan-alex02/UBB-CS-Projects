package ir.map.g221.seminar7_v3;

import ir.map.g221.seminar7_v3.business.UserService;
import ir.map.g221.seminar7_v3.domain.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;

public class UserController {
    UserService service;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableColumn<User,Long> tableColumnUserID;
    @FXML
    TableColumn<User,String> tableColumnFirstName;


    public void setUserService(UserService userService) {
        service = userService;
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnUserID.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Collection<User> users = service.getAll();
        model.setAll(users);
    }
}