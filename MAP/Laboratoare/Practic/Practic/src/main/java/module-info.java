module org.example.practic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.practic to javafx.fxml;
    exports org.example.practic;

    opens org.example.practic.factory to javafx.fxml;
    exports org.example.practic.factory;

    opens org.example.practic.controllers to javafx.fxml;
    exports org.example.practic.controllers;

    opens org.example.practic.domain to javafx.fxml;
    exports org.example.practic.domain;

    opens org.example.practic.utils to javafx.fxml;
    exports org.example.practic.utils;

    opens org.example.practic.utils.events to javafx.fxml;
    exports org.example.practic.utils.events;

    opens org.example.practic.utils.observer to javafx.fxml;
    exports org.example.practic.utils.observer;

    opens org.example.practic.persistence to javafx.fxml;
    exports org.example.practic.persistence;

    opens org.example.practic.exceptions to javafx.fxml;
    exports org.example.practic.exceptions;

    opens org.example.practic.business to javafx.fxml;
    exports org.example.practic.business;

    opens org.example.practic.validation to javafx.fxml;
    exports org.example.practic.validation;
}