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
import ro.mpp2024.domain.User;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameObserver;
import ro.mpp2024.services.GameServices;

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
    TableColumn<Score, Integer> tableColumnScore;
    @FXML
    TableColumn<Score, Integer> tableColumnDuration;

    private List<Button> buttons;

    private GameServices server;
    private User user;
    private Game game;

    private final ObservableList<Score> scoresModel = FXCollections.observableArrayList();
    private static final int numRows = 4;
    private static final int numCols = 4;

    private int currentRow = 1;

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

        enableRow(currentRow);

        startTime = LocalDateTime.now();
    }

    private void disableRow(int rowNumber) {
        for (int i = 0; i < numCols; i++) {
            buttons.get((rowNumber - 1) * numCols + i).setDisable(true);
        }
    }

    private void enableRow(int rowNumber) {
        for (int i = 0; i < numCols; i++) {
            buttons.get((rowNumber - 1) * numCols + i).setDisable(false);
        }
    }

    private void handleButtonClick(Button button) {
        int row = buttons.indexOf(button) / numCols + 1;
        int col = buttons.indexOf(button) % numCols + 1;

        if (isHole(row, col)) {
            button.setText("X");
            endGame(false);
        } else {
            button.setText("O");
            if (row == numRows) {
                endGame(true);
            } else {
                disableRow(row);
                enableRow(row + 1);
            }
        }
        button.setDisable(true);
    }

    private boolean isHole(int row, int col) {
        return game.getConfigurations().stream()
                .anyMatch(c -> c.getId().getRow() == row && c.getId().getColumn() == col);
    }

    private void endGame(boolean won) {
        buttons.forEach(b -> b.setDisable(true));
        labelGameStatus.setVisible(true);
        labelGameStatus.setText(won ? "You won!" : "Game over!");
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
