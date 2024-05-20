$.get('departures.php', function (data, status) {
    if (status === 'success') {
        $('#dep-list').html(data).children().each(function () {
            $(this).on('click', function () {
                $('#arv-list').load('arrivals.php?d=' + encodeURIComponent($(this).text()));
            });
        });
    }
});
