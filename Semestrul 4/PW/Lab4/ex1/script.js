selects = document.getElementsByTagName("select");
console.log(selects);
for (let i = 0; i < selects.length; i++) {
    console.log(selects[i]);

    selects[i].nextList = selects[(i+1)%selects.length];
    updateSize(selects[i]);
    for (let option of selects[i].options) {
            option.ondblclick = function() {
                parent = this.parentElement;
                nextParent = parent.nextList;

                parent.removeChild(this);
                nextParent.appendChild(this);

                updateSize(parent);
                updateSize(nextParent);
            };
    }
}

function updateSize(select) {
    select.size = Math.max(select.length, 2);
}
