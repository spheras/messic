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

/* init the album page */
function initPlaylist() {

    // function to leave the upload section
    VAR_changeSection = function (nextFunction) {
        var visible = $("#messic-playlist-menuoption-save").is(':visible');
        if (visible) {
            $
                .confirm({
                    'title': messicLang.playlistLeaveTitle,
                    'message': messicLang.playlistLeaveContent,
                    'buttons': {
                        'Yes': {
                            'title': messicLang.confirmationYes,
                            'class': 'blue',
                            'action': function () {
                                nextFunction();
                            }
                        },
                        'No': {
                            'title': messicLang.confirmationNo,
                            'class': 'gray',
                            'action': function () {} // Nothing to do in this case. You can as
                            // well omit the action property.
                        }
                    }
                });
        } else {
            nextFunction();
        }
    }

}

/**
 * show the playlist songs at the content of the playlist page
 * @param playlistSid sid of the playlist
 */
function playlistShow(playlistSid, div) {
    $.ajax({
        type: "GET",
        async: true,
        url: "services/playlists/?filterSid=" + playlistSid,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            UtilShowInfo("Error while getting playlist!");
        },
        success: function (data) {
            $(".messic-playlist-content-list-playlist").removeClass("messic-playlist-content-list-playlist-selected");
            $(div).addClass("messic-playlist-content-list-playlist-selected");

            if (data[0]) {
                var playlist = data[0];
                var code = "<ul>";
                if (playlist.songs) {
                    for (var j = 0; j < playlist.songs.length; j++) {
                        var song = playlist.songs[j];

                        code = code + "<li class=\"messic-albumentity-container\" draggable=\"true\" data-songsid=\"" + song.sid + "\" data-albumsid=\"" + song.album.sid + "\" data-songname=\"" + UtilEscapeHTML(song.name) + "\" data-albumname=\"" + UtilEscapeHTML(song.album.name) + "\" data-authorname=\"" + UtilEscapeHTML(song.album.author.name) + "\" data-songrate=\"" + UtilEscapeHTML(song.rate) + "\">";
                        code = code + "  <div class=\"messic-albumentity-remove\" onclick=\"$(this).parent().remove();playlistEditStatus();\"></div>";
                        code = code + "  <div class=\"messic-albumentity-albumcover\" title=\"" + UtilEscapeHTML(song.album.author.name) + "\n" + UtilEscapeHTML(song.album.name) + "\n" + UtilEscapeHTML(song.name) + "\">";
                        code = code + "      <div class=\"messic-albumentity-add\"></div>";
                        code = code + "      <img src=\"services/albums/" + song.album.sid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken + "&" + UtilGetAlbumRandom(song.album.sid) + "\"></img>";
                        if (!song.rate || song.rate <= 1) {
                            code = code + "      <div class=\"messic-albumentity-vinyl\"></div>";
                        } else if (song.rate == 2) {
                            code = code + "      <div class=\"messic-albumentity-vinyl messic-albumentity-vinyl-ratetwo\"></div>";
                        } else if (song.rate >= 3) {
                            code = code + "      <div class=\"messic-albumentity-vinyl messic-albumentity-vinyl-ratethree\"></div>";
                        }
                        code = code + "  </div>"
                        code = code + "  <div class=\"messic-albumentity-albumauthor\" title=\"" + UtilEscapeHTML(song.album.author.name) + "\" onclick=\"showAuthorPage(" + song.album.author.sid + ")\">" + UtilEscapeHTML(song.album.author.name) + "</div>";
                        code = code + "  <div class=\"messic-albumentity-albumtitle\" title=\"" + UtilEscapeHTML(song.name) + "\" onclick=\"exploreEditAlbum('" + song.album.sid + "')\">" + UtilEscapeHTML(song.name) + "</div>";
                        code = code + "</li>";
                    }
                }
                code = code + "</ul>";
                $("#messic-playlist-content-selected").empty();
                var $code = $(code);

                $code.find(".messic-albumentity-add").hover(function () {
                    $("#messic-playlist-background").addClass("interesting");
                }, function () {
                    $("#messic-playlist-background").removeClass("interesting");
                });

                $code.find(".messic-albumentity-add").longpress(function (e) {
                    if (e) {
                        e.stopPropagation();
                        e.preventDefault();
                    }
                    var $div = $(e.target).parent().parent();
                    var albumSid = $div.data("albumsid");
                    var songSid = $div.data("songsid");
                    var songName = $div.data("songname");
                    var albumName = $div.data("albumname");
                    var authorName = $div.data("authorname");
                    var songRate = $div.data("songrate");
                    addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate, true);
                }, function (e) {
                    if (e) {
                        e.stopPropagation();
                        e.preventDefault();
                    }
                    var $div = $(e.target).parent().parent();
                    var albumSid = $div.data("albumsid");
                    var songSid = $div.data("songsid");
                    var songName = $div.data("songname");
                    var albumName = $div.data("albumname");
                    var authorName = $div.data("authorname");
                    var songRate = $div.data("songrate");
                    addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate);
                });

                $("#messic-playlist-content-selected").append($code);

                var dragSrcEl;
                $(".messic-albumentity-container").bind("dragstart", function () {
                    $(this).addClass("messic-albumentity-container-dragging");

                    dragSrcEl = this;
                    //e.dataTransfer.effectAllowed = 'move';
                    //e.dataTransfer.setData('text/html', this.innerHTML);
                });
                $(".messic-albumentity-container").bind("dragend", function () {
                    $(this).removeClass("messic-albumentity-container-dragging");
                });

                $(".messic-albumentity-container").bind("dragover", function (e) {
                    if (e.preventDefault) {
                        e.preventDefault(); // Necessary. Allows us to drop.
                    }

                    if ($(dragSrcEl).index() > $(this).index()) {
                        $(dragSrcEl).insertBefore($(this));
                    } else {
                        $(dragSrcEl).insertAfter($(this));
                    }

                    playlistEditStatus();

                    return false;
                });

                $(".messic-albumentity-container").bind("drop", function (e) {
                    if (e.stopPropagation) {
                        e.stopPropagation(); // Stops some browsers from redirecting.
                    }
                    e.preventDefault();
                    return false;
                });
            }
        }
    });
}

/**
 * Function to create a new playlist with the list of song sids as parameter
 * @param songSids list of song sids to be linked with the playlist
 */
function playlistCreateNewPlaylist(songSids) {
    var code = "<div id=\"messic-playlists-create-container-overlay\">";
    code = code + "  <div id=\"messic-playlists-create-container\">";
    code = code + "      <input type=\"text\" value=\"\" placeholder=\"" + messicLang.playlistCreateNewPlaceholder + "\">";
    code = code + "      <button id=\"messic-playlists-create-ok\" class=\"button\">" + messicLang.playlistCreateNewOK + "</button>";
    code = code + "      <button id=\"messic-playlists-create-cancel\" class=\"button\" onclick=\"$(this).parent().parent().remove()\">" + messicLang.playlistCreateNewCancel + "</button>";
    code = code + "  </div>";
    code = code + "</div>";


    $(code).hide().appendTo('body').fadeIn();

    $("#messic-playlists-create-ok").click(function () {
        var newname = $("#messic-playlists-create-container input").val();
        if (newname.length <= 0) {
            UtilShowInfo(messicLang.playlistMustHaveName);
            return;
        }

        var playlist = {
            name: newname,
            songs: []
        }

        for (var j = 0; j < songSids.length; j++) {
            playlist.songs.push({
                sid: songSids[j],
            })
        }

        $.ajax({
            type: "POST",
            async: false,
            url: "services/playlists",
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                UtilShowInfo("Error while creating playlist!");
            },
            success: function (data) {
                $("#messic-playlists-create-container-overlay").fadeOut().remove();
                UtilShowInfo(messicLang.playlistCreatedSucessfully);
            },
            processData: false,
            data: JSON.stringify(playlist),
            contentType: "application/json"
        });

    });

}

/**
 * add to the queue the songs of the playlist
 * @param playlistSid
 */
function playlistPlay(playlistSid) {
    $.ajax({
        type: "GET",
        async: true,
        url: "services/playlists/?filterSid=" + playlistSid,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            UtilShowInfo("Error while getting playlist!");
        },
        success: function (data) {
            if (data[0]) {
                var playlist = data[0];
                if (playlist.songs) {
                    for (var j = 0; j < playlist.songs.length; j++) {
                        var song = playlist.songs[j];

                        addSong('raro',
                            UtilEscapeJS(song.album.author.name),
                            song.album.sid, UtilEscapeJS(song.album.name),
                            song.sid,
                            UtilEscapeJS(song.name)
                        );

                    }
                }
            }
        }
    });

}

/* change the status of the playlist to editing */
function playlistEditStatus() {
    $("#messic-playlist-menuoption-save").show();
    $("#messic-playlist-menuoption-discard").show();
}

/* function to discard changes */
function playlistDiscardChanges() {
    $(".messic-playlist-content-list-playlist.messic-playlist-content-list-playlist-selected").click();
    $("#messic-playlist-menuoption-save").hide();
    $("#messic-playlist-menuoption-discard").hide();
    $("input.messic-playlist-content-list-playlist-name-edit").remove();
}

/**
 * Function to save changes done at the playlist
 */
function playlistSaveChanges() {

    var newname = $(".messic-playlist-content-list-playlist-selected .messic-playlist-content-list-playlist-name").text();
    var editname = $(".messic-playlist-content-list-playlist-selected input.messic-playlist-content-list-playlist-name-edit");
    if (editname.length > 0) {
        newname = editname.val();
    }

    var playlistsid = $(".messic-playlist-content-list-playlist.messic-playlist-content-list-playlist-selected").data("sid");

    var playlist = {
        sid: playlistsid,
        name: newname,
        songs: []
    }

    $(".messic-albumentity-container").each(function () {
        playlist.songs.push({
            sid: $(this).data("songsid"),
        })
    });

    $.ajax({
        type: "POST",
        async: false,
        url: "services/playlists",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            UtilShowInfo("Error while saving changes!");
        },
        success: function (data) {
            $("#messic-playlists-create-container-overlay").fadeOut().remove();

            $("#messic-playlist-menuoption-save").hide();
            $("#messic-playlist-menuoption-discard").hide();
            $(".messic-playlist-content-list-playlist-selected .messic-playlist-content-list-playlist-name").text(newname)
            if (editname.length > 0) {
                editname.remove();
            }
            UtilShowInfo(messicLang.playlistSavedSucessfully);
        },
        processData: false,
        data: JSON.stringify(playlist),
        contentType: "application/json"
    });


}


/**
 * Function to remove a playlist
 * @param playlistSid sid of the playlist
 * @param div button div that is calling the functionality
 */
function playlistRemove(playlistSid, div) {
    $.confirm({
        'title': messicLang.playlistRemoveTitle,
        'message': messicLang.playlistRemoveContent,
        'buttons': {
            'Yes': {
                'title': messicLang.confirmationYes,
                'class': 'blue',
                'action': function () {

                    $.ajax({
                        type: "DELETE",
                        async: false,
                        url: "services/playlists/" + playlistSid,
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            UtilShowInfo("Error while removing playlist!");
                        },
                        success: function (data) {
                            $(div).parent().parent().remove();
                            $("#messic-playlist-content-selected").empty();
                            UtilShowInfo(messicLang.playlistRemoveOK);
                        },
                    });

                }
            },
            'No': {
                'title': messicLang.confirmationNo,
                'class': 'blue',
                'action': function () {

                }
            }
        }
    });

}

/**
 * Function to download a zip with the playlist content
 * @param playlistSid sid of the playlist to download
 */
function playlistDownload(playlistSid) {
    var url = 'services/playlists/' + playlistSid + '/zip?messic_token=' + VAR_MessicToken;
    window.open(url);
}

/**
 * Function to edit a playlist
 * @param divButton div button that launch the edit process
 */
function playlistEdit(divButton) {
    playlistEditStatus();

    var parent = $(divButton).parent().parent();
    if (parent.find(".messic-playlist-content-list-playlist-name-edit").length <= 0) {
        var currentName = parent.find(".messic-playlist-content-list-playlist-name").text();
        var code = "<input class=\"messic-playlist-content-list-playlist-name-edit\" type=\"text\" value=\"" + currentName + "\">"

        parent.find(".messic-playlist-content-list-playlist-name").prepend($(code));
        parent.find(".messic-playlist-content-list-playlist-name-edit").focus();
    }
}

function playlistAddToPlaylist(songSids) {

    $.getJSON("services/playlists?songsInfo=false", function (data) {
        var code = "<div id=\"messic-playlists-select-container-overlay\" onclick=\"$(this).remove()\">";
        code = code + "  <div id=\"messic-playlists-select-container\" onclick=\"event.stopPropagation();\">";
        code = code + "      <h2 title=\"" + messicLang.playlistSelectOneTitle + "\">" + messicLang.playlistSelectOneTitle + "</h2>"
        code = code + "      <div id=\"messic-playlists-select-existent\">";
        if (data.length > 0) {
            code = code + "        <ul>";
            for (var j = 0; j < data.length; j++) {
                code = code + "<li data-sid=\"" + data[j].sid + "\">" + data[j].name + "</li>";
            }
            code = code + "        </ul>";
        } else {
            code = code + "<h4>" + messicLang.playlistSelectNone + "</h4>"
        }
        code = code + "      </div>";
        code = code + "      <div id=\"messic-playlists-select-new\">";
        code = code + "          <h4>" + messicLang.playlistOrCreateTitle + "</h4>"
        code = code + "          <input type=\"text\" value=\"\" placeholder=\"" + messicLang.playlistCreateNewPlaceholder + "\">";
        code = code + "          <button id=\"messic-playlists-select-new-ok\" class=\"button\">" + messicLang.playlistCreateNewOK + "</button>";
        code = code + "          <button id=\"messic-playlists-select-new-cancel\" class=\"button\" onclick=\"$(this).parent().parent().parent().remove()\">" + messicLang.playlistCreateNewCancel + "</button>";
        code = code + "      </div>";
        code = code + "  </div>";
        code = code + "</div>";


        $(code).hide().appendTo('body').fadeIn();

        $("#messic-playlists-select-existent li").click(function () {
            var sid = $(this).data("songsid");
            $.getJSON("services/playlists?songsInfo=true&filterSid=" + sid, function (dataPlaylistSelected) {
                if (!dataPlaylistSelected[0].songs) {
                    dataPlaylistSelected[0].songs = new Array();
                }
                for (var j = 0; j < songSids.length; j++) {
                    dataPlaylistSelected[0].songs.push({
                        sid: songSids[j],
                    });
                }

                $.ajax({
                    type: "POST",
                    async: false,
                    url: "services/playlists",
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        UtilShowInfo("Error while creating playlist!");
                    },
                    success: function (data) {
                        $("#messic-playlists-select-container-overlay").fadeOut().remove();
                        UtilShowInfo(messicLang.playlistSavedSucessfully);

                    },
                    processData: false,
                    data: JSON.stringify(dataPlaylistSelected[0]),
                    contentType: "application/json"
                });

            });
        });

        $("#messic-playlists-select-new-ok").click(function () {
            var newname = $("#messic-playlists-select-new input").val();
            if (newname.length <= 0) {
                UtilShowInfo(messicLang.playlistMustHaveName);
                return;
            }

            var playlist = {
                name: newname,
                songs: []
            }

            for (var j = 0; j < songSids.length; j++) {
                playlist.songs.push({
                    sid: songSids[j],
                })
            }

            $.ajax({
                type: "POST",
                async: false,
                url: "services/playlists",
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    UtilShowInfo("Error while creating playlist!");
                },
                success: function (data) {
                    $("#messic-playlists-select-container-overlay").fadeOut().remove();
                    UtilShowInfo(messicLang.playlistCreatedSucessfully);
                },
                processData: false,
                data: JSON.stringify(playlist),
                contentType: "application/json"
            });

        });
    });
}