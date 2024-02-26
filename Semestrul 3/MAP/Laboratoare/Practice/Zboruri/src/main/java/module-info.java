module org.example.zboruri {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.zboruri to javafx.fxml;
    exports org.example.zboruri;

    opens org.example.zboruri.factory to javafx.fxml;
    exports org.example.zboruri.factory;

    opens org.example.zboruri.controllers to javafx.fxml;
    exports org.example.zboruri.controllers;

    opens org.example.zboruri.domain to javafx.fxml;
    exports org.example.zboruri.domain;

    opens org.example.zboruri.utils.events to javafx.fxml;
    exports org.example.zboruri.utils.events;

    opens org.example.zboruri.utils.observer to javafx.fxml;
    exports org.example.zboruri.utils.observer;

    opens org.example.zboruri.persistence to javafx.fxml;
    exports org.example.zboruri.persistence;

    opens org.example.zboruri.persistence.paging to javafx.fxml;
    exports org.example.zboruri.persistence.paging;

    opens org.example.zboruri.exceptions to javafx.fxml;
    exports org.example.zboruri.exceptions;

    opens org.example.zboruri.business to javafx.fxml;
    exports org.example.zboruri.business;

    opens org.example.zboruri.persistence.pagingrepos to javafx.fxml;
    exports org.example.zboruri.persistence.pagingrepos;
}