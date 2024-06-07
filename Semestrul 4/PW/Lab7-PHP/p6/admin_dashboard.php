<?php
$conn = null;
include '../db_config.php'; // Database configuration file
session_start();

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'admin') {
    header("Location: login.html");
    exit();
}

// Fetch unapproved comments
$stmt = $conn->prepare("SELECT id, username, comment FROM web7_comments6 WHERE approved = FALSE");
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
    <title>Admin Dashboard</title>
</head>
<body>
<h1>Admin Dashboard</h1>
<h2>Pending Comments</h2>
<div>
    <?php foreach ($comments as $comment): ?>
        <div>
            <strong><?php echo htmlspecialchars($comment['username']); ?>:</strong>
            <p><?php echo nl2br(htmlspecialchars($comment['comment'])); ?></p>
            <form action="approve_comment.php" method="POST">
                <input type="hidden" name="comment_id" value="<?php echo $comment['id']; ?>">
                <button type="submit">Approve</button>
            </form>
            <form action="delete_comment.php" method="POST">
                <input type="hidden" name="comment_id" value="<?php echo $comment['id']; ?>">
                <button type="submit">Delete</button>
            </form>
        </div>
    <?php endforeach; ?>
</div>
</body>
</html>
