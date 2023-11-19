package ir.map.g221.guisocialnetwork;

import ir.map.g221.guisocialnetwork.factory.BuildContainer;
import ir.map.g221.guisocialnetwork.factory.Factory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        userLoader.setLocation(getClass().getResource("views/user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = userLoader.getController();
        userController.setUserService(container.getUserService());
        userController.setSampleGenerator(container.getSampleGenerator());
        userController.setCommunityService(container.getCommunityService());
    }
}
