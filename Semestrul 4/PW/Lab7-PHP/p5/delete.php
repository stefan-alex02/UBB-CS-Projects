<?php
$conn = null;
include '../db_config.php';
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.html");
    exit();
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $photo_id = $_POST['photo_id'];
    $user_id = $_SESSION['user_id'];

    // Fetch the filename
    $stmt = $conn->prepare("SELECT filename FROM web7_photos5 WHERE id = ? AND user_id = ?");
    $stmt->bind_param("ii", $photo_id, $user_id);
    $stmt->execute();
    $stmt->store_result();
    $stmt->bind_result($filename);
    $stmt->fetch();

    if ($stmt->num_rows > 0) {
        // Delete the file from the server
        unlink('uploads/' . $filename);

        // Delete the record from the database
        $stmt = $conn->prepare("DELETE FROM web7_photos5 WHERE id = ? AND user_id = ?");
        $stmt->bind_param("ii", $photo_id, $user_id);
        $stmt->execute();
    }

    $stmt->close();
}

$conn->close();
header("Location: profile.php");
?>
