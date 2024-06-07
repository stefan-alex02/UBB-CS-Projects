<?php
$conn = null;
include '../db_config.php'; // Database configuration file
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.html");
    exit();
}

if ($_SERVER["REQUEST_METHOD"] == "GET" && isset($_GET['username'])) {
    $username = $_GET['username'];

    // Fetch the user ID for the given username
    $stmt = $conn->prepare("SELECT id FROM web7_users5 WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $stmt->store_result();
    $user_id = null;
    $stmt->bind_result($user_id);
    $stmt->fetch();

    if ($stmt->num_rows > 0) {
        // Fetch the user's photos
        $stmt->close(); // Close the previous statement
        $stmt = $conn->prepare("SELECT filename FROM web7_photos5 WHERE user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $photos = [];
        while ($row = $result->fetch_assoc()) {
            $photos[] = $row;
        }
    } else {
        echo "User not found.";
        exit();
    }

    $stmt->close();
    $conn->close();
} else {
    header("Location: profile.php");
    exit();
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><?php echo htmlspecialchars($username); ?>'s Profile</title>
</head>
<body>
<h1><?php echo htmlspecialchars($username); ?>'s Profile</h1>
<div>
    <?php foreach ($photos as $photo): ?>
        <div>
            <img src="uploads/<?php echo htmlspecialchars($photo['filename']); ?>" alt="Photo" width="200">
        </div>
    <?php endforeach; ?>
</div>
<div>
    <a href="profile.php">Back to your profile</a>
</div>
</body>
</html>
