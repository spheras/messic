function loadAPI(e) {
    e.preventDefault();
    e.stopPropagation();
    $.ajax({
        url: "api.html",
        success: function (data) {
            $content = $("#content-wrapper");
            $content.empty();
            $content.append(data);

            $(".messic-header2").addClass("messic-header2-min");
        },
    });
}