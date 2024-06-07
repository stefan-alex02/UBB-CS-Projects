<?php
$conn = null;
include '../db_config.php';
session_start();

if (!isset($_SESSION['user_id'])) {
    header("Location: login.html");
    exit();
}

$user_id = $_SESSION['user_id'];

// Fetch user's photos
$stmt = $conn->prepare("SELECT id, filename FROM web7_photos5 WHERE user_id = ?");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$photos = [];
while ($row = $result->fetch_assoc()) {
    $photos[] = $row;
}

$stmt->close();
$conn->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
</head>
<body>
<h1>Your Profile</h1>
<form action="upload.php" method="POST" enctype="multipart/form-data">
    <input type="file" name="photo" required>
    <button type="submit">Upload</button>
</form>

<h2>Your Photos</h2>
<div>
    <?php foreach ($photos as $photo): ?>
        <div>
            <img src="uploads/<?php echo $photo['filename']; ?>" alt="Photo" width="200">
            <form action="delete.php" method="POST">
                <input type="hidden" name="photo_id" value="<?php echo $photo['id']; ?>">
                <button type="submit">Delete</button>
            </form>
        </div>
    <?php endforeach; ?>
</div>

<div>
    <a href="logout.php">Logout</a>
</div>

<h2>Other Users' Profiles</h2>
<div>
    <form action="view_profile.php" method="GET">
        <input type="text" name="username" placeholder="Enter username to view profile" required>
        <button type="submit">View Profile</button>
    </form>
</div>
</body>
</html>
