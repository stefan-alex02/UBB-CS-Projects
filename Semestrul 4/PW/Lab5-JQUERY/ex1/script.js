$('select')
    .each(function(index, select) {
        let selects = $('select');

        $(select)
            .data('nextSelect', selects.eq((index+1) % selects.length))
            .attr('size', String(Math.max(select.length, 2)));
    })
    .on('dblclick', 'option', function() {
        $(this).parent()
            .attr('size', Math.max($(this).parent().find('option').length - 1, 2))
            .data('nextSelect')
            .append($(this))
                .attr('size', Math.max($(this).parent().find('option').length, 2));
    });
