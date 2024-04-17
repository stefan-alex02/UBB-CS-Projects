function biggestDivisorCloseToRoot(n) {
    let divisor = 2;
    for (let i = 2; i * i <= n; i++) {
        if (n % i === 0) {
            divisor = i;
        }
    }
    return divisor;
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
}



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
    let c = parseInt(input);
    if (c % 2 === 1) {
        alert("Please enter an even number");
        return;
    }
    document.getElementById("container").hidden = true;

    const pairs = [];
    for (let i = 1; i <= c / 2; i++) {
        pairs.push(i);
    }
    shuffleArray(pairs);

    const numbers = pairs.concat(pairs);

    shuffleArray(numbers);

    console.log(numbers);

    let n = biggestDivisorCloseToRoot(c);
    let m = c / n;

    let table = document.getElementById("table");
    table.hidden = false;
    for (let i = 0; i < n; i++) {
        let row = table.insertRow();
        for (let j = 0; j < m; j++) {
            let cell = row.insertCell();

            let span = document.createElement("span");
            span.innerText = numbers[i * m + j];

            span.classList.add("hidden-text");
            cell.appendChild(span);
            cell.addEventListener("click", function() {
                span.classList.toggle("flipped");
            });
        }
    }

    console.log(table);
}