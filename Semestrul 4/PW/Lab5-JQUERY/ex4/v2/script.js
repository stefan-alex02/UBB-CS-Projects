const table = document.getElementById('table');
table.sortedColIndex = null;
table.ascending = true;

function onClickHeader(event) {
    const index = this.cellIndex;
    if (table.sortedColIndex === index) {
        table.ascending = !table.ascending;
        this.getElementsByClassName('arrow')[0].innerText = table.ascending ? '↑' : '↓';
    } else {
        if (table.sortedColIndex !== null) {
            table.rows[0].cells[table.sortedColIndex].getElementsByClassName('arrow')[0].innerText = '';
        }
        this.getElementsByClassName('arrow')[0].innerText = '↑';

        table.ascending = true;
        table.sortedColIndex = index;
    }

    for (let i = 1; i < table.rows.length - 1; i++) {
        for (let j = i + 1; j < table.rows.length; j++) {
            let shouldSwap = false;

            if (table.ascending) {
                shouldSwap = Number(table.rows[i].cells[index].innerText) >
                    Number(table.rows[j].cells[index].innerText);
            } else {
                shouldSwap = Number(table.rows[i].cells[index].innerText) <
                    Number(table.rows[j].cells[index].innerText);
            }

            if (shouldSwap) {
                let parent = table.rows[i].parentNode;
                parent.insertBefore(table.rows[j], table.rows[i]);
                parent.insertBefore(table.rows[i+1], table.rows[j]);
            }
        }
    }
}

headers = document.getElementsByClassName('header');
for (let i = 0; i < headers.length; i++) {
    headers[i].addEventListener('click', onClickHeader);
}