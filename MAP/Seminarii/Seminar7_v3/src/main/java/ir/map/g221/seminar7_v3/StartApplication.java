package ir.map.g221.seminar7_v3;

import ir.map.g221.seminar7_v3.factory.BuildContainer;
import ir.map.g221.seminar7_v3.factory.Factory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends Application {
    BuildContainer container = Factory.getInstance().build();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        stage.setScene(new Scene(userLayout));

        UserController userController = userLoader.getController();
        userController.setUserService(container.getUserService());

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}