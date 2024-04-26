function validate() {
    $('input').removeClass('invalid');

    let invalids = $('input.string')
        .filter(function() {
            return $(this).val().length < 3;
        })
        .addClass('invalid')
        .map(() => "string")
        .get().concat(
    $('input.number')
        .filter(function() {
            return isNaN($(this).val());
        })
        .addClass('invalid')
        .map(() => "number")
        .get()).concat(
    $('input.email')
        .filter(function() {
            return !$(this).val().match(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/);
        })
        .addClass('invalid')
        .map(() => 'email')
        .get()).concat(
    $('input.age')
        .filter(function() {
            return isNaN($(this).val()) || $(this).val() < 18;
        })
        .addClass('invalid')
        .map(() => 'age')
        .get()).concat(
    $('input.date')
        .filter(function() {
            return isNaN(Date.parse($(this).val()));
        })
        .addClass('invalid')
        .map(() => 'date')
        .get());

    if (invalids.length > 0) {
        setTimeout(() => {
            alert("Invalid fields: " + invalids.join(", "));
        }, 1);
        return false;
    }
    else {
        alert("All fields are valid");
        return true;
    }
}