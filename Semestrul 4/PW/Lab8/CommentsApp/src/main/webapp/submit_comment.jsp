<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Submit Comment</title>
</head>
<body>
<h1>Submit a Comment</h1>
<form action="submitComment" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br>
    <label for="comment_text">Comment:</label>
    <textarea id="comment_text" name="comment_text" required></textarea><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
