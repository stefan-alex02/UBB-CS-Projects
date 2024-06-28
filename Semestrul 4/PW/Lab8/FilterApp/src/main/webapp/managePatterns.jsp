<%@ page import="java.sql.*, java.util.*" %>
<%@ page import="web2024.filterapp.DBUtils" %>
<!DOCTYPE html>
<html>
<head>
  <title>Manage Regular Expressions</title>
</head>
<body>
<h1>Manage Regular Expressions</h1>
<form action="addRegex" method="post">
  <label for="pattern">Add New Pattern:</label>
  <input type="text" id="pattern" name="pattern" required>
  <button type="submit">Add</button>
</form>

<h2>Existing Patterns</h2>
<ul>
  <%
    try (Connection connection = DBUtils.getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT pattern FROM regex_patterns");
      while (rs.next()) {
        out.println("<li>" + rs.getString("pattern") + "</li>");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  %>
</ul>
</body>
</html>
