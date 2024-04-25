$('select').each(function(index, select) {
    let selects = $('select');

    $(select).data('nextSelect', selects.eq((index+1) % selects.length))
        .attr('size', String(Math.max(select.length, 2)));

    console.log($(select).data('nextSelect'));

    $(select).on('dblclick', 'option', function() {
        $(this).parent()
            .data('nextSelect').append($(this))
            .attr('size', String(Math.max($(this).parent().length, 2)))
            .data('nextSelect')
                .attr('size', String(Math.max($(this).parent().data('nextSelect').length, 2)));

        // select.size = Math.max(select.length, 2);
        // $(select).data('nextSelect').size = Math.max(select.nextSelect.length, 2);
    });
});
