<?php
$mysqli = new mysqli("localhost", "root", "", "web6_trains");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$departure = urldecode($_GET['d']);

$sql = "SELECT arrival FROM routes where departure = ?";
$stmt = $mysqli->prepare($sql);
$stmt->bind_param("s", $departure);
$stmt->execute();
$result = $stmt->get_result();
$stmt->close();

while ($row = $result->fetch_assoc()) {
    echo '<li>' . $row['arrival'] . '</li>';
}
?>