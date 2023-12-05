package ir.map.g221.guisocialnetwork.gui;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartApplication extends AbstractApplication {
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

    protected void initView(Stage primaryStage) throws IOException {
        super.initView(primaryStage);

        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(OldMain.class.getResource("views/user-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = userLoader.getController();
        userController.setContent(container);
    }
}
