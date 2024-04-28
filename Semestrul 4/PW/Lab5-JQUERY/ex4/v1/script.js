$('table').each((_, table) => {
    $(table)
        .data('sortingMode', 0)
        .find('.header')
        .click(function() {
            const index = $(this).parent().index();
            if (Math.abs($(table).data('sortingMode')) === index) {
                $(table).data('sortingMode', -$(table).data('sortingMode'));
                $(this).find('.arrow')
                    .text($(table)
                    .data('sortingMode') > 0 ? '↑' : '↓');
            }
            else {
                if ($(table).data('sortingMode') !== 0) {
                    $(table).find('.header')
                        .eq(Math.abs($(table).data('sortingMode')))
                        .find('.arrow').text('');
                }
                $(this).find('.arrow').text('↑');

                $(table).data('sortingMode', index);
            }

            $(table).find('tr').eq(index).children().slice(1).sort((a, b) => {
                const swap =  Number($(a).text()) > Number($(b).text()) ===
                    ($(table).data('sortingMode') > 0);

                if (swap) {
                    $(table).find('tr').each((_, row) => {
                        const ib = $(b).index();
                        $(row).children().eq(ib).before(
                            $(row).children().eq($(a).index())
                        );
                        $(row).children().eq($(a).index()).before(
                            $(row).children().eq(ib)
                        );
                    });
                }
                return swap;
            }).appendTo($(table).find('tr').eq(index));
        });
});
