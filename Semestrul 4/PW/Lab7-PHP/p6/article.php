<?php
$conn = null;
include '../db_config.php';

// Fetch the article
$article_id = 1; // Assuming there's only one article
$stmt = $conn->prepare("SELECT title, content FROM web7_articles6 WHERE id = ?");
$stmt->bind_param("i", $article_id);
$stmt->execute();
$stmt->bind_result($title, $content);
$stmt->fetch();
$stmt->close();

// Fetch approved comments
$stmt = $conn->prepare("SELECT username, comment FROM web7_comments6 WHERE article_id = ? AND approved = TRUE");
$stmt->bind_param("i", $article_id);
$stmt->execute();
$result = $stmt->get_result();
$comments = [];
while ($row = $result->fetch_assoc()) {
    $comments[] = $row;
}
$stmt->close();
$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><?php echo htmlspecialchars($title); ?></title>
</head>
<body>
<h1><?php echo htmlspecialchars($title); ?></h1>
<p><?php echo nl2br(htmlspecialchars($content)); ?></p>

<h2>Comments</h2>
<div>
    <?php foreach ($comments as $comment): ?>
        <div>
            <strong><?php echo htmlspecialchars($comment['username']); ?>:</strong>
            <p><?php echo nl2br(htmlspecialchars($comment['comment'])); ?></p>

            <!-- XSS vulnerability -->
<!--            <strong>--><?php //echo $comment['username']; ?><!--:</strong>-->
<!--            <p>--><?php //echo nl2br($comment['comment']); ?><!--</p>-->
        </div>
    <?php endforeach; ?>
</div>

<h2>Leave a Comment</h2>
<form action="post_comment.php" method="POST">
    <input type="text" name="username" placeholder="Your name" required>
    <textarea name="comment" placeholder="Your comment" required></textarea>
    <button type="submit">Post Comment</button>
</form>
</body>
</html>
