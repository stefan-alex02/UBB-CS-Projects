package ro.mpp2024.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2024.client.controller.GameController;
import ro.mpp2024.client.controller.LoginController;
import ro.mpp2024.networking.objectprotocol.GameServicesObjectProxy;
import ro.mpp2024.services.GameServices;

import java.io.IOException;
import java.util.Properties;

public class LoginApplication extends Application {
    private Stage primaryStage;

    private static final int defaultChatPort = 55555;
    private static final String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        GameServices server = createGameServices();

        FXMLLoader loginLoader = new FXMLLoader(
                getClass().getResource("views/login-view.fxml"));
        Parent loginRoot = loginLoader.load();

        LoginController loginController =
                loginLoader.<LoginController>getController();
        loginController.setServer(server);


        FXMLLoader tripLoader = new FXMLLoader(
                getClass().getResource("views/game-view.fxml"));
        Parent tripRoot = tripLoader.load();

        GameController gameController =
                tripLoader.<GameController>getController();
        gameController.setServer(server);

        loginController.setGameController(gameController);
        loginController.setParent(tripRoot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(loginRoot, 500, 300));
        primaryStage.show();
    }

    private GameServices createGameServices() {
        Properties clientProps = new Properties();
        try {
            clientProps.load(LoginApplication.class.getResourceAsStream("/gameclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find gameclient.properties " + e);
            return null;
        }

        try {
            String serverIP = clientProps.getProperty("game.server.host", defaultServer);
            int serverPort = Integer.parseInt(clientProps.getProperty("game.server.port"));

            System.out.println("Using server IP " + serverIP);
            System.out.println("Using server port " + serverPort);

            return new GameServicesObjectProxy(serverIP, serverPort);

        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
            return null;
        }
    }
}
