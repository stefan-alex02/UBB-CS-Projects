package org.example.practic.controllers;

import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.practic.domain.City;
import org.example.practic.domain.RouteDTO;
import org.example.practic.domain.Ticket;
import org.example.practic.factory.BuildContainer;
import org.example.practic.utils.events.TicketEvent;
import org.example.practic.utils.observer.Observer;

import java.util.HashMap;

public class ClientController implements Observer<TicketEvent> {
    private BuildContainer container;
    private Stage stage;
    private HashMap<String, City> citites;

    private final ObservableList<RouteDTO> routesModel = FXCollections.observableArrayList();
    @FXML
    TableView<RouteDTO> tableViewRoutes;
    public TableColumn<RouteDTO, String> tableColumnEntities;

    public ComboBox<String> comboBoxDepartureCity;
    public ComboBox<String> comboBoxDestinationCity;
    public DatePicker datePicker;


    private final ObservableList<Ticket> ticketsModel = FXCollections.observableArrayList();
    public TableView<Ticket> tableViewTickets;
    public TableColumn<Ticket, String> tableColumnTickets;

    @FXML
    public void initialize() {
        tableColumnEntities.setCellValueFactory(route ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return route.getValue().toString(container);
                    }
                });
        tableViewRoutes.setItems(routesModel);


        tableColumnTickets.setCellValueFactory(route ->
                new ObservableValueBase<>() {
                    @Override
                    public String getValue() {
                        return container.getCityService().getCityById(route.getValue().getDepartureCityId()) +
                                " " + route.getValue().getTrainId();
                    }
                });
        tableViewTickets.setItems(ticketsModel);
    }

    public void setContent(BuildContainer container, Stage stage) {
        this.container = container;
        this.stage = stage;

//        container.getDummyEntityService().addObserver(this);

        citites = new HashMap<>();
        for (City city : container.getCityService().getCities()) {
            citites.put(city.getName(), city);
        }
        comboBoxDepartureCity.setItems(FXCollections.observableList(
                container.getCityService().getCityNames()
        ));
        comboBoxDestinationCity.setItems((comboBoxDepartureCity.getItems()));

        this.stage.setOnCloseRequest(e -> dispose());

        initModels();
    }

    private void dispose() {
//        container.getDummyEntityService().addObserver(this);

        stage.close();
    }

    public void initModels() {
    }

    @Override
    public void update(TicketEvent event) {
        switch(event.getDummyEntityEventType()) {
            default:
        }
    }

    @FXML
    void onSearchButtonPressed(ActionEvent actionEvent) {
        if (comboBoxDepartureCity.getValue() == null) {
            MessageAlerter.showErrorMessage(null,
                    "Error",
                    "You must select departure city");
            return;
        }

        if (comboBoxDestinationCity.getValue() == null) {
            MessageAlerter.showErrorMessage(null,
                    "Error",
                    "You must select destination city");
            return;
        }

        routesModel.setAll(container.getTrainStationService().calculateRoutes(
                citites.get(comboBoxDepartureCity.getValue()).getID(),
                citites.get(comboBoxDestinationCity.getValue()).getID()
        ));

//        container.getDummyEntityService().notifyObservers(
//                new DummyEntityEvent(null, null));
    }

    public void onBuyButtonPressed(ActionEvent actionEvent) {
        if (tableViewRoutes.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Error",
                    "You must select a route");
            return;
        }

        try {
            container.getTicketService().saveTicket(new Ticket(
                    0L,
                    tableViewRoutes.getSelectionModel().getSelectedItem().getTrainId(),
                    tableViewRoutes.getSelectionModel().getSelectedItem().getStations().get(0).getDepartureCityId(),
                    datePicker.getValue()
            ));

            ticketsModel.setAll(container.getTicketService().getTicketsOf(
                    tableViewRoutes.getSelectionModel().getSelectedItem().getStations().get(0).getDepartureCityId()
                    , tableViewRoutes.getSelectionModel().getSelectedItem().getTrainId()
            ));
        }
        catch(RuntimeException e) {
            MessageAlerter.showErrorMessage(null,
                    "Error",
                    e.getMessage());
            return;
        }
    }
}
