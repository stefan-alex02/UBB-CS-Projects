function validate(event) {
    event.preventDefault();

    const inputs = document.getElementsByTagName('input');

    let errors = "";
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].classList.contains('string') &&
            inputs[i].value.length < 3) {
            inputs[i].classList.add('invalid');
            errors += 'String must be at least 3 characters long\n';
        }
        else if (inputs[i].classList.contains('number') &&
            isNaN(inputs[i].value)) {
            inputs[i].classList.add('invalid');
            errors += 'Value must be a number\n';
        }
        else if (inputs[i].classList.contains('email') &&
            !inputs[i].value.match(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/)) {
            inputs[i].classList.add('invalid');
            errors += 'Invalid email\n';
        }
        else if (inputs[i].classList.contains('age') &&
            (isNaN(inputs[i].value) || inputs[i].value < 18)) {
            inputs[i].classList.add('invalid');
            errors += 'Invalid age\n';
        }
        else if (inputs[i].classList.contains('date') &&
            isNaN(Date.parse(inputs[i].value))) {
            inputs[i].classList.add('invalid');
            errors += 'Invalid date\n';
        }
        else {
            inputs[i].classList.remove(['invalid']);
        }
    }

    if (errors !== "") {
        alert(errors);
        return false;
    }
}