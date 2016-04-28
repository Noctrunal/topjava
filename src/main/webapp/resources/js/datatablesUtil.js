const USERS = 'ajax/admin/users/';
const MEALS = 'ajax/profile/meals/';

function makeEditable() {
    $('#add').click(function () {
        $('#id').val(0);
        $('#editRow').modal();
    });

    $('.delete').click(function () {
        deleteRow($(this).closest('tr').attr('id'));
    });

    $('#detailsForm').submit(function () {
        save();
        return false;
    });
    
    $('#filter').submit(function () {
        updateMealsTable();
        return false;
    });

    $('.checkbox').change(function () {
        var checkbox = $(this);
        var id = $(this).closest('tr').attr('id');
        enabled(checkbox, id);
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(event, jqXHR, options, jsExc);
    });
}

function enabled(checkbox, id) {
    var enabled = checkbox.is(":checked");
    checkbox.closest('tr').css("text-decoration", enabled ? "none" : "line-through");
    debugger;
    $.ajax({
        url: ajaxUrl + id,
        type: 'POST',
        data: 'enabled=' + enabled,
        success: function () {
            successNoty(enabled ? 'Enabled' : 'Disabled');
        }
    });
}

function updateTableByData(data) {
    datatableApi.fnClearTable();
    datatableApi.fnAddData(data);
    datatableApi.fnDraw();
}

function updateUsersTable() {
    debugger;
    $.get(ajaxUrl, function(data) {
        updateTableByData(data);
    })
}

function updateMealsTable() {
    debugger;
    $.ajax({
        type: "POST",
        url: ajaxUrl + 'filter',
        data: $('#filter').serialize(),
        success: function (data) {
            updateTableByData(data);
            successNoty('Filtered')
        }
    });
    return false;
}

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: 'DELETE',
        success: function () {
            if (ajaxUrl === USERS) {
                updateUsersTable();
            } else if (ajaxUrl === MEALS) {
                updateMealsTable();
            }
            successNoty('Deleted');
        }
    });
}

function save() {
    var form = $('#detailsForm');
    debugger;
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $('#editRow').modal('hide');
            if (ajaxUrl === USERS) {
                updateUsersTable();
            } else if (ajaxUrl === MEALS) {
                updateMealsTable();
            }
            successNoty('Saved');
        }
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    noty({
        text: text,
        type: 'success',
        layout: 'bottomRight',
        timeout: true
    });
}

function failNoty(event, jqXHR, options, jsExc) {
    closeNoty();
    failedNote = noty({
        text: 'Failed: ' + jqXHR.statusText + "<br>",
        type: 'error',
        layout: 'bottomRight'
    });
}
