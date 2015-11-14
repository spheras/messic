/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart The following is the entire license notice for the JavaScript code
 *           in this page.
 *
 * The JavaScript code in this page is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License (GNU GPL)
 * as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. The code is distributed
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU GPL for more details.
 *
 * As additional permission under GNU GPL version 3 section 7, you may
 * distribute non-source (e.g., minimized or compacted) forms of that code
 * without the copy of the GNU GPL normally required by section 4, provided you
 * include this license notice and a URL through which recipients can access the
 * Corresponding Source.
 *
 * @licend The above is the entire license notice for the JavaScript code in
 *         this page.
 *
 */
function initAbout() {
    initAPIDoc($("#messic-about-api"));
}

/**
 * Show the about messic splash
 */
function aboutShowMessicVersion() {
    $.get("abouttape.do", function (data) {

        var posts = $($.parseHTML(data)).filter('#content').children();
        $("body").append(posts);
    });
}

/**
 * Show the about messic splash
 */
function aboutShowChanges() {
    $.get("aboutchanges.do", function (data) {
        var posts = $($.parseHTML(data)).filter('#content').children();
        $("body").append(posts);

        $("#messic-about-changes-window").dialog({
            modal: true,
            closeOnEscape: true,
            title: "messic History Changes",
            width: 600,
            maxHeight: 400,
            buttons: {
                Ok: function () {
                    $(this).dialog("close");
                }
            }
        });

    });
}


/**
 * expand or contract a menu, just changing the css (the style will decide what to do)
 * @param divMenu
 */
function aboutExpandMenu(divMenu) {
    var div = $(divMenu);
    if (div.hasClass("messic-about-title-expanded")) {
        div.removeClass("messic-about-title-expanded");
    } else {
        div.addClass("messic-about-title-expanded");
    }
}

/**
 * function that check if there's a new version of messic, and notify to the user.
 */
function aboutCheckUpdate() {
    UtilShowWait(messicLang.checkUpdateWait);

    $.getJSON("services/checkupdate", function (data) {

        UtilHideWait();

        var infoPhrases = messicLang.messicMessagesCheckUpdate1_1;

        if (data.needUpdate) {
            infoPhrases = messicLang.messicMessagesCheckUpdate1_2 + "<b>" + data.lastVersion + "</b>||" + messicLang.messicMessagesCheckUpdate1_3;
        }

        UtilShowMessic(messicLang.messicMessagesCheckUpdate1, infoPhrases);
    });
}