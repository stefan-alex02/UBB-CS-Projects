package ro.mpp2024.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import ro.mpp2024.client.guiutils.MessageAlerter;
import ro.mpp2024.client.model.Score;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.Trial;
import ro.mpp2024.domain.User;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameObserver;
import ro.mpp2024.services.GameServices;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class GameController implements Initializable, GameObserver {
    @FXML
    GridPane gridGame;
    @FXML
    Label labelScore;
    @FXML
    Label labelGameStatus;
    @FXML
    TableView<Score> tableViewScores;
    @FXML
    TableColumn<Score, String> tableColumnAlias;
    @FXML
    TableColumn<Score, Integer> tableColumnScore;
    @FXML
    TableColumn<Score, Integer> tableColumnDuration;

    private List<Button> buttons;

    private GameServices server;
    private User user;
    private Game game;

    private final ObservableList<Score> scoresModel = FXCollections.observableArrayList();
    private static final int numRows = 5;
    private static final int numCols = 5;

    private int currentCol = 1;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

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
        tableColumnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        tableColumnDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

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
        buttons = new ArrayList<>();
        gridGame.getChildren().clear();

        for (int i = 0; i < numRows * numCols; i++) {
            Button button = new Button(" ");
            button.setPrefSize(60, 60);
            button.setFont(new Font("Cascadia", 14));
            button.setOnAction(event -> handleButtonClick(button));
            button.setDisable(true);
            buttons.add(button);
            gridGame.add(button, i % numCols, i / numCols);
        }

        enableCol(currentCol);

        startTime = LocalDateTime.now();
    }

    private void disableCol(int colNumber) {
        for (int i = 0; i < numRows; i++) {
            buttons.get(numCols * i + colNumber - 1).setDisable(true);
        }
    }

    private void enableCol(int colNumber) {
        for (int i = 0; i < numRows; i++) {
            buttons.get(numCols * i + colNumber - 1).setDisable(false);
        }
    }

    private void handleButtonClick(Button button) {
        int row = buttons.indexOf(button) / numCols + 1;
        int col = buttons.indexOf(button) % numCols + 1;

        game.getTrials().add(new Trial(new Trial.TrialId(game.getId(), row, col), game));

        if (isTrap(row, col)) {
            button.setText("X");
//            disableCol(col);

            endGame(false);
        } else {
            button.setText("O");

            game.setScore(game.getScore() + col * 2);

            if (col == numCols) {
                endGame(true);
            } else {
                disableCol(col);
                enableCol(col + 1);
            }
        }
        button.setDisable(true);
    }

    private boolean isTrap(int row, int col) {
        return game.getConfigurations().stream()
                .anyMatch(c -> c.getId().getRow() == row && c.getId().getColumn() == col);
    }

    private void revealTraps() {
        game.getConfigurations()
                .forEach(c ->
                        buttons.get(
                                (c.getId().getRow() - 1) * numCols + c.getId().getColumn() - 1)
                                .setText("X"));
    }

    private void endGame(boolean won) {
        revealTraps();

        endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        game.setDuration((int) duration.toSeconds());

        game.setWon(won);

        buttons.forEach(b -> b.setDisable(true));
        labelGameStatus.setVisible(true);
        labelGameStatus.setText(won ? "You won!" : "Game over!");

        labelScore.setText("Score: " + game.getScore());

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
