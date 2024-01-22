package org.example.practic.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.practic.domain.DummyEntity;
import org.example.practic.factory.BuildContainer;
import org.example.practic.persistence.paging.Pageable;
import org.example.practic.persistence.paging.PageableImplementation;
import org.example.practic.utils.events.DummyEntityEvent;
import org.example.practic.utils.observer.Observer;

public class DummyEntityController implements Observer<DummyEntityEvent> {
    private BuildContainer container;
    private Stage stage;
    private Pageable currentPageable;
    private DummyEntity dummyEntity;

    private final ObservableList<DummyEntity> dummyEntitiesModel = FXCollections.observableArrayList();

    @FXML
    TableView<DummyEntity> tableViewEntities;
    @FXML
    TableColumn<DummyEntity, String> tableColumnEntities;
    @FXML
    Pagination pagination;

    @FXML
    public void initialize() {
        tableColumnEntities
                .setCellValueFactory(param ->
                        new SimpleStringProperty(param.getValue().getID().toString()));
        tableViewEntities.setItems(dummyEntitiesModel);
    }

    public void setContent(BuildContainer container, DummyEntity dummyEntity, Stage stage) {
        this.container = container;
        this.dummyEntity = dummyEntity;
        this.stage = stage;

        container.getDummyEntityService().addObserver(this);

        this.stage.setOnCloseRequest(e -> dispose());

        currentPageable = new PageableImplementation(1, 2);
        initModels();
    }

    private void dispose() {
        container.getDummyEntityService().addObserver(this);

        stage.close();
    }

    public void initModels() {
        updatePagination(currentPageable.getPageSize());
    }

    private void refreshPage() {
        dummyEntitiesModel.setAll(container.getDummyEntityService().getDummies(currentPageable)
                .getContent().toList());
    }

    private void updatePagination(int pageSize) {
        // Set the page count based on the page size
        int pageCount = (int) Math.ceil((double) container.getDummyEntityService()
                .getNoOfDummies() / pageSize);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);

        // Set the page factory
        pagination.setPageFactory(pageIndex -> {
            currentPageable = new PageableImplementation(pageIndex + 1, pageSize);
            refreshPage();
            return tableViewEntities;
        });
    }

    @Override
    public void update(DummyEntityEvent event) {
        switch(event.getDummyEntityEventType()) {
            default:
        }
    }

    @FXML
    void onButtonPressed(ActionEvent actionEvent) {
        if (tableViewEntities.getSelectionModel().isEmpty()) {
            MessageAlerter.showErrorMessage(null,
                    "Eroare",
                    "Trebuie sa selectati");
            return;
        }

        container.getDummyEntityService().notifyObservers(
                new DummyEntityEvent(null, null));
    }
}
