$(document).ready(function () {
    // Load the classic theme
    Galleria.loadTheme('javascripts/galleria.classic.min.js');

    // Initialize Galleria
    Galleria.run('#galleria');

    loadAPI();
});

function loadAPI() {
    $.ajax({
        url: "api.html",
        success: function (data) {
            $content = $(".apisection");
            $content.empty();
            $content.append(data);

            //$(".messic-header2").addClass("messic-header2-min");
        },
    });
}

/**
* Función de Google Analytics que realiza un seguimiento de un clic en un enlace externo.
* Esta función toma una cadena de URL válida como argumento y la utiliza
* como la etiqueta del evento.
*/
var trackOutboundLink = function(url) {
   ga('send', 'event', 'outbound', 'click', url, {'hitCallback':
     function () {
     document.location = url;
     }
   });
}
