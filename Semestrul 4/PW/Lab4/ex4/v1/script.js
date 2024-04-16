const table = document.getElementById('table');
table.sortedRowindex = null;
table.ascending = true;

function onClickHeader(event) {
    const index = this.parentNode.rowIndex;
    if (table.sortedRowindex === index) {
        table.ascending = !table.ascending;
        this.getElementsByClassName('arrow')[0].innerText = table.ascending ? '↑' : '↓';
    } else {
        if (table.sortedRowindex !== null) {
            table.rows[table.sortedRowindex].getElementsByClassName('arrow')[0].innerText = '';
        }
        this.getElementsByClassName('arrow')[0].innerText = '↑';

        table.ascending = true;
        table.sortedRowindex = index;
    }

    for (let i = 1; i < table.rows[index].cells.length - 1; i++) {
        for (let j = i + 1; j < table.rows[index].cells.length; j++) {
            let selectedRow = table.rows[index];
            let shouldSwap = false;

            if (table.ascending) {
                shouldSwap = Number(selectedRow.cells[i].innerText) > Number(selectedRow.cells[j].innerText);
            } else {
                shouldSwap = Number(selectedRow.cells[i].innerText) < Number(selectedRow.cells[j].innerText);
            }

            if (shouldSwap) {
                for (let k = 0; k < table.rows.length; k++) {
                    table.rows[k].insertBefore(table.rows[k].cells[j], table.rows[k].cells[i]);
                    table.rows[k].insertBefore(table.rows[k].cells[i+1], table.rows[k].cells[j]);
                }
            }
        }
    }
}

headers = document.getElementsByClassName('header');
for (let i = 0; i < headers.length; i++) {
    headers[i].addEventListener('click', onClickHeader);
}