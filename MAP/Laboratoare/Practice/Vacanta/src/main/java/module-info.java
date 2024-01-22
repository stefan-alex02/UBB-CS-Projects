module org.example.vacanta {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.vacanta to javafx.fxml;
    exports org.example.vacanta;

    opens org.example.vacanta.factory to javafx.fxml;
    exports org.example.vacanta.factory;

    opens org.example.vacanta.controllers to javafx.fxml;
    exports org.example.vacanta.controllers;

    opens org.example.vacanta.domain to javafx.fxml;
    exports org.example.vacanta.domain;

    opens org.example.vacanta.utils.events to javafx.fxml;
    exports org.example.vacanta.utils.events;

    opens org.example.vacanta.persistence to javafx.fxml;
    exports org.example.vacanta.persistence;

    opens org.example.vacanta.persistence.paging to javafx.fxml;
    exports org.example.vacanta.persistence.paging;

    opens org.example.vacanta.exceptions to javafx.fxml;
    exports org.example.vacanta.exceptions;

    opens org.example.vacanta.business to javafx.fxml;
    exports org.example.vacanta.business;

    opens org.example.vacanta.ui to javafx.fxml;
    exports org.example.vacanta.ui;
}