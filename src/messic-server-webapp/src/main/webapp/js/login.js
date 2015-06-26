/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the
 *  JavaScript code in this page.
 *
 * The JavaScript code in this page is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License (GNU GPL) as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option)
 * any later version.  The code is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
 *
 * As additional permission under GNU GPL version 3 section 7, you
 * may distribute non-source (e.g., minimized or compacted) forms of
 * that code without the copy of the GNU GPL normally required by
 * section 4, provided you include this license notice and a URL
 * through which recipients can access the Corresponding Source.
 *
 * @licend  The above is the entire license notice
 * for the JavaScript code in this page.
 *
 */
/* messic token for petitions */
var VAR_MessicToken;

function loginSucessfull(messic_token) {
    VAR_MessicToken = messic_token;
    $.ajaxSetup({
        cache: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader('messic_token', messic_token);
        },
        error: function (e) {
            UtilCheckMessic(e);
        }
    });

    var $browsehappy = $(".browsehappy");
    if ($browsehappy.length > 0) {
        $browsehappy.fadeOut().remove();
    }

    $.ajax({
        type: "GET",
        url: "main.do",
        success: function (data) {

            $("#messic-logo1").attr("class", "messic-main");
            $("#messic-logo2").attr("class", "messic-main");

            //removing android note
            $("#messic-login-android-container").fadeOut(1000, function () {
                $(this).remove();
            });

            //let's hide and remove the login window!
            $("#messic-login-window").fadeOut(1000, function () {
                $(this).remove();
                var posts = $($.parseHTML(data)).filter('#content').children();
                $("body").append(posts);
                initMessic();
            });
        },
        error: function (data) {
            //if there is an error, then we should remove the current token to allow create a new one
            VAR_MessicToken = "";
            $.ajaxSetup({
                beforeSend: null,
            });
            UtilCreateCookie("messic_login_cookie", "");
        }
    });

}

$(document).ready(function () {
    var loginWindow = $("#messic-login-window");

    //make the window dragablle
    loginWindow.draggable();

    //centering the loginWindow
    jQuery.fn.center = function () {
        this.css("position", "absolute");
        this.css("top", ($(document).height() - this.height()) / 2 + $(loginWindow).scrollTop() + "px");
        this.css("left", ($(document).width() - this.width()) / 2 + $(loginWindow).scrollLeft() + "px");
    }
    loginWindow.center();

    loginWindow.show();
    loginWindow.center();

    $('input[autofocus]').trigger('focus');

    $("#username").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#password").focus();
        }
    });
    $("#password").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#messic-login-button").click();
        }
    });
    $("#messic_login_rememberme").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#messic-login-button").click();
        }
    });


    //add the login button functionality
    $("#messic-login-button").click(function () {
        info = $("#messic-login-form").serialize();
        $.ajax({
            type: "POST",
            url: "messiclogin",
            async: false,
            data: info, // serializes the form's elements.
            dataType: "json",
            success: function (data) {
                if (data.success == true) {
                    if ($("#messic_login_rememberme").is(':checked')) {
                        UtilCreateCookie("messic_login_cookie", data.messic_token, 30);
                    }
                    loginSucessfull(data.messic_token);
                } else {
                    //error, shake!!
                    $("#messic-login-window").parent().effect("shake");
                }
            }
        });
    });

    //show the about page on logo1 click
    $("#messic-logo1").click(function () {
        $.get("about.do", function (data) {
            var posts = $($.parseHTML(data)).filter('#content').children();
            $("body").append(posts);

            var window = $("#messic-about-window");
            window.kendoWindow({
                width: "410px",
                height: "400px",
                title: "About Messic",
                modal: true,
                actions: [
                                "Close"
                            ],
                deactivate: function () {
                    this.destroy();
                }
            });
            var kendoWindow = $("#messic-about-window").data("kendoWindow");
            kendoWindow.center();
        });
    });


    //show the new user window
    $("#newAccount").click(function () {
        $.get("settings.do", function (data) {
            $("body").html(data);
            initSettings(true);
        });
    });

    var cookie = UtilGetCookie("messic_login_cookie")
    if (cookie.length > 0) {
        loginSucessfull(cookie);
    }
});