module org.example.modelpractic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.modelpractic to javafx.fxml;
    exports org.example.modelpractic;

    opens org.example.modelpractic.factory to javafx.fxml;
    exports org.example.modelpractic.factory;

    opens org.example.modelpractic.controllers to javafx.fxml;
    exports org.example.modelpractic.controllers;

    opens org.example.modelpractic.domain to javafx.fxml;
    exports org.example.modelpractic.domain;

    opens org.example.modelpractic.utils.events to javafx.fxml;
    exports org.example.modelpractic.utils.events;

    opens org.example.modelpractic.persistence to javafx.fxml;
    exports org.example.modelpractic.persistence;

    opens org.example.modelpractic.persistence.paging to javafx.fxml;
    exports org.example.modelpractic.persistence.paging;

    opens org.example.modelpractic.exceptions to javafx.fxml;
    exports org.example.modelpractic.exceptions;

    opens org.example.modelpractic.business to javafx.fxml;
    exports org.example.modelpractic.business;
}