<?php
$mysqli = new mysqli("localhost", "root", "", "web6_products");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$id = $_POST['id'];
$name = $_POST['name'];
$brand = $_POST['brand'];
$quantity = $_POST['quantity'];

if ($quantity < 0) {
    http_response_code(400);
    echo "Quantity must be a positive number";
    exit();
}

$sql = "UPDATE products SET name = '$name', brand = '$brand', quantity = $quantity WHERE id = $id";

if ($mysqli->query($sql) === TRUE) {
    http_response_code(200);
    echo "Product updated successfully";
} else {
    http_response_code(400);
    echo "Error updating product: " . $mysqli->error;
}

?>