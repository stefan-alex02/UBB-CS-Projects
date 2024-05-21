<?php
/*
 * Possible statuses:
 * 0: Game in progress
 * 1: Player wins
 * 2: Computer wins
 * 3: Draw
 */

function checkWin($cellValues, $mark) {
    for ($i = 0; $i < 3; $i++) {
        if ($cellValues[$i][0] == $mark && $cellValues[$i][1] == $mark && $cellValues[$i][2] == $mark ||
            $cellValues[0][$i] == $mark && $cellValues[1][$i] == $mark && $cellValues[2][$i] == $mark) {
            return true;
        }
    }
    if ($cellValues[0][0] == $mark && $cellValues[1][1] == $mark && $cellValues[2][2] == $mark ||
        $cellValues[0][2] == $mark && $cellValues[1][1] == $mark && $cellValues[2][0] == $mark) {
        return true;
    }
    return false;
}
function checkDraw($cellValues) {
    for ($i = 0; $i < 3; $i++) {
        for ($j = 0; $j < 3; $j++) {
            if ($cellValues[$i][$j] == '') {
                return false;
            }
        }
    }
    return true;
}
function findTwoInARow($cellValues, $mark) {
    for ($i = 0; $i < 3; $i++) {
        for ($j = 0; $j < 3; $j++) {
            if ($cellValues[$i][$j] == $mark) {
                for ($k = 0; $k < 3; $k++) {
                    if ($k != $j && $cellValues[$i][$k] == $mark && $cellValues[$i][3 - $j - $k] == '') {
                        return $i * 3 + (3 - $j - $k);
                    }
                    if ($k != $i && $cellValues[$k][$j] == $mark && $cellValues[3 - $i - $k][$j] == '') {
                        return (3 - $i - $k) * 3 + $j;
                    }
                }
                if ($i == $j || $i + $j == 2) {
                    if ($cellValues[3 - $i - 1][3 - $j - 1] == $mark && $cellValues[2 - $i][2 - $j] == '') {
                        return (2 - $i) * 3 + (2 - $j);
                    }
                    if ($cellValues[2 - $i][2 - $j] == $mark && $cellValues[3 - $i - 1][3 - $j - 1] == '') {
                        return (3 - $i - 1) * 3 + (3 - $j - 1);
                    }
                }
            }
        }
    }
    return -1;
}

$data = json_decode(file_get_contents('php://input'), true);
$turnNumber = $data['turn'];
$cellValues = $data['cells'];

if ($turnNumber == 1) {
    # First turn, the player or the computer starts first, randomly
    $playerStarts = rand(0, 1);
    if ($playerStarts == 0) {
        # Computer starts first, choose a random cell
        $cell = rand(0, 8);
        $turnNumber = 2;
        echo json_encode(array('turn' => $turnNumber,
            'row' => floor($cell / 3),
            'column' => $cell % 3,
            'status' => 0));
    } else {
        # Player starts first
        $turnNumber = 1;
        echo json_encode(array('turn' => $turnNumber, 'row' => -1, 'column' => -1, 'status' => 0));
    }
}
else {
    # Check if the player wins
    $playerMark = ($turnNumber-1) % 2 == 0 ? 'O' : 'X';
    $computerMark = $playerMark == 'O' ? 'X' : 'O';
    if (checkWin($cellValues, $playerMark)) {
        echo json_encode(array('turn' => $turnNumber, 'row' => -1, 'column' => -1, 'status' => 1));
        return;
    }
    else if (checkDraw($cellValues)) {
        echo json_encode(array('turn' => $turnNumber, 'row' => -1, 'column' => -1, 'status' => 3));
        return;
    }

    # Choose a cell in a smart way, by completing a row, column or diagonal
    $cell = findTwoInARow($cellValues, $computerMark);
    if ($cell == -1) {
        # If there is no row, column or diagonal to complete, try blocking the player
        $cell = findTwoInARow($cellValues, $playerMark);
    }
    if ($cell == -1) {
        # If there is no row, column or diagonal to block, choose a random cell
        do {
            $cell = rand(0, 8);
        } while ($cellValues[floor($cell / 3)][$cell % 3] != '');
    }

    $row = floor($cell / 3);
    $column = $cell % 3;
    $cellValues[$row][$column] = $computerMark;
    $turnNumber++;

    # Check if the computer wins
    if (checkWin($cellValues, $computerMark)) {
        echo json_encode(array('turn' => $turnNumber,
            'row' => $row,
            'column' => $column,
            'status' => 2));
        return;
    }
    if (checkDraw($cellValues)) {
        echo json_encode(array('turn' => $turnNumber, 'row' => $row, 'column' => $column, 'status' => 3));
    } else {
        echo json_encode(array('turn' => $turnNumber,
            'row' => $row,
            'column' => $column,
            'status' => 0));
    }
}
?>