package web2024.tictactoeapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/record-win")
public class RecordWinServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String winnerName = request.getParameter("winnerName");

        try (Connection connection = DBUtils.getConnection()) {
            String selectQuery = "SELECT wins FROM high_scores WHERE username = ?";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setString(1, winnerName);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int wins = rs.getInt("wins");
                    String updateQuery = "UPDATE high_scores SET wins = ? WHERE username = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, wins + 1);
                        updateStmt.setString(2, winnerName);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String insertQuery = "INSERT INTO high_scores (username, wins) VALUES (?, 1)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, winnerName);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect("high-scores");
    }
}
