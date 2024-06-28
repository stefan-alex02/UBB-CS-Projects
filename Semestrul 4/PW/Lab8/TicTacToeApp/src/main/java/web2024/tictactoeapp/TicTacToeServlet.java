package web2024.tictactoeapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class TicTacToeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final HashMap<String, TicTacToeGame> games = new HashMap<>();
    private static final Queue<HttpSession> waitingPlayers = new LinkedList<>();
    private static final Character PLAYER_X = 'X';
    private static final Character PLAYER_O = 'O';

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();

        TicTacToeGame game;
        if (games.containsKey(sessionId)) {
            game = games.get(sessionId);
        } else {
            game = new TicTacToeGame();
            if (waitingPlayers.isEmpty()) {
                waitingPlayers.add(session);
                session.setAttribute("waiting", true);
                System.out.println("Adding player to waiting list: " + sessionId);
            } else {
                HttpSession opponentSession = waitingPlayers.poll();
                String opponentId = opponentSession.getId();

                if (opponentSession == session) {
                    // Put the opponent back in waiting if the same session tries to pair with itself
                    waitingPlayers.add(session);
                    session.setAttribute("waiting", true);
                    System.out.println("Player " + sessionId + " tried to pair with themselves. Kept in waiting.");
                } else {
                    waitingPlayers.removeIf(s -> Objects.equals(s.getId(), sessionId));

                    // Start the game
                    games.put(sessionId, game);
                    games.put(opponentId, game);

                    session.setAttribute("player", PLAYER_X);
                    opponentSession.setAttribute("player", PLAYER_O);

                    session.setAttribute("waiting", false);
                    opponentSession.setAttribute("waiting", false);
                    System.out.println("Starting game between players: " + sessionId + " and " + opponentId);
                }
            }
        }

        System.out.println("Setting game attribute: " + game);
        request.setAttribute("game", game);
        session.setAttribute("winner", game.getWinner());
        session.setAttribute("draw", game.isBoardFull());
        request.getRequestDispatcher("/WEB-INF/tic-tac-toe.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String sessionId = session.getId();

        if (!games.containsKey(sessionId)) {
            response.sendRedirect("tic-tac-toe");
            return;
        }

        TicTacToeGame game = games.get(sessionId);
        int row = Integer.parseInt(request.getParameter("row"));
        int col = Integer.parseInt(request.getParameter("col"));

        char currentPlayer = (char) session.getAttribute("player");

        if (currentPlayer != game.getCurrentPlayer()) {
            response.getWriter().print("It's not your turn!");
            return;
        }

        if (game.makeMove(row, col)) {
            char winner = game.checkWinner();
            if (winner != ' ' || game.isBoardFull()) {
                session.setAttribute("winner", winner);
                session.setAttribute("draw", game.isBoardFull());
            }
        }

        response.sendRedirect("tic-tac-toe");
    }
}
