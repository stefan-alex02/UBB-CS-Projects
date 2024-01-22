package org.example.practic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.practic.controllers.DummyEntityController;
import org.example.practic.controllers.MessageAlerter;
import org.example.practic.domain.DummyEntity;
import org.example.practic.exceptions.NonexistentUsernameException;
import org.example.practic.factory.BuildContainer;
import org.example.practic.factory.Factory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class HeadlessConsoleApplication extends Application {
    private static BuildContainer container = Factory.getInstance().build();
    private static String[] loginIds;

    @Override
    public void start(Stage primaryStage) {
        login(loginIds);
    }

    @Override
    public void stop() throws Exception {
        try {
            container.closeSQLConnection();
            System.out.println("SQL Connection closed successfully.");
        } catch (SQLException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void main(String[] args) {
        // Check if an ID is provided as a command-line argument
        if (args.length > 0) {
            loginIds = args;

            // Launch the JavaFX application
            launch(args);
        } else {
            System.out.println("Please provide a login ID as a command-line argument.");
            Platform.exit();
        }
    }

    private static void login(String[] loginIds) {
        Arrays.stream(loginIds).forEach(loginId -> {
            try {
                DummyEntity dummyEntity = container.getDummyEntityService()
                        .getEntityById(Long.valueOf(loginId));

                FXMLLoader loader = new FXMLLoader();
                Stage stage = new Stage();

                loader.setLocation(HeadlessConsoleApplication.class.getResource("views/entity-view.fxml"));

                AnchorPane root = loader.load();

                // Create the dialog Stage.
                stage.setTitle("Window");
                stage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(root);
                stage.setScene(scene);

                DummyEntityController controller = loader.getController();
                controller.setContent(container, dummyEntity, stage);

                stage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NumberFormatException | NonexistentUsernameException e) {
                MessageAlerter.showErrorMessage(null, "Login error", e.getMessage());
            }
        });
    }
}
