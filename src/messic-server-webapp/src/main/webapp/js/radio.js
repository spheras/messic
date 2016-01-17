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
var MessicRadio = function () {

    var currentSongInPlaylistIndex = -1;
    var flagRadioAvailable = false;
    var divradio = $("#messic-playlist-action-radio");
    var divradioplayer = $("#messic_radio_player_container");
    var self = this;

    //flag to know if we should foce the next song
    var flagForceNextSong = false;

    /**
     * Radio button click
     */
    this.messicRadioButton = function () {
        if (!flagRadioAvailable) {
            return;
        }

        if (divradio.hasClass("messic-playlist-action-radio-on")) {
            self.messicRadioStop();
        } else {
            self.messicRadioStart();
        }
    }

    //if there is a radio icon, then it is supposed to have a radio service available, let's link with the event
    if (divradio.length > 0) {
        flagRadioAvailable = true;
        divradio.click(self.messicRadioButton);
    }


    /**
     * Start the radio streaming cast, if available
     */
    this.messicRadioStart = function () {
        if (!flagRadioAvailable) {
            return;
        }

        $.ajax({
            type: "PUT",
            async: false,
            url: "services/radio/start",
            contentType: "application/json",
            error: function (jqXHR, textStatus, errorThrown) {

                divradio.removeClass("messic-playlist-action-radio-on");
                divradioplayer.hide();
                UtilShowInfo(messicLang.radioError);

            },
            success: function (data, textStatus, jqXHR) {
                divradio.addClass("messic-playlist-action-radio-on");
                divradioplayer.show();
                self.messicRadioPoll();

                var $audio = $("#messic_radio_player");
                var audioobj = $audio[0];
                var serverurl = data;

                if (!data.startsWith("http")) {
                    serverurl = "http://" + document.location.hostname + data;
                }

                var sourceUrl = serverurl + "?timestamp=" + (new Date().getTime()); //"http://localhost:8000/mymount?timestamp=" + (new Date().getTime());


                $audio.attr("src", sourceUrl);
                $audio.attr("type", "audio/mp3")
                audioobj.pause();
                audioobj.controls = true;
                audioobj.playbackRate = 1;
                audioobj.load(); //suspends and restores all audio element
                audioobj.oncanplaythrough = audioobj.play();
                audioobj.controls = true;
                audioobj.controls = true;


                $("#jquery_jplayer").jPlayer("volume", 0); // 0.0 - 1.0
                $("#jquery_jplayer").jPlayer("pause");
                mainPauseCurrentVinyl();

                //UtilShowInfo("@TODO The radio service will start RUNNING with the following song at the playlist.  You can disable again pressing again this button.");
            },
        });
    }

    /**
     * Stop the radio streaming cast
     */
    this.messicRadioStop = function () {
        if (!flagRadioAvailable) {
            return;
        }

        $.ajax({
            type: "PUT",
            async: false,
            url: "services/radio/stop",
            contentType: "application/json",
            error: function (jqXHR, textStatus, errorThrown) {
                divradio.removeClass("messic-playlist-action-radio-on");
                divradioplayer.hide();
                currentSongInPlaylistIndex = -1;
                $("#jquery_jplayer").jPlayer("volume", 0.8);
            },
            success: function (data, textStatus, jqXHR) {
                divradio.removeClass("messic-playlist-action-radio-on");
                divradioplayer.hide();

                var $audio = $("#messic_radio_player");
                var audioobj = $audio[0];
                $audio.attr("src", "");
                $audio.attr("type", "audio/mp3")
                audioobj.pause();
                currentSongInPlaylistIndex = -1;
                $(".jplayer-playlist-radio").removeClass("jplayer-playlist-radio-on");
                $("#jquery_jplayer").jPlayer("volume", 0.8);
            }
        });
    }


    /**
     * function fo know if the radio is on or o not
     */
    this.messicIsRadioOn = function () {
        if (!flagRadioAvailable) {
            return;
        }

        if (divradio.length > 0) {
            return divradio.hasClass("messic-playlist-action-radio-on");
        } else {
            return false;
        }
    }

    /**
     * function to poll the server in order to watch what is being played on the radio
     */
    this.messicRadioPoll = function () {
        if (!flagRadioAvailable) {
            return;
        }

        $.ajax({
            type: "GET",
            url: "services/radio/status",
            contentType: "application/json",
            success: function (data) {
                //Update your playlist radio status

                if (self.messicIsRadioOn()) {


                    //if the radio is waiting, and the playlist we have is filled with songs, and ... there is another song to be played in the playlist
                    var availableSongs = (playlist.playlist.length > 0 && playlist.playlist.length > currentSongInPlaylistIndex + 1);
                    var forceNext = flagForceNextSong && availableSongs;
                    if (forceNext || (data.status == "WAITING" && availableSongs)) {
                        currentSongInPlaylistIndex++;
                        flagForceNextSong = false;
                        $.ajax({
                            type: "POST",
                            async: false,
                            url: "services/radio/" + playlist.playlist[currentSongInPlaylistIndex].songSid + "?songQueuePosition=" + currentSongInPlaylistIndex,
                            contentType: "application/json",
                            success: function (data) {
                                $(".jplayer-playlist-radio").removeClass("jplayer-playlist-radio-on");
                                $($(".jplayer-playlist-radio")[currentSongInPlaylistIndex]).addClass("jplayer-playlist-radio-on");
                            },
                        });
                    } else if (!availableSongs) {
                        $(".jplayer-playlist-radio").removeClass("jplayer-playlist-radio-on");
                    }

                    if (currentSongInPlaylistIndex >= 0) {
                        $($(".jplayer-playlist-radio")[currentSongInPlaylistIndex]).addClass("jplayer-playlist-radio-on");
                    }

                }

            },
            dataType: "json",
            complete: function () {
                if (self.messicIsRadioOn()) {
                    //we continue polling
                    setTimeout(self.messicRadioPoll, 1000);
                }
            }
        });
    }

    /**
     * function to know when a song has been removed
     */
    this.messicRadioRemovedSong = function (index) {
        //if the removed song is a previous song, we need to relocate the new position in the array
        if (index < currentSongInPlaylistIndex) {
            currentSongInPlaylistIndex--;
        } else if (index == currentSongInPlaylistIndex || index == 99999) {
            //if the user has removed the current song being played by the radio
            //let's play the following?
            currentSongInPlaylistIndex--;
            flagForceNextSong = true;
        }
    }

    /**
     * function to know when a song has been moved in the queue playlist
     */
    this.messicRadioSongMoved = function (oldIndex, newIndex) {
        if (oldIndex == currentSongInPlaylistIndex) {
            //the current song being played in the radio has been moved to a different position!
            currentSongInPlaylistIndex = newIndex;
        } else if (oldIndex > currentSongInPlaylistIndex && newIndex <= currentSongInPlaylistIndex) {
            //we need to move right the current song being played by the radio
            currentSongInPlaylistIndex++;
        } else if (oldIndex < currentSongInPlaylistIndex && newIndex > currentSongInPlaylistIndex) {
            //we need to move left the current song being played by the radio
            currentSongInPlaylistIndex--;
        }
    }
}


function messicRadioPlayerLoad() {
    $("#jquery_jplayer").jPlayer({
        ready: function (event) {
            $(this).jPlayer("setMedia", {
                title: "messic radio",
                mp3: $("#jquery_jplayer_content").data("radiourl") + "?timestamp=" + (new Date().getTime()),
            });

            $(this).jPlayer("play");
        },

        playlistOptions: {
            enableRemoveControls: true
        },
        cssSelectorAncestor: "#jquery_jplayer_content",
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

    $("#jquery_jplayer_content").draggable();
    /*{
        handle: "not:#messic_radio_alone_description_container label"
    });*/

    $("a#messic_radio_alone_showinfo").click(function () {
        $(this).parent().remove();
        $("#messic_radio_alone_info").show();
    })

    messicRadioPlayerPollInfo();

    $("#messic_radio_alone_action_wikipedia").click(function () {
        var userLang = navigator.language || navigator.userLanguage;
        var nquery = $("#messic_radio_alone_author").text();
        window.open("https://" + userLang.split('-')[0].toLowerCase() + ".wikipedia.org/w/index.php?search=" + nquery + "");
    });
    $("#messic_radio_alone_action_youtube").click(function () {
        var userLang = navigator.language || navigator.userLanguage;
        var nquery = $("#messic_radio_alone_author").text();
        window.open("https://www.youtube.com/results?search_query=" + nquery + "");

    });

    $("#messic_radio_alone_action_fx").click(function () {
        showFx();
    });

}

var messicRadioLastRadioInfo;

function messicRadioPlayerPollInfo() {
    $.ajax({
        type: "GET",
        url: "services/radio/status",
        contentType: "application/json",
        success: function (data) {
            if (!messicRadioLastRadioInfo || messicRadioLastRadioInfo.songSid != data.songSid) {
                //new song
                $("#messic_radio_alone_author").text(data.authorName);
                $("#messic_radio_alone_album").text(data.albumName);
                $("#messic_radio_alone_song").text(data.songName);

                $("#messic_radio_alone_cover").attr("src", "services/radio/cover?preferredWidth=400&preferredHeight=400&timestamp=" + (new Date().getTime()));
            }

            messicRadioLastRadioInfo = data;
        },
        complete: function () {
            setTimeout(messicRadioPlayerPollInfo, 2000);
        }
    });



}
