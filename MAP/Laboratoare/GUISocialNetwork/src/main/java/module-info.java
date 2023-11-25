module ir.map.g221.guisocialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.sql;

    opens ir.map.g221.guisocialnetwork.business to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.business;

    opens ir.map.g221.guisocialnetwork.utils.events to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.utils.events;

    opens ir.map.g221.guisocialnetwork.controllers to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.controllers;

    opens ir.map.g221.guisocialnetwork.utils.observer to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.utils.observer;

    opens ir.map.g221.guisocialnetwork.utils.generictypes to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.utils.generictypes;

    opens ir.map.g221.guisocialnetwork.domain to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.domain;

    opens ir.map.g221.guisocialnetwork.domain.entities to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.domain.entities;

    opens ir.map.g221.guisocialnetwork.exceptions to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.exceptions;

    opens ir.map.g221.guisocialnetwork.domain.entities.dtos to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.domain.entities.dtos;

    opens ir.map.g221.guisocialnetwork.utils.graphs to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.utils.graphs;

    opens ir.map.g221.guisocialnetwork.exceptions.functionexceptions to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.exceptions.functionexceptions;

    opens ir.map.g221.guisocialnetwork.exceptions.graphs to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.exceptions.graphs;

    opens ir.map.g221.guisocialnetwork.persistence to javafx.fxml;
    exports ir.map.g221.guisocialnetwork.persistence;

    opens ir.map.g221.guisocialnetwork to javafx.fxml;
    exports ir.map.g221.guisocialnetwork;
    exports ir.map.g221.guisocialnetwork.gui;
    opens ir.map.g221.guisocialnetwork.gui to javafx.fxml;
}