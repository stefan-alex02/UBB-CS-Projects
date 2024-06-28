<%@page import="java.sql.*, web2024.commentsapp.DBUtils"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Comments</title>
</head>
<body>
<h1>Comments</h1>
<a href="submit_comment.jsp">Submit a Comment</a>
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
