module ir.map.g221.guisocialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.sql;

    opens ir.map.g221.guisocialnetwork to javafx.fxml;
    exports ir.map.g221.guisocialnetwork;
}