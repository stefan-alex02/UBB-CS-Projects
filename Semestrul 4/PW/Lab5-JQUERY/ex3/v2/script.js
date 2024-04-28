let imageName = "fruit.png";
const imagePositions = {
    1: "0 0",
    2: "-38px 0",
    3: "-76px 0",
    4: "-114px 0",
    5: "-152px 0",
    6: "0 -38px",
    7: "-38px -38px",
    8: "-76px -38px",
    9: "-114px -38px",
    10: "-152px -38px",
    11: "0 -76px",
    12: "-38px -76px",
    13: "-76px -76px",
    14: "-114px -76px",
    15: "-152px -76px",
    16: "0 -114px",
    17: "-38px -114px",
    18: "-76px -114px",
};
const noOfImages = 18;
let selectedCards = [];
const duration = 0.5;

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
    array.sort((a, b) => 0.5 - Math.random());
}

function generateArray(c) {
    const pairs = [];
    for (let i = 1; i <= c / 2; i++) {
        pairs.push(i % noOfImages + 1);
    }
    shuffleArray(pairs);
    const numbers = pairs.concat(pairs);
    shuffleArray(numbers);

    return numbers;
}

function generate() {
    const input = $("#input").val();

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
    $("#container").hide();

    let numbers = generateArray(c);

    console.log(numbers);

    let n = biggestDivisorCloseToRoot(c);
    let m = c / n;

    let table = $("#table")[0];
    table.hidden = false;
    for (let i = 0; i < n; i++) {
        let row = $("<tr>").appendTo(table);
        for (let j = 0; j < m; j++) {
            row.append(
                $("<td>").append(
                    $("<span>")
                        .data("revealed", false)
                        .data("imageLabel", numbers[i * m + j])
                        .css("background-image", "url('" + imageName + "')")
                        .css("background-position", imagePositions[numbers[i * m + j]])
                        .addClass("card hidden")
                    )
                    .bind("flip", function() {
                        $(this)
                            .toggleClass("flipped")
                            .find('span')
                                .toggleClass("hidden")
                                .data("revealed", !$(this).find('span').data("revealed"));

                        if (!$(this).find('span').data("revealed")) {
                            $(this)
                                .css("animation", "flipping-down " + duration + "s ease-in-out")
                                .find('span')
                                .css("animation", "hiding " + duration + "s ease-out");
                        } else {
                            $(this)
                                .css("animation", "flipping-up " + duration + "s ease-in-out")
                                .find('span')
                                .css("animation", "revealing " + duration + "s ease-out");
                        }
                    })
                    .click(function() {
                        if (selectedCards.length < 2 && $(this).children().data("revealed") === false) {
                            selectedCards.push($(this));
                            $(this).trigger("flip");

                            if (selectedCards.length === 2) {
                                setTimeout(() => {
                                    if (selectedCards[0].children().data("imageLabel") ===
                                        selectedCards[1].children().data("imageLabel")) {
                                        $(selectedCards).each(function() {
                                            $(this).data("revealed", true)
                                                .css("animation", "")
                                                .addClass("matched")
                                                .data("disable", true);
                                        });
                                    } else {
                                        $(selectedCards).each(function() {
                                            $(this).trigger("flip");
                                        });
                                    }
                                    selectedCards = [];
                                }, duration * 1000 + 500);
                            }
                        }
                    })
            );
        }
    }
}
