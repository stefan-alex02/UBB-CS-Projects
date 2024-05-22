<?php
$path = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = __DIR__ . '/' . $path;

$items = [];
if (is_dir($fullPath)) {
    $dir = opendir($fullPath);
    while (($entry = readdir($dir)) !== false) {
        if ($entry == '.' || $entry == '..') continue;
        $entryPath = $path . '/' . $entry;
        $items[] = [
            'name' => $entry,
            'isFile' => is_file($fullPath . '/' . $entry),
            'path' => $entryPath
        ];
    }
    closedir($dir);
}

echo json_encode($items);
?>
