package ir.map.g221.guisocialnetwork.gui;

import ir.map.g221.guisocialnetwork.OldMain;
import ir.map.g221.guisocialnetwork.controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends AbstractApplication {
    @Override
    public void start(Stage primaryStage) throws Exception {
        initView(primaryStage);
        primaryStage.setHeight(300);
        primaryStage.setWidth(500);
        primaryStage.show();
    }

    protected void initView(Stage primaryStage) throws IOException {
        super.initView(primaryStage);

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(OldMain.class.getResource("views/login-view.fxml"));
        AnchorPane userLayout = loginLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LoginController loginController = loginLoader.getController();
        loginController.setContent(container, primaryStage);
    }
}
