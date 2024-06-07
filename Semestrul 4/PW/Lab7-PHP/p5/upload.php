<?php
$conn = null;
include '../db_config.php'; // Database configuration file
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.html");
    exit();
}

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES['photo'])) {
    $user_id = $_SESSION['user_id'];
    $photo = $_FILES['photo'];

    $upload_dir = 'uploads/';
    // Ensure the uploads directory exists
    if (!is_dir($upload_dir)) {
        mkdir($upload_dir, 0777, true);
    }

    $filename = basename($photo['name']);
    $target_file = $upload_dir . $filename;

    if (move_uploaded_file($photo['tmp_name'], $target_file)) {
        $stmt = $conn->prepare("INSERT INTO web7_photos5 (user_id, filename) VALUES (?, ?)");
        $stmt->bind_param("is", $user_id, $filename);

        if ($stmt->execute()) {
            header("Location: profile.php");
        } else {
            echo "Error: " . $stmt->error;
        }

        $stmt->close();
    } else {
        echo "Failed to upload photo.";
        echo "Error Information: " . print_r($_FILES, true);
    }
}

$conn->close();
?>
