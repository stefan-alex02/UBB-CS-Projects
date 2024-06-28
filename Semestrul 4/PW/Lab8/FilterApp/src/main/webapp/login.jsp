<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Login</title>
</head>
<body>
<h1>Admin Login</h1>
<form action="login" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required>
    <br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>
    <br>
    <button type="submit">Login</button>
</form>
<%
    if (request.getAttribute("errorMessage") != null) {
        out.println("<p style='color:red;'>" + request.getAttribute("errorMessage") + "</p>");
    }
%>
</body>
</html>
