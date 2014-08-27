$(document).ready(function () {
    // Load the classic theme
    Galleria.loadTheme('javascripts/galleria.classic.min.js');

    // Initialize Galleria
    Galleria.run('#galleria');
});

function loadAPI(e) {
    $.ajax({
        url: "api.html",
        success: function (data) {
            $content = $(".apisection");
            $content.empty();
            $content.append(data);

            $(".messic-header2").addClass("messic-header2-min");
        },
    });
}