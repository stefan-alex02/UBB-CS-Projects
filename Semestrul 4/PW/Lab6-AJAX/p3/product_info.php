<?php
$mysqli = new mysqli("localhost", "root", "", "web6_products");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$id = $_GET['id'];
$sql = "SELECT * FROM products WHERE id = $id";
$result = $mysqli->query($sql);
$product = $result->fetch_assoc();

if ($product == null) {
    echo "Product not found";
    exit();
}

try {
    $doc = new DOMDocument();
    $root = $doc->createElement('product');
    $doc->appendChild($root);
    $root->appendChild($doc->createElement('id', $product['id']));
    $root->appendChild($doc->createElement('name', $product['name']));
    $root->appendChild($doc->createElement('brand', $product['brand']));
    $root->appendChild($doc->createElement('quantity', $product['quantity']));

    header('Content-type: text/xml');

    echo $doc->saveXML();
}
catch (Exception $e) {
    echo $e->getMessage();
}

$mysqli->close();
?>