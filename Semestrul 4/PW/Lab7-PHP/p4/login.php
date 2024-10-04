<?php
session_start();
$conn = null;
require_once '../db_config.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $stmt = $conn->prepare("SELECT id, password, is_verified FROM web7_users4 WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $stmt->store_result();
    $stmt->bind_result($id, $hashed_password, $is_verified);

    if ($stmt->num_rows > 0) {
        $stmt->fetch();
        if (password_verify($password, $hashed_password)) {
            if ($is_verified) {
                $_SESSION['user_id'] = $id;
                echo "Login successful. Welcome!";
                echo "<br><a href='logout.php'>Logout</a>";
            } else {
                echo "Please verify your email before logging in.";
            }
        } else {
            echo "Invalid email or password.";
        }
    } else {
        echo "Invalid email or password.";
    }

    $stmt->close();
}
else if ($_SERVER["REQUEST_METHOD"] == "GET") {
    if (isset($_SESSION['user_id']) && $_SESSION['user_id'] != "") {
        echo "You are already logged in.";
        echo "<br><a href='logout.php'>Logout</a>";
    } else {
        # Send Unauthorized status code
        http_response_code(401);
        echo "You are not logged in.";
    }
}

$conn->close();
?>
