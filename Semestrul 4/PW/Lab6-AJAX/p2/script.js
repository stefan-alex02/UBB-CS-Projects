let page = 1;
let pageSize = 3;

function loadTable() {
    $.ajax({
        url: 'table-page.php',
        type: 'GET',
        data: {
            page: page,
            pageSize: pageSize
        },
        dataType: 'xml',
        success: function(data) {
            // Clear the table body
            $('#table-body').empty();

            // Append new rows to the table
            $(data).find('user').each(function() {
                let firstName = $(this).find('first_name').text();
                let lastName = $(this).find('last_name').text();
                let phone = $(this).find('phone').text();
                let email = $(this).find('email').text();

                let row = `<tr><td>${firstName}</td><td>${lastName}</td><td>${phone}</td><td>${email}</td></tr>`;
                $('#table-body').append(row);
            });

            // Enable or disable the prev and next buttons based on the prev-on and next-on values
            $('#prev').prop('disabled', $(data).find('prev-on').text() === '0');
            $('#next').prop('disabled', $(data).find('next-on').text() === '0');
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown);
        }
    });
}

$('#next').on('click', function() {
    console.log('next');
    page++;
    loadTable();
});

$('#prev').on('click', function() {
    page--;
    loadTable();
});

loadTable();