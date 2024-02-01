
$(document).ready(function () {
    $('[data-toggle="table"]').bootstrapTable();
});

function EnableDisable(isbn) {
        //Reference the Button.
        const btnSubmit = document.getElementById("submit");
        //Verify the ISBN has 13 characters
        btnSubmit.disabled = isbn.value.trim().length !== 13
    }