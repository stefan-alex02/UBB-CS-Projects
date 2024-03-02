module com.example.curs7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.curs7 to javafx.fxml;
    exports com.example.curs7;
}