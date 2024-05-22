<?php
$path = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = __DIR__ . '/' . $path;

if (is_file($fullPath)) {
    echo file_get_contents($fullPath);
} else {
    echo 'File not found.';
}
?>
