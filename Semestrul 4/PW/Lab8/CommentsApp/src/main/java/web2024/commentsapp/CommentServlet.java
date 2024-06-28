package web2024.commentsapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String commentText = request.getParameter("comment_text");

        try (Connection connection = DBUtils.getConnection()) {
            String sql = "INSERT INTO comments (email, comment_text) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, commentText);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

        response.sendRedirect("comments.jsp");
    }
}
