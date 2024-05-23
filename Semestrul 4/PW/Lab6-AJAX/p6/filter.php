<?php
$mysqli = new mysqli("localhost", "root", "", "web6_shop");
if ($mysqli->connect_error) {
    die("Connection failed: " . $mysqli->connect_error);
}

$sql = "SELECT * FROM smartphones";

if ($_GET['brand'] || $_GET['display'] || $_GET['storage'] || $_GET['ram'] || $_GET['color']) {
    $sql .= " WHERE";
}
$nConditions = 0;
foreach ($_GET as $key => $value) {
    if ($value) {
        if ($nConditions > 0) {
            $sql .= " AND";
        }
        $nConditions++;
        $sql .= " $key = '$value'";
    }
}

$result = $mysqli->query($sql);

if ($result->num_rows == 0) {
    echo "No results found";
    exit();
}

foreach ($result as $row) {
    echo "<div>";
    echo "Name: " . $row['name'] . "<br>";
    echo "Brand: " . $row['brand'] . "<br>";
    echo "Display: " . $row['display'] . "<br>";
    echo "Storage: " . $row['storage'] . "<br>";
    echo "RAM Memory: " . $row['ram'] . "<br>";
    echo "Color: " . $row['color'] . "<br>";
    echo "</div><hr>";
}
?>
