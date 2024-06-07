<?php
$conn = null;
include '../db_config.php'; // Database configuration file

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $article_id = 1; // Assuming there's only one article
    $username = $_POST['username'];
    $comment = $_POST['comment'];

    $stmt = $conn->prepare("INSERT INTO web7_comments6 (article_id, username, comment) VALUES (?, ?, ?)");
    $stmt->bind_param("iss", $article_id, $username, $comment);

    if ($stmt->execute()) {
        echo "Your comment has been submitted for moderation.";
    } else {
        echo "Error: " . $stmt->error;
    }

    $stmt->close();
    $conn->close();
}
?>
