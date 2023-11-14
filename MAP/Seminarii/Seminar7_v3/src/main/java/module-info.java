module ir.map.g221.seminar7_v3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.sql;

    opens ir.map.g221.seminar7_v3 to javafx.fxml;
    exports ir.map.g221.seminar7_v3;
}