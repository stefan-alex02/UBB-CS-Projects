package com.example.sem9.controller;

import com.example.sem9.domain.NotaDto;
import com.example.sem9.service.ServiceManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotaController {

    ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();
    List<String> modelTema;
    private ServiceManager service;


    @FXML
    TableColumn<NotaDto, String> tableColumnName;
    @FXML
    TableColumn<NotaDto, String> tableColumnTema;
    @FXML
    TableColumn<NotaDto, Double> tableColumnNota;
    @FXML
    TableView<NotaDto> tableViewNote;
    //----------------------end TableView fx:id----------------

    @FXML
    TextField textFieldName;
    @FXML
    TextField textFieldTema;
    @FXML
    TextField textFieldNota;

//    @FXML
//    ComboBox<String> comboBoxTeme;

    @FXML
    public void initialize() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("studentName"));
        tableColumnTema.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("temaId"));
        tableColumnNota.setCellValueFactory(new PropertyValueFactory<NotaDto, Double>("nota"));

        tableViewNote.setItems(modelGrade);

        textFieldName.textProperty().addListener(o -> handleFilter());
        textFieldTema.textProperty().addListener(o -> handleFilter());
        textFieldNota.textProperty().addListener(o -> handleFilter());

//        comboBoxTeme.getSelectionModel().selectedItemProperty().addListener(
//                (x,y,z)->handleFilter()
//        );
    }

    private List<NotaDto> getNotaDTOList() {
        return service.findAllGrades()
                .stream()
                .map(n -> new NotaDto(n.getStudent().getName(), n.getTema().getId(), n.getValue(), n.getProfesor()))
                .collect(Collectors.toList());
    }

    private void handleFilter() {
        List<NotaDto> list = getNotaDTOList();

//        String tema = comboBoxTeme.getSelectionModel().getSelectedItem();

//        modelGrade.setAll(list.stream()
//                .filter(notaDTO -> notaDTO.getTemaId().equals(tema))
//                .toList());

        Predicate<NotaDto> byName = notaDTO ->
                notaDTO.getStudentName().startsWith(textFieldName.getText());

        Predicate<NotaDto> byNota = notaDTO ->
                textFieldNota.getText().isEmpty() ||
                notaDTO.getNota() > Double.parseDouble(textFieldNota.getText());

        Predicate<NotaDto> byTema = notaDTO ->
                notaDTO.getTemaId().startsWith(textFieldTema.getText());

        list = list.stream()
                .filter(byName.and(byNota).and(byTema))
                .toList();

        modelGrade.setAll(list);
    }


    public void setService(ServiceManager service) {
        this.service = service;
        modelGrade.setAll(getNotaDTOList());
        modelTema = service.findAllHomeWorks()
                .stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
//        comboBoxTeme.getItems().setAll(modelTema);
//        comboBoxTeme.getSelectionModel().selectFirst();
    }
}
