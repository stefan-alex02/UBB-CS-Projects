<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tic Tac Toe</title>
    <style>
        table {
            width: 300px;
            height: 300px;
            border-collapse: collapse;
        }
        td {
            width: 100px;
            height: 100px;
            text-align: center;
            vertical-align: middle;
            font-size: 24px;
            border: 1px solid black;
        }
        .cell {
            cursor: pointer;
        }
        .disabled {
            pointer-events: none;
            background-color: lightgray;
        }
    </style>
</head>
<body>
<h1>Tic Tac Toe</h1>
<c:if test="${sessionScope.waiting}">
    <p>Waiting for an opponent...</p>
    <meta http-equiv="refresh" content="5">
</c:if>
<c:if test="${!sessionScope.waiting}">
    <p>Current turn: ${game.currentPlayer}</p>

    <table>
        <c:forEach var="row" begin="0" end="2">
            <tr>
                <c:forEach var="col" begin="0" end="2">
                    <c:set var="disabled" value="${!(sessionScope.winner eq ' '.charAt(0)) || sessionScope.draw}"/>
                    <td class="cell ${disabled ? 'disabled' : ''}"
                            <c:if test="${!disabled}">
                                onclick="makeMove(${row}, ${col})"
                            </c:if>>
                            ${game.board[row][col]}
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${!(sessionScope.winner eq ' '.charAt(0))}">
        <p>Winner: ${sessionScope.winner}</p>
    </c:if>
    <c:if test="${sessionScope.draw && sessionScope.winner eq ' '.charAt(0)}">
        <p>It's a draw!</p>
    </c:if>

    <form method="post" action="end-game">
        <button type="submit">End Game</button>
    </form>
    <meta http-equiv="refresh" content="2">
</c:if>

<script>
    function makeMove(row, col) {
        if ('${game.currentPlayer}' !== '${sessionScope.player}') {
            alert('It is not your turn!');
            return;
        }

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'tic-tac-toe';
        form.innerHTML = '<input type="hidden" name="row" value="' + row + '"/>' +
            '<input type="hidden" name="col" value="' + col + '"/>';
        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>
