function validate() {
    const inputs = document.getElementsByTagName('input');

    let errors = [];
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].classList.contains('string') &&
            inputs[i].value.length < 3) {
            inputs[i].classList.add('invalid');
            errors.push(inputs[i].name);
        }
        else if (inputs[i].classList.contains('number') &&
            isNaN(inputs[i].value)) {
            inputs[i].classList.add('invalid');
            errors.push(inputs[i].name);
        }
        else if (inputs[i].classList.contains('email') &&
            !inputs[i].value.match(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/)) {
            inputs[i].classList.add('invalid');
            errors.push(inputs[i].name);
        }
        else if (inputs[i].classList.contains('age') &&
            (isNaN(inputs[i].value) || inputs[i].value < 18)) {
            inputs[i].classList.add('invalid');
            errors.push(inputs[i].name);
        }
        else if (inputs[i].classList.contains('date') &&
            isNaN(Date.parse(inputs[i].value))) {
            inputs[i].classList.add('invalid');
            errors.push(inputs[i].name);
        }
        else {
            inputs[i].classList.remove(['invalid']);
        }
    }

    if (errors.length > 0) {
        let errorText = "Câmpurile ";
        for (let i = 0; i < errors.length; i++) {
            errorText += errors[i];
            if (i < errors.length - 2) {
                errorText += ", ";
            }
            else if (i < errors.length - 1) {
                errorText += " și ";
            }
        }
        errorText += " nu sunt completate corect!";
        setTimeout(() => {
            alert(errorText);
        }, 1);
        return false;
    }
    else {
        alert('Datele sunt completate corect');
        return true;
    }
}