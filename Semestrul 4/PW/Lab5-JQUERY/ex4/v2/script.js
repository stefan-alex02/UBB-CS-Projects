$('table').each((_, table) => {
    $(table)
        .data('sortingMode', 0)
        .find('.header')
        .click(function() {
            const index = $(this).index();
            if (Math.abs($(table).data('sortingMode')) === index) {
                $(table).data('sortingMode', -$(table).data('sortingMode'));
                $(this).find('.arrow')
                    .text($(table)
                        .data('sortingMode') > 0 ? '↑' : '↓');
            }
            else {
                if ($(table).data('sortingMode') !== 0) {
                    $(table).find('.header')
                        .eq(Math.abs($(table).data('sortingMode')) - 1)
                        .find('.arrow').text('');
                }
                $(this).find('.arrow').text('↑');

                $(table).data('sortingMode', index);
            }

            $(table).find('tr').slice(1).sort((a, b) => {
                return Number($(a).children().eq(index).text()) > Number($(b).children().eq(index).text()) ===
                    ($(table).data('sortingMode') > 0);
            }).appendTo(table);
        });
});
