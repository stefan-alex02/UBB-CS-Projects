<?php
$mysqli = new mysqli("localhost", "root", "", "web6_trains");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$sql = "SELECT departure FROM routes group by departure";
$stmt = $mysqli->prepare($sql);
$stmt->execute();
$result = $stmt->get_result();
$stmt->close();

while ($row = $result->fetch_assoc()) {
    echo '<li>' . $row['departure'] . '</li>';
}
?>