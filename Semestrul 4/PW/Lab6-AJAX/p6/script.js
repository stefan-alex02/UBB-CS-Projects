$(function(){
    $('#filterButton').on('click', function(){
        $.ajax({
            url: 'filter.php',
            type: 'GET',
            data: $('#filterForm').serialize(),
            success: function(data){
                $('#results').html(data);
            }
        });
    });
});