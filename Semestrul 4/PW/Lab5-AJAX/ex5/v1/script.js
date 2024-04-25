function generateRandomNumbers(n) {
    // Step 1: Create an array of numbers from 1 to n^2
    let numbers = [];
    for (let i = 1; i <= n * n; i++) {
        numbers.push(i);
    }

    // Step 2: Shuffle the array
    shuffleArray(numbers);

    return numbers;
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}

let table = document.getElementById("table");
let hiddenDiv;
let n;

function generate() {
    var input = document.getElementById("input").value;

    if (input === "") {
        alert("Please enter a number");
        return;
    }
    if (isNaN(input)) {
        alert("Please enter a valid number");
        return;
    }
    n = parseInt(input);

    document.getElementById("container").hidden = true;

    table.hidden = false;

    let numbers = generateRandomNumbers(n);

    for (let i = 0; i < n; i++) {
        let row = table.insertRow();
        for (let j = 0; j < n; j++) {
            let cell = row.insertCell();

            let div = document.createElement("div");
            div.innerHTML = numbers[i * n + j];
            div.classList.add("game-piece");

            cell.appendChild(div);

            div.onclick = function() {
                let row = div.parentElement.parentElement.rowIndex;
                let col = div.parentElement.cellIndex;

                if (col === hiddenDiv.col) {
                    let distance = Math.abs(row - hiddenDiv.row);
                    for (let i = 0; i < distance; i++) {
                        if (row > hiddenDiv.row) {
                            moveUp();
                        } else {
                            moveDown();
                        }
                    }
                    checkWin();
                } else if (row === hiddenDiv.row) {
                    let distance = Math.abs(col - hiddenDiv.col);
                    for (let i = 0; i < distance; i++) {
                        if (col > hiddenDiv.col) {
                            moveLeft();
                        } else {
                            moveRight();
                        }
                    }
                    checkWin();
                }
            };
        }
    }

    let hiddenNumber = Math.floor(Math.random() * n * n) + 1;
    console.log(hiddenNumber);
    hiddenDiv = table.rows[Math.floor((hiddenNumber - 1) / n)]
        .cells[(hiddenNumber - 1) % n]
        .firstChild;
    hiddenDiv.style.display = 'none';
    hiddenDiv.row = hiddenDiv.parentElement.parentElement.rowIndex;
    hiddenDiv.col = hiddenDiv.parentElement.cellIndex;
}

function checkWin() {
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            let div = table.rows[i].cells[j].firstChild;
            if (Number(div.innerHTML) !== i * n + j + 1) {
                return;
            }
        }
    }
    hiddenDiv.style.display = 'flex';

    window.removeEventListener('keydown', checkForKeyPress);

    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            table.rows[i].cells[j].firstChild.onclick = null;
        }
    }

    setTimeout(() => {
        alert("You win!");

        for (let i = 0; i < n; i++) {
            for (let j = 0; j < n; j++) {
                let cell = table.rows[i].cells[j];
                cell.firstChild.classList.remove("game-piece");
                cell.firstChild.classList.add("win-piece");

                cell.classList.add("win-cell");
            }
        }
    }, 400);
}

function moveUp() {
    let row = hiddenDiv.row;
    let col = hiddenDiv.col;

    if (row === table.rows.length - 1) {
        return;
    }

    let belowDiv = table.rows[row + 1].cells[col].firstChild;

    table.rows[row].cells[col].removeChild(hiddenDiv);
    table.rows[row + 1].cells[col].removeChild(belowDiv);

    table.rows[row].cells[col].appendChild(belowDiv);
    table.rows[row + 1].cells[col].appendChild(hiddenDiv);

    hiddenDiv.row = row + 1;

    belowDiv.classList.add('move-up');
    setTimeout(() => {
        belowDiv.classList.remove('move-up');
    }, 1);
}

function moveDown() {
    let row = hiddenDiv.row;
    let col = hiddenDiv.col;

    if (row === 0) {
        return;
    }

    let aboveDiv = table.rows[row - 1].cells[col].firstChild;

    table.rows[row].cells[col].removeChild(hiddenDiv);
    table.rows[row - 1].cells[col].removeChild(aboveDiv);

    table.rows[row].cells[col].appendChild(aboveDiv);
    table.rows[row - 1].cells[col].appendChild(hiddenDiv);

    hiddenDiv.row = row - 1;

    aboveDiv.classList.add('move-down');
    setTimeout(() => {
        aboveDiv.classList.remove('move-down');
    }, 1);
}

function moveLeft() {
    let row = hiddenDiv.row;
    let col = hiddenDiv.col;

    if (col === table.rows[0].cells.length - 1) {
        return;
    }

    let rightDiv = table.rows[row].cells[col + 1].firstChild;

    table.rows[row].cells[col].removeChild(hiddenDiv);
    table.rows[row].cells[col + 1].removeChild(rightDiv);

    table.rows[row].cells[col].appendChild(rightDiv);
    table.rows[row].cells[col + 1].appendChild(hiddenDiv);

    hiddenDiv.col = col + 1;

    rightDiv.classList.add('move-left');
    setTimeout(() => {
        rightDiv.classList.remove('move-left');
    }, 1);
}

function moveRight() {
    let row = hiddenDiv.row;
    let col = hiddenDiv.col;

    if (col === 0) {
        return;
    }

    let leftDiv = table.rows[row].cells[col - 1].firstChild;

    table.rows[row].cells[col].removeChild(hiddenDiv);
    table.rows[row].cells[col - 1].removeChild(leftDiv);

    table.rows[row].cells[col].appendChild(leftDiv);
    table.rows[row].cells[col - 1].appendChild(hiddenDiv);

    hiddenDiv.col = col - 1;

    leftDiv.classList.add('move-right');
    setTimeout(() => {
        leftDiv.classList.remove('move-right');
    }, 1);
}

function checkForKeyPress(event) {
    if (table.hidden) {
        return;
    }
    switch (event.key) {
        case 'ArrowUp':
        case 'w':
            moveUp();
            checkWin();
            break;
        case 'ArrowDown':
        case 's':
            moveDown();
            checkWin();
            break;
        case 'ArrowLeft':
        case 'a':
            moveLeft();
            checkWin();
            break;
        case 'ArrowRight':
        case 'd':
            moveRight();
            checkWin();
            break;
    }
}

window.addEventListener('keydown', checkForKeyPress);