const xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState === 4 && this.status === 200) {
        document.getElementById("dep-list").innerHTML = this.responseText;

        const depList = document.getElementById("dep-list");
        const depItems = depList.getElementsByTagName("li");
        for (let i = 0; i < depItems.length; i++) {
            depItems[i].addEventListener("click", onClickDeparture);
        }
    }
}

xhttp.open("GET", "departures.php", true);
xhttp.send();

function onClickDeparture() {
    const department = this.innerText;
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById("arv-list").innerHTML = this.responseText;
        }
    }

    xhttp.open("GET", "arrivals.php?d=" + department, true);
    xhttp.send();
}
