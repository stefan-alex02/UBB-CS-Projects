package ro.mpp2024.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import ro.mpp2024.client.guiutils.MessageAlerter;
import ro.mpp2024.client.model.Score;
import ro.mpp2024.domain.*;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameObserver;
import ro.mpp2024.services.GameServices;
import java.text.DecimalFormat;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class GameController implements Initializable, GameObserver {
    @FXML
    GridPane gridGame;
    @FXML
    Label labelTrialsCount;
    @FXML
    Label labelGameStatus;
    @FXML
    Label labelHint;
    @FXML
    TableView<Score> tableViewScores;
    @FXML
    TableColumn<Score, String> tableColumnAlias;
    @FXML
    TableColumn<Score, LocalDateTime> tableColumnStarted;
    @FXML
    TableColumn<Score, Integer> tableColumnTrials;
    @FXML
    TableColumn<Score, String> tableColumnHint;

    private List<Button> buttons;

    private GameServices server;
    private User user;
    private Game game;

    private final ObservableList<Score> scoresModel = FXCollections.observableArrayList();
    private static final int numRows = 4;
    private static final int numCols = 4;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final int maxAttempts = 4;
    private int attempts = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialize();
    }

    public void setServer(GameServices services) {
        server = services;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private void fillScoresModel(Collection<Game> games) {
        scoresModel.setAll(games.stream().map(Score::fromGame).toList());
    }

    @Override
    public void newFinishedGame(Collection<Game> games) throws GameException {
        Platform.runLater(() -> {
            fillScoresModel(games);
        });
    }

    @FXML
    public void initialize() {
        tableColumnAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        tableColumnStarted.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        tableColumnTrials.setCellValueFactory(new PropertyValueFactory<>("trialsCount"));
        tableColumnHint.setCellValueFactory(new PropertyValueFactory<>("hint"));

        tableViewScores.getSelectionModel().setSelectionMode(null);

        tableViewScores.setItems(scoresModel);
    }

    public void initializeScoresModel() {
        try {
            fillScoresModel(server.getGames());
        } catch (GameException e) {
            MessageAlerter.showErrorMessage(null,
                    "Error: Couldn't load games",
                    e.getMessage());
        }
    }

    public void startGame() {
        game.setStartDateTime(LocalDateTime.now());

        buttons = new ArrayList<>();

        gridGame.getChildren().clear();

        for (int i = 0; i < numRows * numCols; i++) {
            Button button = new Button(" ");
            button.setPrefSize(60, 60);
            button.setFont(new Font("Cascadia", 14));
            button.setOnAction(event -> handleButtonClick(button));
            buttons.add(button);
            gridGame.add(button, i % numCols, i / numCols);
        }
    }

    private void handleButtonClick(Button button) {
        game.getTrials().add(new Trial(
                new Trial.TrialId(game.getId(),
                        buttons.indexOf(button) / numCols + 1,
                        buttons.indexOf(button) % numCols + 1),
                game
        ));
        attempts++;

        RevealButton(button);
    }

    private double euclidianDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void RevealButton(Button button) {
        button.setDisable(true);

        int row = buttons.indexOf(button) / numCols + 1;
        int col = buttons.indexOf(button) % numCols + 1;

        if (row == game.getConfiguration().getRow() && col == game.getConfiguration().getColumn()) {
            game.setHint(game.getConfiguration().getHint());
            button.setText("*");
            endGame();
        }
        else {
            double distance = euclidianDistance(col,
                    row,
                    game.getConfiguration().getColumn(),
                    game.getConfiguration().getRow());

            button.setText(df.format(distance));

            if (attempts >= maxAttempts) {
                endGame();
            }
        }
    }

    private void endGame() {
        buttons.forEach(b -> {
            b.setDisable(true);
        });

        labelGameStatus.setVisible(true);
        labelTrialsCount.setVisible(true);

        labelTrialsCount.setText("Trials count: " + attempts);

        if (!Objects.equals(game.getHint(), "")) {
            labelHint.setVisible(true);
            labelHint.setText("The hint: " + game.getHint());
            labelGameStatus.setText("Game won!");
        }
        else {
            labelGameStatus.setText("Game over");
        }

        try {
            server.saveGame(game);
        } catch (GameException e) {
            MessageAlerter.showErrorMessage(null, "Saving error", e.getMessage());
        }
    }

    @FXML
    private void handleButtonLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    void logout() {
        try {
            server.logout(user, this);
        } catch (GameException e) {
            System.out.println("Logout error " + e);
        }
    }
}
