<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${empty highScores}">
    <jsp:forward page="end-game"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="refresh" content="5">
    <title>High Scores</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <link rel="stylesheet" type="text/css" href="high-scores.css">
</head>
<body>
<h1>Top 10 High Scores</h1>
<table>
    <thead>
    <tr>
        <th>Username</th>
        <th>Wins</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="highScore" items="${highScores}">
        <tr>
            <td>${highScore.username}</td>
            <td>${highScore.wins}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<form method="post" action="end-game">
    <button type="submit">Play Again</button>
</form>
</body>
</html>
