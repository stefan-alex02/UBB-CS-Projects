package org.example.vacanta.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.modelpractic.domain.Driver;
import org.example.modelpractic.domain.Order;
import org.example.modelpractic.domain.Person;
import org.example.modelpractic.domain.PlacedOrder;
import org.example.modelpractic.factory.BuildContainer;
import org.example.modelpractic.persistence.paging.Pageable;
import org.example.modelpractic.persistence.paging.PageableImplementation;
import org.example.modelpractic.utils.events.OrderEvent;
import org.example.modelpractic.utils.events.OrderEventType;
import org.example.modelpractic.utils.observer.Observer;

import java.util.Objects;
import java.util.Optional;

public class DriverController implements Observer<OrderEvent> {
    private BuildContainer container;
    private Driver driver;
    private Stage stage;

    private final ObservableList<PlacedOrder> ordersModel = FXCollections.observableArrayList();

    @FXML
    TableView<PlacedOrder> tableViewOrders;
    @FXML
    TableColumn<PlacedOrder, String> tableColumnOrdersClientNume;
    @FXML
    TableColumn<PlacedOrder, Integer> tableColumnOrdersLocation;

    @FXML
    TextField textFieldTimpAsteptare;

    private final ObservableList<Person> clientsModel = FXCollections.observableArrayList();
    private Pageable currentPageable;
    @FXML
    ListView<Person> listViewClients;
    @FXML Pagination paginationClients;
    @FXML Spinner<Integer> spinnerPage;

    public Label labelMedie;
    public Label labelClientFidel;

    private final ObservableList<Order> ordersDateModel = FXCollections.observableArrayList();

    @FXML
    TableView<Order> tableViewOrdersDate;
    @FXML
    TableColumn<Order, String> tableColumnOrdersDateClientNume;
    @FXML
    TableColumn<Order, Integer> tableColumnOrdersDateDate;

    @FXML DatePicker datePicker;

    @FXML
    public void initialize() {
        tableColumnOrdersClientNume
                .setCellValueFactory(param ->
                        new SimpleStringProperty(param.getValue().getPerson().getName()));
        tableColumnOrdersLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tableViewOrders.setItems(ordersModel);

        spinnerPage.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 100,  // Minimum and maximum values
                1        // Initial value
        ));
        spinnerPage.valueProperty().addListener(
                (observable, oldValue, newValue) -> updatePagination(newValue));
        listViewClients.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Person> call(ListView<Person> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Person person, boolean empty) {
                        super.updateItem(person, empty);
                        if (person != null && !empty) {
                            setText(person.getName());
                            setAlignment(Pos.BASELINE_CENTER);
                        }
                        else {
                            setText("");
                        }
                    }
                };
            }
        });
        listViewClients.setItems(clientsModel);

        datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> initOrdersDate());
        tableColumnOrdersDateClientNume
                .setCellValueFactory(param ->
                        new SimpleStringProperty(param.getValue().getPerson().getName()));
        tableColumnOrdersDateDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableViewOrdersDate.setItems(ordersDateModel);
    }

    public void setContent(BuildContainer container, Driver driver, Stage stage) {
        this.container = container;
        this.driver = driver;
        this.stage = stage;

        container.getOrderService().addObserver(this);

        this.stage.setOnCloseRequest(e -> dispose());

        currentPageable = new PageableImplementation(1, 1);
        initModels();
    }

    public void initModels() {
        updatePagination(currentPageable.getPageSize());
        initOrdersDate();

        labelMedie.setText(String.valueOf(container.getOrderService().last3MonthsOrderAverage(driver.getID())));

        Optional<Person> clientFidel = container.getOrderService().celMaiFidelClient(driver.getID());
        if (clientFidel.isPresent()) {
            labelClientFidel.setText(clientFidel.get().getName());
        }
        else {
            labelClientFidel.setText("[None]");
        }
    }

    public void initOrdersDate() {
        if (datePicker.getValue() == null) {
            ordersDateModel.clear();
        }
        else {
            ordersDateModel.setAll(container.getOrderService().getOrdersOf(datePicker.getValue(), driver.getID()));
        }
    }

    private void refreshPage() {
        clientsModel.setAll(container.getDriverService().getClientsOf(currentPageable, driver.getID())
                .getContent().toList());
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = (int) Math.ceil((double) container.getDriverService()
                .getNumberOfClientsOf(driver.getID()) / pageSize);
        paginationClients.setPageCount(pageCount);
        paginationClients.setCurrentPageIndex(0);

        // Set the page factory
        paginationClients.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            refreshPage();
            return listViewClients;
        });
    }

    private void dispose() {
        container.getOrderService().removeObserver(this);

        stage.close();
    }

    @Override
    public void update(OrderEvent event) {
        switch(event.getOrderEventType()) {
            case PLACED:
                ordersModel.add(event.getData());
                break;
            case ACCEPTED:
                ordersModel.removeIf(ord -> ord.getPerson().equals(event.getData().getPerson()) &&
                        Objects.equals(ord.getTimpAsteptare(), event.getData().getTimpAsteptare()));
                initModels();
                break;
            default:
        }
    }

    @FXML
    void onOnoareazaButtonPressed(ActionEvent actionEvent) {
        if (textFieldTimpAsteptare.getText().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare onorare",
                    "Trebuie sa dati un timp de asteptare");
            return;
        }

        int time;
        try{
            time = Integer.parseInt(textFieldTimpAsteptare.getText());
        }
        catch (NumberFormatException ex){
            MessageAlerter.showErrorMessage(null,
                    "Eroare onorare",
                    "Trebuie sa dati un numar pentru timpul de asteptare");
            return;
        }

        if (tableViewOrders.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare onorare",
                    "Trebuie sa selectati o comanda");
            return;
        }

        PlacedOrder selectedOrder = tableViewOrders.getSelectionModel().getSelectedItem();
        selectedOrder.setDriver(driver);
        selectedOrder.setTimpAsteptare(time);

        container.getOrderService().notifyObservers(
                new OrderEvent(OrderEventType.HONOURED,
                        tableViewOrders.getSelectionModel().getSelectedItem()));

        ordersModel.remove(tableViewOrders.getSelectionModel().getSelectedItem());
    }
}
