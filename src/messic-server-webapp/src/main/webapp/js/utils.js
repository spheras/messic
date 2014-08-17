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

//This function obtain a random number for an existing album
//its very interesting to obtain random numbers and random urls (preventing caching) (but only for album)
var MESSIC_ALBUM_RANDOM_MAP = {};

function UtilGetAlbumRandom(albumSid) {
    if (albumSid in MESSIC_ALBUM_RANDOM_MAP) {
        return MESSIC_ALBUM_RANDOM_MAP[albumSid];
    } else {
        MESSIC_ALBUM_RANDOM_MAP[albumSid] = UtilGetRandom(0, 9999999999);
    }
}
//reset the random number stablished at the function UtilGetAlbumRandom
function UtilResetAlbumRandom(albumSid) {
    MESSIC_ALBUM_RANDOM_MAP[albumSid] = UtilGetRandom(0, 9999999999);
    // add a item
    //map[key1] = value1;
    // or remove it
    //delete map[key1];
    // or determine whether a key exists
    //key1 in map;
}

function UtilGetRandom(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}
/* function to escape quotes in string variables, useful when are inserted in html/javascript code */
function UtilEscapeHTML(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
/* function that escape all those characteres that are used in javascript, like ' by \' ... */
function UtilEscapeJS(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '\\\'');
}
/* Obtain a GUID - globally unique identifier */
function UtilGetGUID() {
    return UtilS4() + UtilS4() + '-' + UtilS4() + '-' + UtilS4() + '-' +
        UtilS4() + '-' + UtilS4() + UtilS4() + UtilS4();
}

function UtilS4() {
    return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
};

/* Obtain the extension of a filename */
function UtilGetFileExtension(filename) {
    var a = filename.split(".");
    if (a.length === 1 || (a[0] === "" && a.length === 2)) {
        return "";
    }
    return a.pop();
}

/* Obtain the name of the track from the filename, trying to identify if there is a pattern for the track number, and removing it */
function UtilRemoveTrackNumberFromFileName(filename) {
    filename = UtilGetFileNameWithoutExtension(filename);
    if (filename.indexOf('-') > 0 || filename.indexOf('.') > 0) {
        var where = filename.indexOf('-');
        if (where < 0) {
            where = filename.indexOf('.');
        }
        if (where < 4) {
            return filename.substring(where + 1).trim();
        }
    }

    return filename.trim();
}

/* Obtain the file name without the extension */
function UtilGetFileNameWithoutExtension(filename) {
    var a = filename.split(".");
    if (a.length === 1 || (a[0] === "" && a.length === 2)) {
        return filename;
    }
    var extension = a.pop();
    var name = filename.substring(0, filename.length - (extension.length + 1));
    return name;
}

function UtilShowWait(message) {
    $("#messic-wait").css({
        display: "block"
    });
    $("#messic-wait p").text(message);
}

function UtilHideWait() {
    $("#messic-wait").css({
        display: "none"
    });
}

function UtilShowInfo(info) {
    var min_delay = 2000;
    var delay = min_delay + info.length * 100;
    UtilShowInfoDelay(info, delay);
}
/**
 * Show messages from messic. A set of phrases can be showed, once after other. Phrases must be separated by "||"
 */
function UtilShowMessic(infoTitle, infoPhrases) {
    UtilShowMessic(infoTitle, infoPhrases, null);
}
/**
 * Show messages from messic. A set of phrases can be showed, once after other. Phrases must be separated by "||"
 * the nextFunction param is a function that can be executed after the end of the phrases.
 */
function UtilShowMessic(infoTitle, infoPhrases, nextFunction) {
    var phrases = infoPhrases.split("||");
    var currentPhrase = 0;
    var markup = [
                  '<div id="messicOverlay">',
                  '  <div id="messicOverlayContent">',
                  '    <div id="messicAvatar"></div>',
                  '    <div id="messicSkip"></div>',
                  '    <h1>', infoTitle, '</h1>',
                  '    <p id="messicPhrase">', phrases[0], '</p>',
                  '    <div id="messicClose">Aceptar</div>',
                  '  </div>',
                  '</div>'
              ].join('');

    $(markup).hide().appendTo('body').fadeIn();

    $("body").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#messicClose").click();
        }
    });

    $("#messicSkip").click(function () {
        $(this).parent().parent().remove();
        if (nextFunction) {
            nextFunction();
        }
    })

    $("#messicClose").click(function () {
        currentPhrase++;
        if (phrases.length > currentPhrase) {
            $("#messicPhrase").fadeOut(400, function () {
                $(this).html(phrases[currentPhrase]).fadeIn();
            });
        } else {
            $("#messicOverlay").fadeOut(400, function () {
                $(this).remove();
                if (nextFunction) {
                    nextFunction();
                }
            });
        }
    });

}

function UtilShowInfoDelay(info, delay) {
    $('.messic-smallinfo').stop();
    $('.messic-smallinfo').remove();
    var code = "<div class='messic-smallinfo'>" + info + "</div>";
    $('body').append($(code));
    setTimeout(function () {
        $('.messic-smallinfo').fadeOut().remove();
    }, delay);
    //$('.messic-smallinfo').delay(delay).remove();
}

function UtilCreateCookie(name, value, days) {
    var expires;
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toGMTString();
    } else {
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function UtilGetCookie(c_name) {
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(c_name + "=");
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1;
            c_end = document.cookie.indexOf(";", c_start);
            if (c_end == -1) {
                c_end = document.cookie.length;
            }
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    return "";
}


// https://gist.github.com/jonleighton
// Converts an ArrayBuffer directly to base64, without any intermediate 'convert
// to string then
// use window.btoa' step. According to my tests, this appears to be a faster
// approach:
// http://jsperf.com/encoding-xhr-image-data/5

function UtilBase64ArrayBuffer(arrayBuffer) {
    var base64 = ''
    var encodings = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/'

    var bytes = new Uint8Array(arrayBuffer)
    var byteLength = bytes.byteLength
    var byteRemainder = byteLength % 3
    var mainLength = byteLength - byteRemainder

    var a, b, c, d
    var chunk

    // Main loop deals with bytes in chunks of 3
    for (var i = 0; i < mainLength; i = i + 3) {
        // Combine the three bytes into a single integer
        chunk = (bytes[i] << 16) | (bytes[i + 1] << 8) | bytes[i + 2]

        // Use bitmasks to extract 6-bit segments from the triplet
        a = (chunk & 16515072) >> 18 // 16515072 = (2^6 - 1) << 18
        b = (chunk & 258048) >> 12 // 258048 = (2^6 - 1) << 12
        c = (chunk & 4032) >> 6 // 4032 = (2^6 - 1) << 6
        d = chunk & 63 // 63 = 2^6 - 1

        // Convert the raw binary segments to the appropriate ASCII encoding
        base64 += encodings[a] + encodings[b] + encodings[c] + encodings[d]
    }

    // Deal with the remaining bytes and padding
    if (byteRemainder == 1) {
        chunk = bytes[mainLength]

        a = (chunk & 252) >> 2 // 252 = (2^6 - 1) << 2

        // Set the 4 least significant bits to zero
        b = (chunk & 3) << 4 // 3 = 2^2 - 1

        base64 += encodings[a] + encodings[b] + '=='
    } else if (byteRemainder == 2) {
        chunk = (bytes[mainLength] << 8) | bytes[mainLength + 1]

        a = (chunk & 64512) >> 10 // 64512 = (2^6 - 1) << 10
        b = (chunk & 1008) >> 4 // 1008 = (2^6 - 1) << 4

        // Set the 2 least significant bits to zero
        c = (chunk & 15) << 2 // 15 = 2^4 - 1

        base64 += encodings[a] + encodings[b] + encodings[c] + '='
    }
    return base64
}

/**
 *	function to put the full screen mode of the navigator
 */
function UtilFullScreen() {
    var docElm = document.documentElement;
    if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
    } else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
    } else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
    } else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
    }
}

/**
 *	Function to load a JS file. the callback is launched after the script is loaded
 */
function UtilLoadJSFile(src, callback) {
    var s = document.createElement('script');
    s.src = src;
    s.async = true;
    s.onreadystatechange = s.onload = function () {
        var state = s.readyState;
        if (!callback.done && (!state || /loaded|complete/.test(state))) {
            callback.done = true;
            callback();
        }
    };
    document.getElementsByTagName('head')[0].appendChild(s);
    return s;
}

/**
 * Function that compare two strings and return the % of similarity
 * @param s string
 * @param t string
 * @returns % of similarity
 */
function UtilStringComparator(s, t) {

    var levenshtein = UtilLevDist(s, t);
    var longest = s.length;
    if (t.length > s.length) {
        longest = t.length;
    }
    return (1 - levenshtein / longest) * 100;
}

/**
 * http://stackoverflow.com/questions/11919065/sort-an-array-by-the-levenshtein-distance-with-best-performance-in-javascript
 * http://www.merriampark.com/ld.htm, http://www.mgilleland.com/ld/ldjavascript.htm, Damerau–Levenshtein distance (Wikipedia)
 * @param s string
 * @param t string
 * @returns the distance between two strings
 */
function UtilLevDist(s, t) {
    var d = []; //2d matrix

    // Step 1
    var n = s.length;
    var m = t.length;

    if (n == 0) return m;
    if (m == 0) return n;

    //Create an array of arrays in javascript (a descending loop is quicker)
    for (var i = n; i >= 0; i--) d[i] = [];

    // Step 2
    for (var i = n; i >= 0; i--) d[i][0] = i;
    for (var j = m; j >= 0; j--) d[0][j] = j;

    // Step 3
    for (var i = 1; i <= n; i++) {
        var s_i = s.charAt(i - 1);

        // Step 4
        for (var j = 1; j <= m; j++) {

            //Check the jagged ld total so far
            if (i == j && d[i][j] > 4) return n;

            var t_j = t.charAt(j - 1);
            var cost = (s_i == t_j) ? 0 : 1; // Step 5

            //Calculate the minimum
            var mi = d[i - 1][j] + 1;
            var b = d[i][j - 1] + 1;
            var c = d[i - 1][j - 1] + cost;

            if (b < mi) mi = b;
            if (c < mi) mi = c;

            d[i][j] = mi; // Step 6

            //Damerau transposition
            if (i > 1 && j > 1 && s_i == t.charAt(j - 2) && s.charAt(i - 2) == t_j) {
                d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + cost);
            }
        }
    }

    // Step 7
    return d[n][m];
}

/**
 * Return the mime of a certain extension
 * @param filetype, structure with mimetype and container
 */
function UtilGetMime(filetype) {
    var mimetype = '';
    var media_container = 'video';
    switch (filetype) {
    case 'mp4':
        mimetype = 'video/mp4; codecs="avc1.42E01E, mp4a.40.2"';
        break;
    case 'ogg':
        mimetype = 'video/ogg; codecs="theora, vorbis"';
        break;
    case 'webm':
        mimetype = 'video/webm; codecs="vp8, vorbis"';
        break;
    case 'mp3':
        mimetype = 'audio/mpeg';
        media_container = 'audio';
        break;
    }
    return {
        'mimetype': mimetype,
        'container': media_container
    };
}

/**
 * Function to now if a certain mime is supported by the navigator.
 * See this function in conjuntion with the UtilGetMime
 * @param mimetype
 * @param container
 * @returns {Boolean}
 */
function UtilSupportsMedia(mimetype, container) {
    var elem = document.createElement(container);
    if (typeof elem.canPlayType == 'function') {
        var playable = elem.canPlayType(mimetype);
        if ((playable.toLowerCase() == 'maybe') || (playable.toLowerCase() == 'probably')) {
            return true;
        }
    }
    return false;
}


/**
 * Function to check if a var is numeric.
 * See http://stackoverflow.com/questions/9716468/is-there-any-function-like-isnumeric-in-javascript-to-validate-numbers
 */
function UtilIsNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
