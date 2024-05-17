function generateRandomNumbers(n) {
    // Step 1: Create an array of numbers from 1 to n^2
    let numbers = [];
    for (let i = 1; i <= n * n; i++) {
        numbers.push(i);
    }

    // Step 2: Shuffle the array
    numbers.sort((a, b) => 0.5 - Math.random());

    return numbers;
}

let hiddenDiv;
let n;

function generate() {
    const input = $("#input").val();

    if (input === "" || isNaN(input)) {
        alert("Please enter a valid number");
        return;
    }
    n = parseInt(input);

    let fileInput = $("#file-input");
    if (fileInput.val() === "") {
        alert("Please select a file");
        return;
    }
    let file = fileInput.prop('files')[0];

    $("#container").hide();
    $("#table").show().css("display", "inline-block");

    let numbers = generateRandomNumbers(n);

    let reader = new FileReader();
    reader.onload = function(e) {
        let img = new Image();
        img.onload = function() {
            console.log("Image loaded successfully");

            for (let i = 0; i < n; i++) {
                let row = $("<tr>").appendTo($("#table"));
                for (let j = 0; j < n; j++) {
                    let cell = $("<td>").appendTo(row);

                    cell.append(
                        $("<div>")
                            .data("pieceNumber", numbers[i * n + j])
                            .addClass("game-piece")
                            .each(function() {
                                let cellStyle = window.getComputedStyle(cell[0]);

                                let width = Math.floor(parseFloat(cellStyle.getPropertyValue("width")));
                                let height = Math.floor(parseFloat(cellStyle.getPropertyValue("height")));

                                let imgWidth = width * n;
                                let imgHeight = height * n;

                                let pieceRow = Math.floor(($(this).data("pieceNumber") - 1) / n);
                                let pieceCol = ($(this).data("pieceNumber") - 1) % n;

                                $(this)
                                    .css("background-image", `url(${e.target.result})`)
                                    .css("background-size", `${imgWidth}px ${imgHeight}px`)
                                    .css("background-position",
                                        `-${pieceCol * Math.floor(imgWidth / n)}px -${pieceRow * Math.floor(imgHeight / n)}px`);
                            })
                            .on("click", function() {
                                console.log("Clicked on piece");
                                let row = $(this).parent().parent().index();
                                let col = $(this).parent().index();

                                let hiddenRow = hiddenDiv.parent().parent().index();
                                let hiddenCol = hiddenDiv.parent().index();

                                let distance, direction;
                                if (col === hiddenCol) {
                                    distance = Math.abs(row - hiddenRow);
                                    direction = row > hiddenRow ? [1, 0] : [-1, 0];
                                } else if (row === hiddenRow) {
                                    distance = Math.abs(col - hiddenCol);
                                    direction = col > hiddenCol ? [0, 1] : [0, -1];
                                }

                                for (let i = 0; i < distance; i++) {
                                    moveNeighbor(...direction);
                                }
                            }
                        )
                    );
                }
            }

            let hiddenNumber = Math.floor(Math.random() * n * n) + 1;
            hiddenDiv = $("#table").children().eq(Math.floor((hiddenNumber - 1) / n))
                .children().eq((hiddenNumber - 1) % n).children().eq(0).hide();
        };
        img.onerror = function() {
            console.log("Error loading image");
        };
        img.src = e.target.result;
    };
    reader.readAsDataURL(file);
}

function checkWin() {
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            if ($("#table").children().eq(i).children().eq(j).children().eq(0)
                .data("pieceNumber") !== i * n + j + 1) {
                return;
            }
        }
    }
    hiddenDiv.show();

    window.removeEventListener('keydown', checkForKeyPress);

    $("#table").find("td div").off('click');

    setTimeout(() => {
        alert("You win!");

        $("#table")
            .find("td").addClass("win-cell")
            .find("div").removeClass("game-piece").addClass("win-piece");
    }, 400);
}

const moveClasses = ['move-down', 'move-right', null, 'move-left', 'move-up'];

function moveNeighbor(rowDif, colDif) {
    let row = hiddenDiv.parent().parent().index();
    let col = hiddenDiv.parent().index();

    if (rowDif > 0 && row === $("#table").find('tr').length - 1 ||
        rowDif < 0 && row === 0 ||
        colDif > 0 && col === $("#table").find('tr').eq(0).find('td').length - 1 ||
        colDif < 0 && col === 0) {
        return;
    }

    let neighborDiv = $("#table").find(`tr:eq(${row + rowDif}) td:eq(${col + colDif}) div`);

    hiddenDiv.detach();
    neighborDiv.detach();

    $("#table").find(`tr:eq(${row}) td:eq(${col})`).append(neighborDiv);
    $("#table").find(`tr:eq(${row + rowDif}) td:eq(${col + colDif})`).append(hiddenDiv);

    neighborDiv.addClass(moveClasses[2 + rowDif * 2 + colDif]);
    setTimeout(() => {
        neighborDiv.removeClass(moveClasses[2 + rowDif * 2 + colDif]);
    }, 1);

    checkWin();
}

function checkForKeyPress(event) {
    if ($("#table").css("display") === "none"){
        return;
    }
    switch (event.key) {
        case 'ArrowUp':
        case 'w':
            moveNeighbor(1, 0);
            break;
        case 'ArrowDown':
        case 's':
            moveNeighbor(-1, 0);
            break;
        case 'ArrowLeft':
        case 'a':
            moveNeighbor(0, 1);
            break;
        case 'ArrowRight':
        case 'd':
            moveNeighbor(0, -1);
            break;
    }
}

window.addEventListener('keydown', checkForKeyPress);
