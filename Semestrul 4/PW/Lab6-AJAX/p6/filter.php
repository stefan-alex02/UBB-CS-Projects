<?php
$notebooks = [
    ["brand" => "Dell", "cpu" => "Intel", "ram" => "16GB", "hdd" => "512GB", "gpu" => "NVIDIA"],
    ["brand" => "HP", "cpu" => "AMD", "ram" => "8GB", "hdd" => "256GB", "gpu" => "AMD"],
    ["brand" => "Lenovo", "cpu" => "Intel", "ram" => "32GB", "hdd" => "1TB", "gpu" => "NVIDIA"],
    // Adaugă mai multe produse după necesități
];

$filteredNotebooks = array_filter($notebooks, function($notebook) {
    foreach ($_GET as $key => $value) {
        if ($value && $notebook[$key] !== $value) {
            return false;
        }
    }
    return true;
});

if (!empty($filteredNotebooks)) {
    foreach ($filteredNotebooks as $notebook) {
        echo "<div>";
        echo "Brand: " . $notebook['brand'] . "<br>";
        echo "CPU: " . $notebook['cpu'] . "<br>";
        echo "RAM Memory: " . $notebook['ram'] . "<br>";
        echo "HDD Capacity: " . $notebook['hdd'] . "<br>";
        echo "GPU: " . $notebook['gpu'] . "<br>";
        echo "</div><hr>";
    }
} else {
    echo "No results were found.";
}
?>
