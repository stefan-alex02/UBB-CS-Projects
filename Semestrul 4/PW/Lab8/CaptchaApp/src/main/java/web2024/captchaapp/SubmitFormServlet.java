package web2024.captchaapp;

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
import java.sql.SQLException;

@WebServlet("/submitForm")
public class SubmitFormServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String captcha = (String) session.getAttribute("captcha");

        String enteredCaptcha = request.getParameter("captcha");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (captcha != null && captcha.equals(enteredCaptcha)) {
            try (Connection connection = DBUtils.getConnection()) {
                String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, email);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    session.setAttribute("email", email);
                    response.sendRedirect("page.jsp");
                } else {
                    response.sendRedirect("form.jsp?error=Invalid email or password");
                }
            } catch (SQLException e) {
                throw new ServletException("Database error", e);
            }

//            response.getWriter().write("Form submitted successfully!");
        } else {
            response.sendRedirect("form.jsp?error=Invalid captcha code. Please try again.");
        }
    }
}
