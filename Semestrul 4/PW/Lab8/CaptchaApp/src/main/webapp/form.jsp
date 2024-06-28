<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Form with CAPTCHA</title>
</head>
<body>
<h1>Login with CAPTCHA</h1>
<form action="submitForm" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required checked><br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required><br><br>

    <img src="captcha" alt="CAPTCHA"><br>
    <label for="captcha">Enter CAPTCHA:</label>
    <input type="text" id="captcha" name="captcha" required><br><br>

    <input type="submit" value="Submit">
</form>
<c:if test="${param.error != null}">
    <p style="color:red;">${param.error}</p>
</c:if>
</body>
</html>
