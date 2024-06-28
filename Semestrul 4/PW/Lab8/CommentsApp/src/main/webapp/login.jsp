<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
</head>
<body>
<h1>Admin Login</h1>
<form action="admin" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br>
    <input type="submit" value="Login">
</form>
<c:if test="${param.error != null}">
    <p style="color:red;">${param.error}</p>
</c:if>
</body>
</html>
