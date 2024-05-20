<?php
$mysqli = new mysqli("localhost", "root", "", "web6_users");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}

$page = $_GET['page'];
$limit = $_GET['pageSize'];
$offset = ($page - 1) * $limit;
$limit++;

$sql = "SELECT * FROM user_details LIMIT ? OFFSET ?";
$stmt = $mysqli->prepare($sql);
$stmt->bind_param("ii", $limit, $offset);
$stmt->execute();
$result = $stmt->get_result();

// Create a new XML document
$doc = new DOMDocument();
$root = $doc->createElement('users');
$doc->appendChild($root);

try {
    while (($row = $result->fetch_assoc()) && --$limit > 0) {
        $user = $doc->createElement('user');
        $root->appendChild($user);

        $user->appendChild($doc->createElement('first_name', $row['first_name']));
        $user->appendChild($doc->createElement('last_name', $row['last_name']));
        $user->appendChild($doc->createElement('phone', $row['phone']));
        $user->appendChild($doc->createElement('email', $row['email']));
    }

    $stmt->close();

    $root->appendChild($doc->createElement('next-on', $limit == 0 ? 1 : 0));
    $root->appendChild($doc->createElement('prev-on', $page > 1 ? 1 : 0));

    // Set the content type to XML
    header('Content-type: text/xml');

    // Output the XML data
    echo $doc->saveXML();
}
catch (Exception $e) {
    echo $e->getMessage();
}

?>