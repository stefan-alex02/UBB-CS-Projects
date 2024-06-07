<?php
$conn = null;
include '../db_config.php';

if (isset($_GET['code'])) {
    $verification_code = $_GET['code'];

    $stmt = $conn->prepare("SELECT id FROM web7_users4 WHERE verification_code = ? AND is_verified = 0");
    $stmt->bind_param("s", $verification_code);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->close();

        $stmt = $conn->prepare("UPDATE web7_users4 SET is_verified = 1 WHERE verification_code = ?");
        $stmt->bind_param("s", $verification_code);
        $stmt->execute();

        if ($stmt->affected_rows > 0) {
            echo "Your email has been verified. You can now log in.";
        } else {
            echo "Error updating verification status.";
        }
    } else {
        echo "Invalid or already verified code.";
    }

    $stmt->close();
}

$conn->close();
?>
