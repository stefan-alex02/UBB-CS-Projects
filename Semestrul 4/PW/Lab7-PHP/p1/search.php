<link rel="stylesheet" type="text/css" href="styles.css">
<h4>Search Results:</h4>
<?php
$conn = null;
include '../db_config.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $departure_city = $_POST['departure_city'];
    $arrival_city = $_POST['arrival_city'];
    $direct_only = isset($_POST['direct_only']) ? true : false;

    // Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);

    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    if ($direct_only) {
        $sql = "SELECT 
                    train_number as 'train_number_1', 
                    train_type as 'train_type_1', 
                    departure_city as 'departure_city_1', 
                    arrival_city as 'arrival_city_1', 
                    departure_time as 'departure_time_1', 
                    arrival_time as 'arrival_time_1',
                    '' as 'train_number_2',
                    '' as 'train_type_2',
                    '' as 'departure_city_2',
                    '' as 'arrival_city_2',
                    '' as 'departure_time_2',
                    '' as 'arrival_time_2'
                FROM web7_trains 
                WHERE departure_city = ? AND arrival_city = ?";
    } else {
        $sql = "SELECT 
                    t1.train_number as 'train_number_1', 
                    t1.train_type as 'train_type_1', 
                    t1.departure_city as 'departure_city_1', 
                    t1.arrival_city as 'arrival_city_1', 
                    t1.departure_time as 'departure_time_1', 
                    t1.arrival_time as 'arrival_time_1',
                    t2.train_number as 'train_number_2',
                    t2.train_type as 'train_type_2',
                    t2.departure_city as 'departure_city_2',
                    t2.arrival_city as 'arrival_city_2',
                    t2.departure_time as 'departure_time_2',
                    t2.arrival_time as 'arrival_time_2'
                FROM web7_trains t1
                INNER JOIN web7_trains t2 ON t1.arrival_city = t2.departure_city
                WHERE t1.departure_city = ? AND t2.arrival_city = ?
                UNION
                SELECT 
                    train_number as 'train_number_1', 
                    train_type as 'train_type_1', 
                    departure_city as 'departure_city_1', 
                    arrival_city as 'arrival_city_1', 
                    departure_time as 'departure_time_1', 
                    arrival_time as 'arrival_time_1',
                    '' as 'train_number_2',
                    '' as 'train_type_2',
                    '' as 'departure_city_2',
                    '' as 'arrival_city_2',
                    '' as 'departure_time_2',
                    '' as 'arrival_time_2'
                FROM web7_trains 
                WHERE departure_city = ? AND arrival_city = ?";    
    }

    $stmt = $conn->prepare($sql);

    // Check for preparation errors
    if (!$stmt) {
        die("Preparation failed: (" . $conn->errno . ") " . $conn->error);
    }

    // Bind parameters
    if ($direct_only) {
        $stmt->bind_param("ss", $departure_city, $arrival_city);
    }
    else {
        $stmt->bind_param("ssss", $departure_city, $arrival_city, $departure_city, $arrival_city);
    }

    // Check for parameter binding errors
    if ($stmt->errno) {
        die("Parameter binding failed: (" . $stmt->errno . ") " . $stmt->error);
    }

    $stmt->execute();

    // Check for execution errors
    if ($stmt->errno) {
        die("Execution failed: (" . $stmt->errno . ") " . $stmt->error);
    }

    $result = $stmt->get_result();

    // Fetch and display results
    if ($result->num_rows > 0) {
                echo "<table border='1'>";
        echo "<tr>
                <th>Train Number 1</th>
                <th>Train Type 1</th>
                <th>Departure City 1</th>
                <th>Arrival City 1</th>
                <th>Departure Time 1</th>
                <th>Arrival Time 1</th>
                <th>Train Number 2</th>
                <th>Train Type 2</th>
                <th>Departure City 2</th>
                <th>Arrival City 2</th>
                <th>Departure Time 2</th>
                <th>Arrival Time 2</th>
              </tr>";
        while ($row = $result->fetch_assoc()) {
            echo "<tr>
                    <td>{$row['train_number_1']}</td>
                    <td>{$row['train_type_1']}</td>
                    <td>{$row['departure_city_1']}</td>
                    <td>{$row['arrival_city_1']}</td>
                    <td>{$row['departure_time_1']}</td>
                    <td>{$row['arrival_time_1']}</td>
                    <td>{$row['train_number_2']}</td>
                    <td>{$row['train_type_2']}</td>
                    <td>{$row['departure_city_2']}</td>
                    <td>{$row['arrival_city_2']}</td>
                    <td>{$row['departure_time_2']}</td>
                    <td>{$row['arrival_time_2']}</td>
                  </tr>";
        }
        echo "</table>";
    } else {
        echo "No trains found.";
    }

    $stmt->close();
    $conn->close();
} else {
    echo "Invalid request method.";
}?>
<br>
<a href="index.html">Back to search</a>