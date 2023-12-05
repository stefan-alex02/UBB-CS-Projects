package ir.map.g221.guisocialnetwork.gui;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.UserController;
import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.factory.Factory;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class StartApplication extends Application {
    private static final BuildContainer container = Factory.getInstance().build();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initView(primaryStage);
        primaryStage.setHeight(600);
        primaryStage.setWidth(900);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(OldMain.class.getResource("views/user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = userLoader.getController();
        userController.setBuildContainer(container);

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
