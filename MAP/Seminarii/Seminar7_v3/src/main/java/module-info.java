module ir.map.g221.seminar7_v3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.jetbrains.annotations;
    requires java.sql;

    opens ir.map.g221.seminar7_v3 to javafx.fxml;

    // OPEN THE FOLDER CONTAINING OBJECT CLASSES TO 'javafx.base'
    // WHEN NOT LOADING TO GUI !!!!!!
    // --------------------------------------------------------------
    opens ir.map.g221.seminar7_v3.domain.entities to javafx.base;
    // --------------------------------------------------------------

    exports ir.map.g221.seminar7_v3;
}