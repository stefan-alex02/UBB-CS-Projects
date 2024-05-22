function createNode(name, isFile, path) {
    let li = $('<li></li>').append(
        $('<span></span>').text(name))
        .data('path', path);

    if (isFile) {
        li.addClass('file').on('click', function(event) {
            event.stopPropagation();
            loadFileContent(path);
        });
    } else {
        li.addClass('directory closed').on('click', function(event) {
            event.stopPropagation();
            toggleDirectory($(this));
        });
    }
    return li;
}

function toggleDirectory(li) {
    const path = li.data('path');

    if (li.hasClass('loaded')) {
        const ul = li.find('ul');
        if (ul.css('display') === 'none') {
            ul.css('display', 'block');
            li.removeClass('closed').addClass('opened');
        } else {
            ul.css('display', 'none');
            li.removeClass('opened').addClass('closed');
        }
    } else {
        loadDirectoryContent(path, li);
    }
}

function loadDirectoryContent(path, li) {
    $.get(`load_directory.php?path=${encodeURIComponent(path)}`, function(response) {
        const ul = $('<ul></ul>');
        const items = JSON.parse(response);
        items.forEach(item => {
            const node = createNode(item.name, item.isFile, item.path);
            ul.append(node);
        });
        li.append(ul);
        li.addClass('loaded').removeClass('closed').addClass('opened');
    });
}

function loadFileContent(path) {
    $.get(`load_file.php?path=${encodeURIComponent(path)}`, function(content) {
        $('#file-content').text(content);
    });
}

// Initial load
loadDirectoryContent('../', $('#tree-view'));
