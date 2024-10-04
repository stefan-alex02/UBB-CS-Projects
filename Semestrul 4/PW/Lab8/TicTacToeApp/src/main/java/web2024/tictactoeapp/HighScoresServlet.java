package web2024.tictactoeapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/high-scores")
public class HighScoresServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<HighScore> highScores = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection()) {
            String query = "SELECT username, wins FROM high_scores ORDER BY wins DESC LIMIT 10";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    highScores.add(new HighScore(rs.getString("username"), rs.getInt("wins")));
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.setAttribute("highScores", highScores);
        request.getRequestDispatcher("/WEB-INF/high-scores.jsp").forward(request, response);
    }
}
