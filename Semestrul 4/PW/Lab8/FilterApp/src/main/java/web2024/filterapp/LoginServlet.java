package web2024.filterapp;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection connection = DBUtils.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT password_hash FROM admin_users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (BCrypt.checkpw(password, storedHash)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("authenticated", true);
                    response.sendRedirect("managePatterns.jsp");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("errorMessage", "Invalid username or password");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
