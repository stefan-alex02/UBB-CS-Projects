let tables = document.getElementsByTagName('table');
for (let i= 0; i < tables.length; i++) {
    tables[i].headers = [];
    tables[i].sortedRowindex = null;
    tables[i].ascending = true;

    // table.headers = document.getElementsByClassName('header');
    tables[i].headers = document.querySelectorAll(`#${tables[i].id} .header`);
    console.log(tables[i].headers);
    for (let j = 0; j < tables[i].headers.length; j++) {
        tables[i].headers[j].addEventListener('click', onClickHeader);
        tables[i].headers[j].parentTable = tables[i];
    }
}

function onClickHeader(event) {
    const index = this.parentNode.rowIndex;
    let table = this.parentTable;
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