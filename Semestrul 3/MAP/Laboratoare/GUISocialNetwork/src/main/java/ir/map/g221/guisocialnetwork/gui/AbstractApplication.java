package ir.map.g221.guisocialnetwork.gui;

import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.factory.Factory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public abstract class AbstractApplication extends Application {
    protected final BuildContainer container = Factory.getInstance().build();

    protected void initView(Stage primaryStage) throws IOException {
        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    container.closeSQLConnection();
                    System.out.println("SQL Connection closed successfully.");
                } catch (SQLException e) {
                    System.err.println(Arrays.toString(e.getStackTrace()));
                }
            }
        });
    }
}
