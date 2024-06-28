<% if (session.getAttribute("email") == null) {
    response.sendRedirect("form.jsp");
    return;
 } %>
<html>
<head>
    <title>Account page</title>
</head>
<body>
<h1>Account page</h1>
<p>Welcome, ${sessionScope.email}!</p>
<button onclick="location.href='logout'">Logout</button>
</body>
</html>
