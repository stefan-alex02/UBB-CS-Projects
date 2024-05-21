<?php
$mysqli = new mysqli("localhost", "root", "", "web6_products");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$sql = "SELECT id FROM products";
$result = $mysqli->query($sql);

echo "<option disabled selected value=''>-- Select a product --</option>";
while ($row = $result->fetch_assoc()) {
    echo "<option value='" . $row['id'] . "'>" . $row['id'] . "</option>";
}

$mysqli->close();

?>
