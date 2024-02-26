package org.example.modelpractic.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.domain.PlacedOrder;
import org.example.modelpractic.factory.BuildContainer;
import org.example.modelpractic.utils.events.OrderEvent;
import org.example.modelpractic.utils.events.OrderEventType;
import org.example.modelpractic.utils.observer.Observer;

public class PersonController implements Observer<OrderEvent> {
    private BuildContainer container;
    private Person person;
    private Stage stage;

    private final ObservableList<PlacedOrder> ordersModel = FXCollections.observableArrayList();

    @FXML
    TableView<PlacedOrder> tableViewOrders;
    @FXML
    TableColumn<PlacedOrder, String> tableColumnIndicativMasina;
    @FXML
    TableColumn<PlacedOrder, Integer> tableColumnTimpAsteptare;

    @FXML
    TextField textFieldLocatie;

    @FXML
    public void initialize() {
        tableColumnIndicativMasina
                .setCellValueFactory(param ->
                        new SimpleStringProperty(param.getValue().getDriver().getIndicativMasina()));
        tableColumnTimpAsteptare.setCellValueFactory(new PropertyValueFactory<>("timpAsteptare"));
        tableViewOrders.setItems(ordersModel);
    }

    public void setContent(BuildContainer container, Person person, Stage stage) {
        this.container = container;
        this.person = person;
        this.stage = stage;

        container.getOrderService().addObserver(this);

        this.stage.setOnCloseRequest(e -> dispose());

    }

    public void initModels() {
    }

    private void dispose() {
        container.getOrderService().removeObserver(this);

        stage.close();
    }

    @Override
    public void update(OrderEvent event) {
        switch(event.getOrderEventType()) {
            case HONOURED:
                if (event.getData().getPerson().equals(person)) {
                    ordersModel.add(event.getData());
                }
                break;
            default:
        }
    }

    @FXML
    private void onButtonCautaPressed(ActionEvent actionEvent) {
        if (textFieldLocatie.getText().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare cautare",
                    "Locatia nu poate fi goala");
            return;
        }

        container.getOrderService().notifyObservers(
                new OrderEvent(OrderEventType.PLACED,
                        new PlacedOrder(person, null, textFieldLocatie.getText(),null)));
    }

    @FXML
    private void onAcceptButtonPressed(ActionEvent actionEvent) {
        if (tableViewOrders.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare acceptare",
                    "Trebuie sa selectati o comanda");
            return;
        }

        container.getOrderService().addOrder(tableViewOrders.getSelectionModel().getSelectedItem());

        container.getOrderService().notifyObservers(
                new OrderEvent(OrderEventType.ACCEPTED,
                        tableViewOrders.getSelectionModel().getSelectedItem()));
        ordersModel.remove(tableViewOrders.getSelectionModel().getSelectedItem());
    }

    public void onCancelButtonPressed(ActionEvent actionEvent) {
        if (tableViewOrders.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare anulare",
                    "Trebuie sa selectati o comanda");
            return;
        }

        container.getOrderService().notifyObservers(
                new OrderEvent(OrderEventType.CANCELLED,
                        tableViewOrders.getSelectionModel().getSelectedItem()));

        ordersModel.remove(tableViewOrders.getSelectionModel().getSelectedItem());
    }
}
