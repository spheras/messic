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
/* init the explore page */
function initExplore() {


    //adding the code to the click for the order by author or order by album
    $(".messic-explore-orderby").each(function (index) {
        var type = $(this).data('type');
        if (type == "author") {
            $(this).click(function () {
                if (!$(this).hasClass("messic-explore-orderby-selected")) {
                    $(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
                    $(this).addClass("messic-explore-orderby-selected");
                }
                $(".messic-explore-values").empty();
                exploreByAuthor();

            })
        } else if (type == "album") {
            $(this).click(function () {
                if (!$(this).hasClass("messic-explore-orderby-selected")) {
                    $(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
                    $(this).addClass("messic-explore-orderby-selected");
                }
                $(".messic-explore-values").empty();
                exploreByAlbum(false);
            })
        } else if (type == "genre") {
            $(this).click(function () {
                if (!$(this).hasClass("messic-explore-orderby-selected")) {
                    $(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
                    $(this).addClass("messic-explore-orderby-selected");
                }
                $(".messic-explore-values").empty();
                exploreByGenre();
            })
        } else if (type == "date") {
            $(this).click(function () {
                if (!$(this).hasClass("messic-explore-orderby-selected")) {
                    $(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
                    $(this).addClass("messic-explore-orderby-selected");
                }
                $(".messic-explore-values").empty();
                exploreByAlbum(true);
            })
        }
    });



    exploreByAuthor();
}


//Fill the web page with the whole list of albums, ordered by genre
function exploreByGenre() {
    $.getJSON("services/genres", function (data) {
        $(".messic-explore-words").empty();
        var lastChar = "";
        var wordAppened = false;

        for (var i = 0; i < data.length; i++) {
            var genre = data[i];
            if (genre) {
                var code = "";
                var firstword = genre.name.substring(0, 1).toUpperCase();
                var changedWord = false;
                if (i == 0 || lastChar != firstword) {
                    code = code + "<a name=\"messic-explore-word" + firstword + "\" class=\"messic-explore-anchor\"></a>";
                    lastChar = firstword;
                    changedWord = true;
                    wordAppened = false;
                }
                code = code + "<div class=\"messic-explore-authortitle\" title=\"" + genre.name + "\"><div class=\"messic-explore-authortitle-text\"\">" + genre.name + "</div>";

                var codewords = "<a class=\"messic-explore-word\" href=\"#messic-explore-word" + lastChar.toUpperCase() + "\">" + lastChar.toUpperCase() + "</a>";

                if (changedWord || !wordAppened) {
                    code = code + "<div class=\"messic-explore-albumstartword\">" + lastChar.toUpperCase() + "</div>";
                }

                var alength = 0;
                $.ajax({
                    url: "services/albums?filterGenreSid=" + genre.sid,
                    dataType: 'json',
                    async: false,
                    success: function (dataAlbums) {
                        alength = dataAlbums.length;
                        if (alength > 0 && !wordAppened) {
                            $(".messic-explore-words").append(codewords);
                            wordAppened = true;
                        }
                        for (var j = 0; j < dataAlbums.length; j++) {
                            var album = dataAlbums[j];
                            code = code + "<div class=\"messic-explore-albumcontainer\" title=\"" + album.name + "\">";
                            code = code + "    <div class=\"messic-explore-albumcover\">";
                            code = code + "        <div class=\"messic-explore-add\" onclick=\"addAlbum(" + album.sid + ")\"></div>";
                            code = code + "        <div class=\"messic-explore-vinyl-detail\" title=\"Edit Album\" onclick=\"exploreEditAlbum(" + album.sid + ")\">...</div>";
                            code = code + "        <img  src=\"services/albums/" + album.sid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken + "&" + UtilGetAlbumRandom(album.sid) + "\" onclick=\"exploreEditAlbum(" + album.sid + ")\"></img>";
                            code = code + "        <div class=\"messic-explore-vinyl\"></div>";
                            code = code + "    </div>"
                            code = code + "    <div class=\"messic-explore-albumtitle\" title=\"" + album.name + "\" onclick=\"exploreEditAlbum('" + album.sid + "')\">" + album.name + "</div>";
                            code = code + "    <div class=\"messic-explore-albumauthortitle\" title=\"" + album.author.name + "\" onclick=\"showAuthorPage(" + album.author.sid + ")\">" + album.author.name + "</div>";
                            code = code + "</div>";
                        }
                    }
                });

                code = code + "</div>";
                if (alength > 0) {
                    //we dont put genres without albums
                    $(".messic-explore-values").append(code);
                }
            }
        }

        $(".messic-explore-add").hover(function () {
            $("#messic-playlist-background").addClass("interesting");
        }, function () {
            $("#messic-playlist-background").removeClass("interesting");
        });

    });

}

//Fill the web page with the whole list of albums, ordered by album
function exploreByAlbum(orderByDate) {
    $.getJSON("services/albums?authorInfo=true&songsInfo=false", function (data) {

        if (orderByDate) {
            data.sort(function (a, b) {
                return parseFloat(a.year) - parseFloat(b.year)
            });
        }

        $(".messic-explore-words").empty();

        var lastChar = "";
        for (var i = 0; i < data.length; i++) {
            var code = "";
            var firstword;
            if (orderByDate) {
                firstword = "" + data[i].year;
            } else {
                firstword = data[i].name.substring(0, 1).toUpperCase();
            }
            var changedWord = false;

            code = code + "<div class=\"messic-explore-albumcontainer\" title=\"" + data[i].name + "\">";

            if (i == 0 || lastChar != firstword) {
                code = code + "<a name=\"messic-explore-word" + firstword + "\" class=\"messic-explore-anchor\"></a>";
                lastChar = firstword;
                changedWord = true;
            }

            if(!orderByDate){
                if (changedWord) {
                    code = code + "<div class=\"messic-explore-albumstartword\">" + lastChar.toUpperCase() + "</div>";

                    codewords = "<a class=\"messic-explore-word\" href=\"#messic-explore-word" + lastChar.toUpperCase() + "\">" + lastChar.toUpperCase() + "</a>";
                    $(".messic-explore-words").append(codewords);
                }
            }


            if (orderByDate) {
                if (data[i].year) {
                    code = code + "    <label class=\"messic-explore-date\">" + data[i].year + "</label>";
                } else {
                    code = code + "    <label class=\"messic-explore-date\">???</label>";
                }
            }
            code = code + "    <div class=\"messic-explore-albumcover\">";
            code = code + "        <div class=\"messic-explore-add\" onclick=\"addAlbum(" + data[i].sid + ")\"></div>";
            code = code + "        <div class=\"messic-explore-vinyl-detail\" title=\"Edit Album\" onclick=\"exploreEditAlbum(" + data[i].sid + ")\">...</div>";
            code = code + "        <img  src=\"services/albums/" + data[i].sid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken + "&" + UtilGetAlbumRandom(data[i].sid) + "\" onclick=\"exploreEditAlbum(" + data[i].sid + ")\"></img>";
            code = code + "        <div class=\"messic-explore-vinyl\"></div>";
            code = code + "    </div>"
            code = code + "    <div class=\"messic-explore-albumtitle\" onclick=\"exploreEditAlbum('" + data[i].sid + "')\" title=\"" + data[i].name + "\">" + data[i].name + "</div>";
            code = code + "    <div class=\"messic-explore-albumauthortitle\" onclick=\"showAuthorPage(" + data[i].author.sid + ")\" title=\"" + data[i].author.name + "\">" + data[i].author.name + "</div>";
            code = code + "</div>";

            code = code + "</div>";
            $(".messic-explore-values").append(code);
        }

        $(".messic-explore-add").hover(function () {
            $("#messic-playlist-background").addClass("interesting");
        }, function () {
            $("#messic-playlist-background").removeClass("interesting");
        });

    });
}

//fill the web page with the whole list of album categorized by authors
function exploreByAuthor() {
    $.getJSON("services/authors?albumsInfo=true&songsInfo=false", function (data) {

        //it should be empty, but sometimes, touch screens, send click and touch...
        if ($(".messic-explore-values").children().length > 0) {
            //this is to avoid double click (touch and click events at the same time)
            return;
        }

        $(".messic-explore-words").empty();

        var lastChar = "";
        for (var i = 0; i < data.length; i++) {
            var code = "";
            var firstword = data[i].name.substring(0, 1).toUpperCase();
            var changedWord = false;
            if (i == 0 || lastChar != firstword) {
                code = code + "<a name=\"messic-explore-word" + firstword + "\" class=\"messic-explore-anchor\"></a>";
                lastChar = firstword;
                changedWord = true;
            }
            code = code + "<div class=\"messic-explore-authortitle\" title=\"" + data[i].name + "\"><div class=\"messic-explore-authortitle-text\" onclick=\"showAuthorPage(" + data[i].sid + ")\">" + data[i].name + "</div>";
            if (changedWord) {
                code = code + "<div class=\"messic-explore-albumstartword\">" + lastChar.toUpperCase() + "</div>";

                codewords = "<a class=\"messic-explore-word\" href=\"#messic-explore-word" + lastChar.toUpperCase() + "\">" + lastChar.toUpperCase() + "</a>";
                $(".messic-explore-words").append(codewords);
            }

            //we add some albums, just to debug and test
            //var debugRandom=UtilGetRandom(1,5);
            //for(var debug=0;debug<debugRandom;debug++){
            for (var j = 0; data[i].albums && j < data[i].albums.length; j++) {
                var album = data[i].albums[j];
                code = code + "<div class=\"messic-explore-albumcontainer\" title=\"" + album.name + "\">";
                code = code + "    <div class=\"messic-explore-albumcover\">";
                code = code + "        <div class=\"messic-explore-add\" onclick=\"addAlbum(" + album.sid + ")\"></div>";
                code = code + "        <div class=\"messic-explore-vinyl-detail\" title=\"Edit Album\" onclick=\"exploreEditAlbum(" + album.sid + ")\">...</div>";
                code = code + "        <img  src=\"services/albums/" + album.sid + "/cover?preferredWidth=100&preferredHeight=100&messic_token=" + VAR_MessicToken + "&" + UtilGetAlbumRandom(album.sid) + "\" onclick=\"exploreEditAlbum(" + album.sid + ")\"></img>";
                code = code + "        <div class=\"messic-explore-vinyl\"></div>";
                code = code + "    </div>"
                code = code + "    <div class=\"messic-explore-albumtitle\" onclick=\"exploreEditAlbum('" + album.sid + "')\" title=\"" + album.name + "\">" + album.name + "</div>";
                code = code + "</div>";
            }
            //}

            code = code + "</div>";
            $(".messic-explore-values").append(code);
        }

        $(".messic-explore-add").hover(function () {
            $("#messic-playlist-background").addClass("interesting");
        }, function () {
            $("#messic-playlist-background").removeClass("interesting");
        });
    });
}

function exploreEditAlbum(albumSid) {
    $.get("album.do?albumSid=" + albumSid, function (data) {
        selectOption($("#messic-menu-explore"));
        $("#messic-page-content").empty();
        var posts = $($.parseHTML(data)).filter('#content').children();
        $("#messic-page-content").append(posts);
        initAlbum();
        window.scrollTo(0, 0);
    });
}