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
//function to check before to change to another section
var VAR_changeSection = function (nextFunction) {
    nextFunction();
    //by default, nothing to do
    //this function should be overwritten by each module in order to be checked
}


//Select a menu
function selectOption(optionSelected) {
    $(".messic-menu-icon").each(function (index) {
        $(this).removeClass("messic-menu-icon-selected");
    });

    $(optionSelected).addClass("messic-menu-icon-selected");
}

function mainCheckUpdate() {
    $.getJSON("services/checkupdate?automatic=true", function (data) {
        if (data.needUpdate) {
            var message = messicLang.messicMessagesCheckUpdate1_2 + "<b>" + data.lastVersion + "</b>.  " + messicLang.messicMessagesCheckUpdate1_3;
            var code = '<div class="messic-main-notification">' + message + '</div>';
            var $code = $(code);
            $('#messic-page-content').prepend($code);
            $code.delay(15000).fadeOut(2000, function () {
                this.remove();
            });
        }
    });
}

function initMessic() {

    (function ($) {
        var playlist = $("#messic-playlist");
        playlist.tinyscrollbar({
            axis: 'x'
        });
        playlist.data("plugin_tinyscrollbar").update('right');
    })(jQuery);

    $(".playlist-moveprevious").click(function () {
        var playlist = $("#messic-playlist").data("plugin_tinyscrollbar");
        playlist.messic_moveleft();
    });

    $(".playlist-movenext").click(function () {
        var playlist = $("#messic-playlist").data("plugin_tinyscrollbar");
        playlist.messic_moveright();
    });

    playlist = new jPlayerPlaylist({
        jPlayer: "#jquery_jplayer",
        cssSelectorAncestor: "#jquery_jplayer_content",
        cssSelectorPlaylist: "#messic-playlist-background",
    }, [], {
        playlistOptions: {
            enableRemoveControls: true
        },
        wmode: "window",
        errorAlerts: true,
        swfPath: "js/vendor/jplayer",
        solution: 'html,flash',
        supplied: 'mp3',
        volume: 1,
        verticalVolume: true,
        formats: ['mp3'],
    });

    JPlayerHackDrag_addListeners();

    mainCreateRandomLists();

    mainCheckUpdate();


    $("#messic-main-logout").on('click touchend', function () {
        mainLogout();
    });

    $("#messic-menu-settings").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);
            $.ajax({
                url: "settings.do",
                success: function (data) {
                    $("#messic-page-content").empty();
                    var posts = $($.parseHTML(data)).find("#messic-page-content").children();
                    $("#messic-page-content").append(posts);
                    initSettings(false);
                    window.scrollTo(0, 0);
                }
            });

        }
        VAR_changeSection(nextFunction);
    });

    //if an space is detected, then we can pause or play the player
    $("body").keydown(function (event) {
        // Get the focused element and check if is an input element
        var focused = $(document.activeElement);
        if (focused.is("input") || focused.is("select") || focused.is("textarea")) {
            // it is an input element, let him to manage the space, not us
            return;
        }

        if (event.keyCode == 32) {
            var div = $(".jp-play");
            if (div.length > 0 && !(div.css('display') == 'none')) {
                div.click();
                event.preventDefault();
                event.stopPropagation();
            } else {
                div = $(".jp-pause");
                if (div.length > 0 && !(div.css('display') == 'none')) {
                    div.click();
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
        }
    });



    $("#messic-menu-playlist").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);
            $.ajax({
                url: "playlist.do",
                success: function (data) {
                    $("#messic-page-content").empty();
                    var posts = $($.parseHTML(data)).find("#messic-playlist-content").children();
                    $("#messic-page-content").append(posts);
                    window.scrollTo(0, 0);
                    initPlaylist();
                }
            });
        }
        VAR_changeSection(nextFunction);
    });

    $("#messic-menu-home").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);
            mainCreateRandomLists();
            window.scrollTo(0, 0);
            VAR_changeSection = function (nextFunction) {
                nextFunction();
            }
        }
        VAR_changeSection(nextFunction);
    });

    $("#messic-menu-help").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);

            $.get("about.do", function (data) {
                $("#messic-page-content").empty();
                var posts = $($.parseHTML(data)).filter('#content').children();
                $("#messic-page-content").append(posts);
                window.scrollTo(0, 0);
                initAbout();
            });

            VAR_changeSection = function (nextFunction) {
                nextFunction();
            }

        }
        VAR_changeSection(nextFunction);
    });

    $("#messic-menu-upload").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);
            $.ajax({
                url: "upload.do",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("messic_token", VAR_MessicToken)
                },
                success: function (data) {
                    $("#messic-page-content").empty();
                    var posts = $($.parseHTML(data)).filter('#content').children();
                    $("#messic-page-content").append(posts);
                    initUpload();
                    window.scrollTo(0, 0);
                }
            });

        }
        VAR_changeSection(nextFunction);
    });

    $("#messic-menu-explore").on('click touchend', function () {
        var self = this;
        var nextFunction = function () {
            selectOption(self);
            $.get("explore.do", function (data) {
                $("#messic-page-content").empty();
                var posts = $($.parseHTML(data)).filter('#content').children();
                $("#messic-page-content").append(posts);
                initExplore();
                window.scrollTo(0, 0);
            });
            VAR_changeSection = function (nextFunction) {
                nextFunction();
            }
        }
        VAR_changeSection(nextFunction);
    });

    $("#messic-search-text").keypress(function (e) {
        if (e.which == 13) {
            mainSearch();
            e.preventDefault();
        }
    });

    $("#messic-search-do").on('click touchend', function () {
        mainSearch();
        window.scrollTo(0, 0);
    });

    $(".messic-playlist-action").hover(function () {
        $("#messic-playlist-background").addClass("interesting");
    }, function () {
        $("#messic-playlist-background").removeClass("interesting");
    });
}

/**
 * Function to logout from messic
 */
function mainLogout() {
    //removing cookie
    UtilCreateCookie("messic_login_cookie", "", 30);
    //going to login
    window.location.href = "logout.do";
    window.location.href = "login.do";
}

var MESSIC_FX_STOP_FLAG = false;

/* show fx in full screen mode */
function showFx() {

    MESSIC_FX_STOP_FLAG = false;

    var stopFunction = function () {
        return MESSIC_FX_STOP_FLAG;
    }
    var canvas = document.createElement('canvas');
    canvas.id = "c";
    canvas.width = $(window).width();
    canvas.height = $(window).height();
    $(canvas).addClass("messic-canvas-fx");

    document.body.appendChild(canvas);

    $(canvas).click(function () {
        MESSIC_FX_STOP_FLAG = true;
        $(canvas).fadeOut(400, function () {
            $(canvas).remove();
        });
    });

    var random = UtilGetRandom(2, 8);
    //alert("Vamos a ver el:"+random);
    UtilFullScreen();
    loadFx(random, canvas, stopFunction);
}

/**
 * load an effect
 */
function loadFx(fxnumber, canvas, stopFunction) {
    UtilLoadJSFile("js/fx/fx" + fxnumber + "/fx" + fxnumber + ".js", function () {
        startFx(canvas, stopFunction);
    });
}

/* show the author page */
function showAuthorPage(authorSid) {
    var self = this;
    var nextFunction = function () {
        $.get("author.do?authorSid=" + authorSid, function (data) {
            selectOption($("#messic-menu-explore"));
            $("#messic-page-content").empty();
            var posts = $($.parseHTML(data)).filter('#content').children();
            $("#messic-page-content").append(posts);
            initAuthor();
        });
    }
    VAR_changeSection(nextFunction);
}

function mainSearch() {
    var content = $("#messic-search-text").val();
    if (content.length <= 0) {
        return;
    }
    $.getJSON("services/search?content=" + content, function (data) {
        if (data) {
            var code = mainCreateRandomList(data);
            $("#messic-page-content").prepend($(code));

            $('.messic-main-randomlist-add').longpress(function (e) {
                if (e) {
                    e.stopPropagation();
                    e.preventDefault();
                }
                var $div = $(e.target).parent();
                var albumSid = $div.data("albumsid");
                var songSid = $div.data("songsid");
                var songName = $div.data("songname");
                var albumName = $div.data("albumname");
                var authorName = $div.data("authorname");
                var songRate = $div.data("songrate");
                addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate, true);


                //alert('You just longpressed something.');
            }, function (e) {
                if (e) {
                    e.stopPropagation();
                    e.preventDefault();
                }
                var $div = $(e.target).parent();
                var albumSid = $div.data("albumsid");
                var songSid = $div.data("songsid");
                var songName = $div.data("songname");
                var albumName = $div.data("albumname");
                var authorName = $div.data("authorname");
                var songRate = $div.data("songrate");
                addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate);

                //alert('You released before longpress duration and that\'s why its a shortpress now.');
            });

            $(".messic-main-randomlist").tinycarousel({
                display: 9,
                duration: 800,
                infinite: false
            });

            $(".messic-main-randomlist-add").hover(function () {
                $("#messic-playlist-background").addClass("interesting");
            }, function () {
                $("#messic-playlist-background").removeClass("interesting");
            });

            window.scrollTo(0, 0);

        } else {
            UtilShowInfo("We haven't found anything");
        }
    });
}

//play all the songs of a randomlist
function mainPlayRandomList(div) {
    var eldiv = $(div);
    var elparent = eldiv.parent().parent();
    var elfind = elparent.find(".messic-main-randomlist-albumcover");
    var i = 0;
    elfind.each(function () {
        var albumSid = $(this).data("albumsid");
        var songSid = $(this).data("songsid");
        var songName = $(this).data("songname");
        var albumName = $(this).data("albumname");
        var authorName = $(this).data("authorname");
        var songRate = $(this).data("songrate");
        var dontplay = (i != 0);
        addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate, false, dontplay);
        i++;
    });
}

//Creates the randomlist divs
function mainCreateRandomList(randomlist, lastTitleType) {

    var code = "<div class=\"messic-main-randomlist\">";
    code = code + "  <div class=\"messic-main-randomlist-name ";

    var titleType = UtilGetRandom(1, 7);
    while (titleType == lastTitleType) {
        titleType = UtilGetRandom(1, 7);
    }
    lastTitleType = titleType;

    switch (titleType) {
    case 1:
        code = code + "blue";
        break;
    case 2:
        code = code + "green";
        break;
    case 3:
        code = code + "orange";
        break;
    case 4:
        code = code + "pink";
        break;
    case 5:
        code = code + "purple";
        break;
    case 6:
        code = code + "red";
        break;
    case 7:
        code = code + "yellow";
        break;
    }

    code = code + "\">" + messicLang.search(randomlist.name);
    code = code + "      <div class=\"messic-main-randomlist-close\" onclick=\"$(this).parent().parent().remove();\"></div>";
    code = code + "  </div>";
    code = code + "  <div class=\"messic-main-randomlist-title-container\">";
    code = code + "     <div class=\"messic-main-randomlist-title\">" + messicLang.search(randomlist.title) + "</div>";
    code = code + "     <div class=\"messic-main-randomlist-playall\" onclick=\"mainPlayRandomList(this)\" title=\"" + messicLang.randomlistplayall + "\"></div>";
    code = code + "     <div class=\"messic-main-randomlist-details\">";
    if (randomlist.details) {
        for (var k = 0; k < randomlist.details.length; k++) {
            var detail = randomlist.details[k];
            code = code + "        <div class=\"messic-main-randomlist-detail\">" + detail + "</div>";
        }
    }
    code = code + "     </div>";
    code = code + "  </div>";
    code = code + "	 <div class=\"messic-main-randomlist-viewportcontainer\">";
    code = code + "  <a class=\"buttons prev\" href=\"#\"></a>";
    code = code + "  <div class=\"viewport\">";
    code = code + "      <ul class=\"overview\">";


    for (var j = 0; randomlist.songs && j < randomlist.songs.length; j++) {
        var song = randomlist.songs[j];
        code = code + "<li>";
        code = code + "    <div data-songsid=\"" + song.sid + "\" data-albumsid=\"" + song.album.sid + "\" data-songname=\"" + UtilEscapeHTML(song.name) + "\" data-albumname=\"" + UtilEscapeHTML(song.album.name) + "\" data-authorname=\"" + UtilEscapeHTML(song.album.author.name) + "\" data-songrate=\"" + UtilEscapeHTML(song.rate) + "\" class=\"messic-main-randomlist-albumcover\" title=\"" + UtilEscapeHTML(song.album.author.name) + "\n" + UtilEscapeHTML(song.album.name) + "\n" + UtilEscapeHTML(song.name) + "\" onclick=\"exploreEditAlbum('" + song.album.sid + "')\">";
        code = code + "        <div class=\"messic-main-randomlist-menu\" title=\"" + messicLang.playlistmoreoptions + "\" onclick=\"event.stopPropagation();mainShowSongOptions(" + song.sid + ",this,$(this).parent()," + song.rate + ");\"></div>";
        code = code + "        <div class=\"messic-main-randomlist-add\" ></div>";

        code = code + "        <img src=\"services/albums/" + song.album.sid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken + "&" + UtilGetAlbumRandom(song.album.sid) + "\"></img>";
        if (!song.rate || song.rate <= 1) {
            code = code + "        <div class=\"messic-main-randomlist-vinyl\"></div>";
        } else if (song.rate == 2) {
            code = code + "        <div class=\"messic-main-randomlist-vinyl messic-main-randomlist-vinyl-ratetwo\"></div>";
        } else if (song.rate >= 3) {
            code = code + "        <div class=\"messic-main-randomlist-vinyl messic-main-randomlist-vinyl-ratethree\"></div>";
        }
        code = code + "    </div>"
        code = code + "    <div class=\"messic-main-randomlist-albumauthor\" title=\"" + UtilEscapeHTML(song.album.author.name) + "\" onclick=\"showAuthorPage(" + song.album.author.sid + ")\">" + UtilEscapeHTML(song.album.author.name) + "</div>";
        code = code + "    <div class=\"messic-main-randomlist-albumtitle\" title=\"" + UtilEscapeHTML(song.name) + "\" onclick=\"exploreEditAlbum('" + song.album.sid + "')\">" + UtilEscapeHTML(song.name) + "</div>";
        code = code + "</li>";
    }

    //at the end we put an empty box, only to make sure it's enough space
    code = code + "<li> <div></div> </li>";

    code = code + "      </ul>";
    code = code + "  </div>";
    code = code + "  <a class=\"buttons next\" href=\"#\"></a>";
    code = code + "  </div>";
    code = code + "</div>";
    return code;
}

function mainCreateRandomLists() {
    $("#messic-page-content").empty();

    $.getJSON("services/randomlists", function (data) {
        var randomlists = data;
        var lastTitleType = 0;
        for (var i = 0; i < randomlists.length; i++) {
            var randomlist = randomlists[i];
            var code = mainCreateRandomList(randomlist, lastTitleType);
            $("#messic-page-content").append($(code));
        }

        $('.messic-main-randomlist-add').longpress(function (e) {
            if (e) {
                e.stopPropagation();
                e.preventDefault();
            }
            var $div = $(e.target).parent();
            var albumSid = $div.data("albumsid");
            var songSid = $div.data("songsid");
            var songName = $div.data("songname");
            var albumName = $div.data("albumname");
            var authorName = $div.data("authorname");
            var songRate = $div.data("songrate");
            addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate, true);


            //alert('You just longpressed something.');
        }, function (e) {
            if (e) {
                e.stopPropagation();
                e.preventDefault();
            }
            var $div = $(e.target).parent();
            var albumSid = $div.data("albumsid");
            var songSid = $div.data("songsid");
            var songName = $div.data("songname");
            var albumName = $div.data("albumname");
            var authorName = $div.data("authorname");
            var songRate = $div.data("songrate");
            addSong('raro', UtilEscapeJS(authorName), albumSid, UtilEscapeJS(albumName), songSid, UtilEscapeJS(songName), songRate);

            //alert('You released before longpress duration and that\'s why its a shortpress now.');
        });


        $(".messic-main-randomlist").tinycarousel({
            display: 9,
            duration: 800,
            infinite: false
        });

        $(".messic-main-randomlist-add").hover(function () {
            $("#messic-playlist-background").addClass("interesting");
        }, function () {
            $("#messic-playlist-background").removeClass("interesting");
        });

        $(".messic-main-randomlist-playall").hover(function () {
            $("#messic-playlist-background").addClass("interesting");
        }, function () {
            $("#messic-playlist-background").removeClass("interesting");
        });

    });
}

//Add an album to the playlist of songs (query the songs of the album to the server)
function addAlbum(albumSid) {
    $.getJSON("services/albums/" + albumSid + "?songsInfo=true&authorInfo=true", function (data) {
        for (var z = 0; z < data.songs.length; z++) {

            var dontplay = (z != 0);

            addSong("raro",
                UtilEscapeHTML(data.author.name),
                data.sid,
                UtilEscapeHTML(data.name),
                data.songs[z].sid,
                UtilEscapeHTML(data.songs[z].name),
                data.songs[z].rate,
                false,
                dontplay
            );
        }
        UtilShowInfo(data.songs.length + " " + messicLang.songsadded);
    });
}

//Add a song to the playlist of songs
function addSong(titleA, authorName, albumSid, albumName, songSid, songName, rate, playnow, dontplay) {
    var media = {
        "albumSid": albumSid,
        "songSid": songSid,
        title: titleA,
        mp3: "services/songs/" + songSid + "/audio?messic_token=" + VAR_MessicToken,
        author: authorName,
        album: albumName,
        song: songName,
        "songRate": rate,
        boxart: "services/albums/" + albumSid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken,
        dontplay: false
    };

    if (dontplay) {
        media.dontplay = true;
    }

    playlist.add(media, playnow);

    $("#messic-playlist").data("plugin_tinyscrollbar").update('right');
}

/* clear the current playlist */
function clearPlaylist() {
    playlist.remove();
    $("#messic-playlist").data("plugin_tinyscrollbar").update('right');
}

function playVinyl(index) {
    var newli = $("#messic-playlist-container li:nth-child(" + (index + 1) + ")");
    var oldli = $(".jp-playlist-current");

    //first, remove the current player
    oldli.removeClass("jplayer-playlist-li-expanding");
    oldli.addClass("jplayer-playlist-li");
    oldli.find(".jplayer-playlist-vinyl").removeClass("jplayer-playlist-vinylPlaying");
    oldli.find(".jplayer-playlist-vinyl").addClass("jplayer-playlist-vinylHide");
    oldli.find(".jplayer-playlist-vinylHand").removeClass("jplayer-playlist-vinylHandPlaying");
    //oldli.find(".jplayer-playlist-vinyl").attr("class","jplayer-playlist-vinyl jplayer-playlist-vinylHide");
    //oldli.find(".jplayer-playlist-vinylHand").attr("class","jplayer-playlist-vinylHand");

    //last, add the new player
    var vinyl = newli.find(".jplayer-playlist-vinyl");
    var vinylHand = newli.find(".jplayer-playlist-vinylHand");
    newli.removeClass("jplayer-playlist-li");
    newli.addClass("jplayer-playlist-li-expanding");
    vinyl.removeClass("jplayer-playlist-vinylHide");
    vinyl.addClass("jplayer-playlist-vinylPlaying");
    vinylHand.addClass("jplayer-playlist-vinylHandPlaying");
    //vinyl.attr("class","jplayer-playlist-vinyl jplayer-playlist-vinylPlaying");
    //vinylHand.attr("class","jplayer-playlist-vinylHand jplayer-playlist-vinylHandPlaying");

    /*
	this.parentNode.parentNode.className =\"jplayer-playlist-li-expanding\";
	this.parentNode.children[0].className =\"jplayer-playlist-vinyl jplayer-playlist-vinylPlaying\";
	this.parentNode.children[1].className =\"jplayer-playlist-vinylHand jplayer-playlist-vinylHandPlaying\";
	*/
}

/* function to download the current playlist which is playing now */
function downloadCurrentPlaylist() {
    var songs = "";
    $("#messic-playlist-container .jplayer-playlist-vinyl-container").each(function () {
        if (songs.length > 0) {
            songs = songs + ":";
        }
        songs = songs + $(this).data("songsid");
    });

    if (songs.length > 0) {
        var url = 'services/songs/' + songs + '/zip?messic_token=' + VAR_MessicToken;
        window.open(url);
    }
}

/**
 * Function to create a new playlist with the current queue of songs
 */
function loveCurrentPlaylist() {
    var songSids = new Array();
    $("#messic-playlist-container .jplayer-playlist-vinyl-container").each(function () {
        songSids.push($(this).data("songsid"));
    });

    playlistCreateNewPlaylist(songSids);
}

/**
 * function to show more song options like add to playlist or rate it
 * @param songSid sid of the song
 * @param divCaller div calling this function, the bubble will be next to it
 * @param songRate current rate of the song
 */
function mainShowSongOptions(songSid, divCaller, parentDiv, songRate) {
    var position = $(divCaller).offset();
    position.top = position.top - $(window).scrollTop();
    position.left = position.left - $(window).scrollLeft();

    var code = "<div id=\"messic-main-songoptions-overlay\" onclick=\"$(this).fadeOut().remove();\">";
    code = code + "  <div class=\"messic-main-songoptions-bubble\">";
    code = code + "    <div class=\"messic-main-songoptions-addtoplaylist\" title=\"" + messicLang.playlistAddToPlaylist + "\" onclick=\"playlistAddToPlaylist([" + songSid + "]);$(this).parent().parent().remove();\"></div>";
    code = code + "    <div class=\"messic-main-songoptions-rate-container\">";
    code = code + "      <div class=\"messic-main-songoptions-rate-one";
    if (songRate && songRate >= 1) {
        code = code + " messic-main-songoptions-rate-selected";
    }
    code = code + "\"></div>";
    code = code + "      <div class=\"messic-main-songoptions-rate-two";
    if (songRate && songRate >= 2) {
        code = code + " messic-main-songoptions-rate-selected";
    }
    code = code + "\"></div>";
    code = code + "      <div class=\"messic-main-songoptions-rate-three";
    if (songRate && songRate >= 3) {
        code = code + " messic-main-songoptions-rate-selected";
    }
    code = code + "\"></div>";
    code = code + "    </div>";
    code = code + "  </div>";
    code = code + "</div>";

    $(code).hide().appendTo('body').fadeIn();

    $("#messic-main-songoptions-overlay .messic-main-songoptions-rate-one").click(function () {
        mainSongRate(songSid, 1, parentDiv);
    });
    $("#messic-main-songoptions-overlay .messic-main-songoptions-rate-two").click(function () {
        mainSongRate(songSid, 2, parentDiv);
    });
    $("#messic-main-songoptions-overlay .messic-main-songoptions-rate-three").click(function () {
        mainSongRate(songSid, 3, parentDiv);
    });

    $(".messic-main-songoptions-bubble").css(position);
}

/**
 * Rate a song
 * @param songSid sid of the song to rate
 * @param newRate new rate (1,2,3)
 */
function mainSongRate(songSid, newRate, parentDiv) {
    $.getJSON("services/songs?filterSongSid=" + songSid + "&albumInfo=false&authorInfo=false", function (data) {
        var song = data[0];
        song.rate = newRate;

        $.ajax({
            url: 'services/songs', //Server script to process data
            type: 'POST',
            success: function () {
                if (parentDiv) {
                    var divs = $(parentDiv).find(".messic-main-randomlist-vinyl-ratetwo");
                    if (divs.length > 0) {
                        if (newRate != 2) {
                            divs.removeClass("messic-main-randomlist-vinyl-ratetwo");
                            if (newRate > 2) {
                                divs.addClass("messic-main-randomlist-vinyl-ratethree");
                            }
                        }
                    } else {
                        divs = $(parentDiv).find(".messic-main-randomlist-vinyl-ratethree");
                        if (divs.length > 0) {
                            if (newRate != 3) {
                                divs.removeClass("messic-main-randomlist-vinyl-ratethree");
                                if (newRate == 2) {
                                    divs.addClass("messic-main-randomlist-vinyl-ratetwo");
                                }
                            }
                        } else {
                            divs = $(parentDiv).find(".jplayer-playlist-vinyl-ratetwo");
                            if (divs.length > 0) {
                                if (newRate != 2) {
                                    divs.removeClass("jplayer-playlist-vinyl-ratetwo");
                                    if (newRate > 2) {
                                        divs.addClass("jplayer-playlist-vinyl-ratethree");
                                    }
                                }
                            } else {
                                divs = $(parentDiv).find(".jplayer-playlist-vinyl-ratethree");
                                if (divs.length > 0) {
                                    if (newRate != 3) {
                                        divs.removeClass("jplayer-playlist-vinyl-ratethree");
                                        if (newRate == 2) {
                                            divs.addClass("jplayer-playlist-vinyl-ratetwo");
                                        }
                                    }
                                } else {
                                    //	then it doesn't have any rate?
                                    divs = $(parentDiv).find(".jplayer-playlist-vinyl");
                                    if (divs.length > 0) {
                                        if (newRate == 2) {
                                            divs.addClass("jplayer-playlist-vinyl-ratetwo");
                                        } else if (newRate > 2) {
                                            divs.addClass("jplayer-playlist-vinyl-ratethree");
                                        }

                                    } else {
                                        divs = $(parentDiv).find(".messic-main-randomlist-vinyl");
                                        if (divs.length > 0) {
                                            if (newRate == 2) {
                                                divs.addClass("messic-main-randomlist-vinyl-ratetwo");
                                            } else if (newRate > 2) {
                                                divs.addClass("messic-main-randomlist-vinyl-ratethree");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            },
            error: function (e) {
                UtilShowInfo(e);
            },
            processData: false,
            data: JSON.stringify(song),
            contentType: "application/json"
        });

    });
}


/**
 * Function to remain opened or closed the top menu
 * this is useful for devices without hover
 */
function mainOpenTopMenu() {
    var div = $("#messic-top");
    if (div.hasClass("messic-top-opened")) {
        div.removeClass("messic-top-opened");
        div.addClass("messic-top-closed");
    } else {
        div.addClass("messic-top-opened");
        div.removeClass("messic-top-closed");
    }
}

/**
 * Function to remain opened or closed the playlist queue menu
 * this is useful for devices without hover
 */
function mainOpenPlaylistMenu() {
    var div = $("#messic-playlist-background");
    if (div.hasClass("messic-playlist-background-opened")) {
        div.removeClass("messic-playlist-background-opened");
        div.addClass("messic-playlist-background-closed");
    } else {
        div.addClass("messic-playlist-background-opened");
        div.removeClass("messic-playlist-background-closed");
    }
}

/**
 * Function to remain opened the playlist queue menu
 */
function mainPinPlaylist() {
    var divpin = $("#messic-playlist-action-pin");

    if (divpin.hasClass("messic-playlist-action-pin-in")) {
        divpin.removeClass("messic-playlist-action-pin-in");

        var div = $("#messic-playlist-background");
        if (div.hasClass("messic-playlist-background-opened")) {
            div.removeClass("messic-playlist-background-opened");
        }
    } else {
        divpin.addClass("messic-playlist-action-pin-in");

        var div = $("#messic-playlist-background");
        if (!div.hasClass("messic-playlist-background-opened")) {
            div.addClass("messic-playlist-background-opened");
            div.removeClass("messic-playlist-background-closed");
        }
    }

}