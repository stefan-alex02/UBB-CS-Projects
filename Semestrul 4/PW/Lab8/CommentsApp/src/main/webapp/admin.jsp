<%@page import="java.sql.*, web2024.commentsapp.DBUtils"%>
<%@page session="true" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Panel</title>
</head>
<body>
<h1>Admin Panel</h1>
<h2>Pending Comments</h2>
<table border="1">
    <tr>
        <th>Email</th>
        <th>Comment</th>
        <th>Action</th>
    </tr>
    <%
        try (Connection connection = DBUtils.getConnection()) {
            String sql = "SELECT * FROM comments WHERE is_approved = FALSE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String commentText = resultSet.getString("comment_text");
    %>
    <tr>
        <td><%= email %></td>
        <td><%= commentText %></td>
        <td>
            <a href="admin?action=approve&id=<%= id %>">Approve</a> |
            <a href="admin?action=delete&id=<%= id %>">Delete</a>
        </td>
    </tr>
    <%
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    %>
</table>
<h2>Approved Comments</h2>
<table border="1">
    <tr>
        <th>Email</th>
        <th>Comment</th>
    </tr>
    <%
        try (Connection connection = DBUtils.getConnection()) {
            String sql = "SELECT * FROM comments WHERE is_approved = TRUE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String commentText = resultSet.getString("comment_text");
    %>
    <tr>
        <td><%= email %></td>
        <td><%= commentText %></td>
    </tr>
    <%
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    %>
</table>
</body>
</html>
