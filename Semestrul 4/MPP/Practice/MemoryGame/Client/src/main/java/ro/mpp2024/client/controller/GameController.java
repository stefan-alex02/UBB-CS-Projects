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
import ro.mpp2024.client.guiutils.MessageAlerter;
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
    private List<Configuration> configurations;
    private Button firstSelected;
    private Button secondSelected;

    private GameServices server;
    private User user;
    private Game game;
    private int attempts;

    private final ObservableList<Score> scoresModel = FXCollections.observableArrayList();
    private static final int maxAttempts = 10;

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
        configurations = game.getConfigurations()
                .stream()
                .sorted(Comparator.comparingInt(c -> c.getId().getPosition()))
                .toList();
        buttons = new ArrayList<>();
        firstSelected = null;
        secondSelected = null;
        attempts = 0;

        gridGame.getChildren().clear();

        for (int i = 0; i < 10; i++) {
            Button button = new Button("?");
            button.setPrefSize(80, 20);
            button.setOnAction(event -> handleButtonClick(button));
            buttons.add(button);
            gridGame.add(button, i % 5, i / 5);
        }

        startTime = LocalDateTime.now();
    }

    private void handleButtonClick(Button button) {
        if (firstSelected == null) {
            firstSelected = button;
            revealButton(button);
        } else {
            attempts++;

            if (secondSelected == null && button != firstSelected) {
                secondSelected = button;
                revealButton(button);

                game.getPairs().add(new Pair(null,
                        buttons.indexOf(firstSelected),
                        buttons.indexOf(secondSelected),
                        game));

                if (getButtonWord(firstSelected).equals(getButtonWord(secondSelected))) {
                    firstSelected.setDisable(true);
                    secondSelected.setDisable(true);

                    firstSelected = null;
                    secondSelected = null;

                    game.setScore(game.getScore() - 2);

                    if (buttons.stream().noneMatch(b -> b.getText().equals("?"))) {
                        endGame(true);
                    }
                } else {
                    game.setScore(game.getScore() + 3);

                    if (attempts < maxAttempts) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    hideButton(firstSelected);
                                    hideButton(secondSelected);
                                    firstSelected = null;
                                    secondSelected = null;
                                });
                            }
                        }, 1000);
                    }
                }
            }

            if (attempts >= maxAttempts) {
                endGame(false);
            }
        }
    }

    private void revealButton(Button button) {
        int index = buttons.indexOf(button);
        button.setText(configurations.get(index).getWord().getId());
    }

    private void hideButton(Button button) {
        button.setText("?");
    }

    private Word getButtonWord(Button button) {
        int index = buttons.indexOf(button);
        return configurations.get(index).getWord();
    }

    private void endGame(boolean won) {
        endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        game.setDuration((int) duration.toSeconds());

        buttons.forEach(b -> {
            b.setDisable(true);
            revealButton(b);
        });

        labelGameStatus.setText(won ? "Game Won!" : "Game Over");
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
