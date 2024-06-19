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
import ro.mpp2024.client.model.GameStatus;
import ro.mpp2024.client.model.Score;
import ro.mpp2024.domain.*;
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
    private static final int numRows = 3;
    private static final int numCols = 3;

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

    public void initializeGame() {
        this.game = new Game(0, user, new ArrayList<>(), 0, 0);
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

        for (int i = 0; i < 9; i++) {
            Button button = new Button(" ");
            button.setPrefSize(80, 80);
            button.setFont(new Font("Cascadia", 20));
            button.setOnAction(event -> handleButtonClick(button));
            buttons.add(button);
            gridGame.add(button, i % numCols, i / numCols);
        }

        startTime = LocalDateTime.now();
    }

    private void handleButtonClick(Button button) {
        MarkButton(button, "X");

        game.getTrials().add(new Trial(
            new Trial.TrialId(null,
                    buttons.indexOf(button) / numCols + 1,
                    buttons.indexOf(button) % numCols + 1),
            'X',
            game));

        var status = checkGameStatus();
        if (status != GameStatus.NOT_FINISHED) {
            endGame(status);
            return;
        }

        computerMove();

        status = checkGameStatus();
        if (status != GameStatus.NOT_FINISHED) {
            endGame(status);
        }
    }

    private GameStatus checkGameStatus() {
        char[][] board = new char[numRows][numCols];
        for (int i = 0; i < 9; i++) {
            Button button = buttons.get(i);
            int row = i / numCols;
            int col = i % numCols;
            board[row][col] = button.getText().charAt(0);
        }

        for (int i = 0; i < numRows; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                return board[i][0] == 'X' ? GameStatus.WON : GameStatus.LOST;
            }
        }

        for (int i = 0; i < numCols; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                return board[0][i] == 'X' ? GameStatus.WON : GameStatus.LOST;
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return board[0][0] == 'X' ? GameStatus.WON : GameStatus.LOST;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return board[0][2] == 'X' ? GameStatus.WON : GameStatus.LOST;
        }

        boolean draw = true;
        for (Button button : buttons) {
            if (button.getText().equals(" ")) {
                draw = false;
                break;
            }
        }

        return draw ? GameStatus.DRAW : GameStatus.NOT_FINISHED;
    }

    private void computerMove() {
        Random rand = new Random();
        List<Button> availableButtons = new ArrayList<>();
        for (Button button : buttons) {
            if (button.getText().equals(" ")) {
                availableButtons.add(button);
            }
        }

        if (!availableButtons.isEmpty()) {
            Button button = availableButtons.get(rand.nextInt(availableButtons.size()));
            MarkButton(button, "O");

            game.getTrials().add(new Trial(
                    new Trial.TrialId(null,
                            buttons.indexOf(button) / numCols + 1,
                            buttons.indexOf(button) % numCols + 1),
                    'O',
                    game));
        }
    }

    private void MarkButton(Button button, String text) {
        button.setText(text);
        button.setDisable(true);
    }

    private void endGame(GameStatus status) {
        endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        game.setDuration((int) duration.toSeconds());

        buttons.forEach(b -> {
            b.setDisable(true);
        });

        switch(status) {
            case WON:
                labelGameStatus.setText("Game Won!");
                game.setScore(10);
                break;
            case LOST:
                labelGameStatus.setText("Game Lost");
                game.setScore(-10);
                break;
            case DRAW:
                labelGameStatus.setText("Game Draw");
                game.setScore(5);
                break;
            default:;
        }
        labelScore.setText("Score: " + game.getScore());

        labelGameStatus.setVisible(true);
        labelScore.setVisible(true);

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
