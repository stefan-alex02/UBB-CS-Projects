package org.example.practic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.practic.factory.BuildContainer;
import org.example.practic.factory.Factory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class StartApplication extends Application {
    protected final BuildContainer container = Factory.getInstance().build();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(event -> {
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

        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Authenticate");
        stage.setScene(scene);
        stage.show();

        StartController startController = fxmlLoader.getController();
        startController.setContent(container);
    }

    public static void main(String[] args) {
        launch();
    }
}