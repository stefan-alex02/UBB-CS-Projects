package web2024.tictactoeapp;

public class HighScore {
    private String username;
    private int wins;

    public HighScore() {
    }

    public HighScore(String username, int wins) {
        this.username = username;
        this.wins = wins;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }
}