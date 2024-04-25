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

let selectedCards = [];
const duration = 0.5;

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
            span.revealed = false;
            span.flip = function() {
                this.classList.toggle("hidden");
                this.revealed = !this.revealed;
                if (!this.revealed) {
                    this.style.animation = "hiding " + duration + "s ease-out";
                } else {
                    this.style.animation = "revealing " + duration + "s ease-out";
                }
            };
            cell.flip = function() {
                this.firstChild.flip();

                if (!this.firstChild.revealed) {
                    this.style.animation = "flipping-down " + duration + "s ease-in-out";
                }
                else {
                    this.style.animation = "flipping-up " + duration + "s ease-in-out";
                }
                cell.classList.toggle("flipped");

            };

            span.innerText = numbers[i * m + j];

            span.classList.add("card", "hidden");
            cell.appendChild(span);
            cell.addEventListener("click", function() {
                console.log("click");
                if (selectedCards.length < 2 && span.revealed === false) {
                    selectedCards.push(cell);

                    cell.flip();

                    if (selectedCards.length === 2) {
                        setTimeout(() => {
                            if (selectedCards[0].innerText === selectedCards[1].innerText) {
                                selectedCards[0].style.animation = "";
                                selectedCards[1].style.animation = "";
                                selectedCards[0].classList.add("matched");
                                selectedCards[1].classList.add("matched");

                                selectedCards[0].disable = true;
                                selectedCards[1].disable = true;
                            } else {
                                selectedCards[0].flip();
                                selectedCards[1].flip();
                            }
                            selectedCards = [];
                            }, duration * 1000 + 500);
                    }
                }
            });
        }
    }

    console.log(table);
}
