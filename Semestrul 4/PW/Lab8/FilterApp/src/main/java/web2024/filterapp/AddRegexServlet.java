package web2024.filterapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/addRegex")
public class AddRegexServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String pattern = request.getParameter("pattern");

        try (Connection connection = DBUtils.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO regex_patterns (pattern) VALUES (?)");
            ps.setString(1, pattern);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("managePatterns.jsp");
    }
}
