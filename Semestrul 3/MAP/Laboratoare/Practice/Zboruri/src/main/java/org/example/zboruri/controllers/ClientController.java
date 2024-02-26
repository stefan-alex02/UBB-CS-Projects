package org.example.zboruri.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.zboruri.domain.Client;
import org.example.zboruri.domain.FlightDTO;
import org.example.zboruri.factory.BuildContainer;
import org.example.zboruri.persistence.paging.Pageable;
import org.example.zboruri.persistence.paging.PageableImplementation;
import org.example.zboruri.utils.events.TicketEvent;
import org.example.zboruri.utils.observer.Observer;

import java.time.LocalDateTime;

public class ClientController implements Observer<TicketEvent> {
    public Pagination pagination;
    public ComboBox<String> comboBoxFrom;
    public ComboBox<String> comboBoxTo;
    public DatePicker datePicker;
    public Label labelTotalPagini;

    private BuildContainer container;
    private Client client;
    private Stage stage;

    private final ObservableList<FlightDTO> flightsModel = FXCollections.observableArrayList();
    @FXML
    TableView<FlightDTO> tableViewFlights;
    @FXML
    TableColumn<FlightDTO, String> tableColumnFrom;
    @FXML
    TableColumn<FlightDTO, String> tableColumnTo;
    @FXML
    TableColumn<FlightDTO, LocalDateTime> tableColumnDepartureTime;
    @FXML
    TableColumn<FlightDTO, LocalDateTime> tableColumnLandingTime;
    @FXML
    TableColumn<FlightDTO, Integer> tableColumnSeats;
    @FXML
    TableColumn<FlightDTO, Integer> tableColumnRemainingSeats;

    private Pageable currentPageable;

    @FXML
    public void initialize() {
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableColumnDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        tableColumnLandingTime.setCellValueFactory(new PropertyValueFactory<>("landingTime"));
        tableColumnSeats.setCellValueFactory(new PropertyValueFactory<>("seats"));
        tableColumnRemainingSeats.setCellValueFactory(new PropertyValueFactory<>("remainingSeats"));
        tableViewFlights.setItems(flightsModel);

        comboBoxFrom.setItems(FXCollections.observableArrayList(
                "Sibiu",
                "Cluj",
                "Paris",
                "Bucuresti",
                "Timisoara",
                "Berlin",
                "Londra"));
        comboBoxTo.setItems(comboBoxFrom.getItems());

        comboBoxFrom.valueProperty().addListener(
                (observale, oldvalue, newValue) -> updatePagination(currentPageable.getPageSize())
        );
        comboBoxTo.valueProperty().addListener(
                (observale, oldvalue, newValue) -> updatePagination(currentPageable.getPageSize())
        );
        datePicker.valueProperty().addListener(
                (observale, oldvalue, newValue) -> updatePagination(currentPageable.getPageSize())
        );
    }

    public void setContent(BuildContainer container, Client client, Stage stage) {
        this.container = container;
        this.client = client;
        this.stage = stage;

        container.getTicketService().addObserver(this);

        this.stage.setOnCloseRequest(e -> dispose());
        currentPageable = new PageableImplementation(1, 1);
        initModels();
    }

    public void initModels() {
        updatePagination(currentPageable.getPageSize());
    }

    private void refreshPage() {
        if (comboBoxFrom.getValue() != null && comboBoxTo.getValue() != null &&
                datePicker.getValue() != null) {
            flightsModel.setAll(container.getFlightService()
                    .getFlightsOf(currentPageable,
                            datePicker.getValue(),
                            comboBoxFrom.getValue(),
                            comboBoxTo.getValue())
                    .getContent().toList());
        }
        else {
            flightsModel.clear();
        }
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount;
        if (!(comboBoxFrom.getValue() != null && comboBoxTo.getValue() != null &&
                datePicker.getValue() != null)) {
            pageCount = 0;
        }
        else {
            pageCount = (int) Math.ceil((double) container.getFlightService()
                    .getNoOfFlightsOf(
                            datePicker.getValue(),
                            comboBoxFrom.getValue(),
                            comboBoxTo.getValue()) / pageSize);
        }
        labelTotalPagini.setText(String.valueOf(pageCount));

        pagination.setPageCount(Math.max(1, pageCount));
        pagination.setCurrentPageIndex(0);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            refreshPage();
            return tableViewFlights;
        });
    }

    private void dispose() {
        container.getTicketService().removeObserver(this);

        stage.close();
    }

    @Override
    public void update(TicketEvent event) {
        switch(event.getTicketEventType()) {
            case PURCHASED:
                if (flightsModel.stream()
                        .anyMatch(f -> f.getID().equals(
                                event.getData()
                                        .getID()
                                        .getValue())
                        )
                ) {
                    refreshPage();
                }
                break;
            default:
        }
    }

    public void onPurchaseButtonPressed(ActionEvent actionEvent) {
        if (tableViewFlights.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare achizitie",
                    "Trebuie sa selectati un zbor");
            return;
        }

        if (tableViewFlights.getSelectionModel().getSelectedItem().getRemainingSeats() <= 0) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare achizitie",
                    "Zborul selectat nu mai are locuri disponibile");
            return;
        }

        try {
            container.getTicketService().addTicket(
                    client.getUsername(),
                    tableViewFlights.getSelectionModel().getSelectedItem().getID()
            );
        }
        catch(RuntimeException e) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare achizitie",
                    "Nu s-a putut achizitiona biletul");
        }
    }
}
