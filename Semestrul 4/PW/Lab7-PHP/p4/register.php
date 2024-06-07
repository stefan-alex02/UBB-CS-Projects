<?php
require 'vendor/autoload.php';
$conn = null;
include '../db_config.php';
include '../email_credentials.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST['email'];

    $stmt = $conn->prepare("SELECT * FROM web7_users4 WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt->close();

    if ($result->num_rows > 0) {
        http_response_code(400); // Bad Request (Email already exists
        echo "Email already exists.";
        $conn->close();
        return;
    }

    $password = password_hash($_POST['password'], PASSWORD_DEFAULT);
    $verification_code = md5(uniqid(rand(), true));

    $stmt = $conn->prepare("INSERT INTO web7_users4 (email, password, verification_code) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $email, $password, $verification_code);

    if ($stmt->execute()) {
        $verification_link = "http://localhost/web-lab7/p4/verify.php?code=$verification_code";
        $subject = "Verify your email address";
        $message = "Please click this link to verify your email: $verification_link";

        $mail = new PHPMailer(true);
        try {
            // Server settings
            $mail->SMTPDebug = 0; // Disable verbose debug output
            $mail->isSMTP();
            $mail->Host = 'localhost'; // Set the SMTP server to send through
            $mail->SMTPAuth = true;
            $mail->Username = $smtp_username; // SMTP username
            $mail->Password = $smtp_password; // SMTP password
            $mail->SMTPSecure = 'tls'; // Enable TLS encryption
            $mail->Port = 587;

            // Recipients
            $mail->setFrom($smtp_username, 'Cristi Popesco');
            $mail->addAddress($email); // Add a recipient

            // Content
            $mail->isHTML(true); // Set email format to HTML
            $mail->Subject = $subject;
            $mail->Body = $message;

            $mail->send();
            echo 'A verification email has been sent to your email address.';
        } catch (Exception $e) {
            echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";

            $stmt = $conn->prepare("DELETE FROM web7_users4 WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
        }
    } else {
        echo "Error: " . $stmt->error;
    }

    $stmt->close();
}

$conn->close();
?>
