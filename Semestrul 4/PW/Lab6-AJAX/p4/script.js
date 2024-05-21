function askComputerTurn() {
    turnNumber++;
    const cellValues = Array(3).fill().map(() => Array(3).fill('')); // Create a 3x3 2D array

    $("tr").each(function(rowIndex) {
        $(this).find("td").each(function(cellIndex) {
            const cellValue = $(this).text();
            cellValues[rowIndex][cellIndex] = cellValue ? cellValue : ''; // If the cell is empty, store an empty string
        });
    });

    const data = {
        turn: turnNumber,
        cells: cellValues
    };

    $.ajax({
        url: 'game.php',
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function(response) {
            console.log('Response: ' + response)

            const result = JSON.parse(response);
            const turn = result['turn'];
            const row = result['row'];
            const column = result['column'];
            const status = result['status'];

            console.log('Turn: ' + turn);

            // Update the game state based on the result
            // For example, if the computer made a move, update the corresponding cell
            if (row !== -1 && column !== -1) {
                const cellId = 'c' + (row * 3 + column);
                $('#' + cellId).text((turn-1) % 2 === 0 ? 'O' : 'X')
                    .removeClass('active').off('click');
            }

            // Update the turn number
            turnNumber = turn;

            // Check the game status and display a message if the game is over
            switch (status) {
                case 1:
                    $('#message')
                        .text('You win')
                        .addClass('win')
                        .css('display', 'inline-flex');
                    break;
                case 2:
                    $('#message')
                        .text('Computer wins')
                        .addClass('lose')
                        .css('display', 'inline-flex');
                    break;
                case 3:
                    $('#message')
                        .text('Draw')
                        .addClass('draw')
                        .css('display', 'inline-flex');
                    break;
            }
            if (status !== 0) {
                $('td').removeClass('active').off('click');
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log('Error: ' + textStatus);
        }
    });
}
function onClickCell() {
    $(this).text(turnNumber % 2 === 0 ? 'O' : 'X')
        .removeClass('active').off('click');
    // Temporarily disable all cells
    $('td').off('click');
    // Delay
    setTimeout(function(){
        askComputerTurn();
        $('td').on('click', onClickCell);
    }, 200);
}

turnNumber = 0;

$(function() { askComputerTurn(); });
$('td').on('click', onClickCell);
