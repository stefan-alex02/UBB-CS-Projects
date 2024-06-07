<?php
session_start();
if (isset($_SESSION['user_id'])) {
    unset($_SESSION['user_id']);
}

// Destroy the session and redirect to login page
session_destroy();
header("Location: login.html");
exit;
?>
