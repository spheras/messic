/*
 * ······················································
 * ·Fork of the jplayer.playlist for messic requirements·
 * ·following, the original version author:
 * ······················································
 *
 * Playlist Object for the jPlayer Plugin
 * http://www.jplayer.org
 *
 * Copyright (c) 2009 - 2013 Happyworm Ltd
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/MIT
 *
 * Author: Mark J Panaghiston
 * Version: 2.3.0
 * Date: 20th April 2013
 *
 * Requires:
 *  - jQuery 1.7.0+
 *  - jPlayer 2.3.0+
 */

/* Code verified using http://www.jshint.com/ */
/*jshint asi:false, bitwise:false, boss:false, browser:true, curly:true, debug:false, eqeqeq:true, eqnull:false, evil:false, forin:false, immed:false, jquery:true, laxbreak:false, newcap:true, noarg:true, noempty:true, nonew:true, onevar:false, passfail:false, plusplus:false, regexp:false, undef:true, sub:false, strict:false, white:false, smarttabs:true */
/*global  jPlayerPlaylist:true */

(function ($, undefined) {

    jPlayerPlaylist = function (cssSelector, playlist, options) {
        var self = this;

        this.current = 0;
        this.loop = false; // Flag used with the jPlayer repeat event
        this.shuffled = false;
        this.removing = false; // Flag is true during remove animation, disabling the remove() method until complete.

        this.cssSelector = $.extend({}, this._cssSelector, cssSelector); // Object: Containing the css selectors for jPlayer and its cssSelectorAncestor
        this.options = $.extend(true, {
            keyBindings: {
                next: {
                    key: 39, // RIGHT
                    fn: function () {
                        self.next();
                    }
                },
                previous: {
                    key: 37, // LEFT
                    fn: function () {
                        self.previous();
                    }
                }
            }
        }, this._options, options); // Object: The jPlayer constructor options for this playlist and the playlist options

        this.playlist = []; // Array of Objects: The current playlist displayed (Un-shuffled or Shuffled)
        this.original = []; // Array of Objects: The original playlist

        this._initPlaylist(playlist); // Copies playlist to this.original. Then mirrors this.original to this.playlist. Creating two arrays, where the element pointers match. (Enables pointer comparison.)

        // Setup the css selectors for the extra interface items used by the playlist.
        this.cssSelector.title = this.cssSelector.cssSelectorAncestor + " .jp-title"; // Note that the text is written to the decendant li node.
        this.cssSelector.playlist = this.cssSelector.cssSelectorPlaylist + " .jp-playlist"; //Playlist maybe in different div
        this.cssSelector.next = this.cssSelector.cssSelectorAncestor + " .jp-next";
        this.cssSelector.previous = this.cssSelector.cssSelectorAncestor + " .jp-previous";
        this.cssSelector.shuffle = this.cssSelector.cssSelectorAncestor + " .jp-shuffle";
        this.cssSelector.shuffleOff = this.cssSelector.cssSelectorAncestor + " .jp-shuffle-off";

        // Override the cssSelectorAncestor given in options
        this.options.cssSelectorAncestor = this.cssSelector.cssSelectorAncestor;

        // Override the default repeat event handler
        this.options.repeat = function (event) {
            self.loop = event.jPlayer.options.loop;
        };

        // Create a ready event handler to initialize the playlist
        $(this.cssSelector.jPlayer).bind($.jPlayer.event.ready, function () {
            self._init();
        });

        // Create an ended event handler to move to the next item
        $(this.cssSelector.jPlayer).bind($.jPlayer.event.ended, function () {
            self.next();
        });

        // Create a play event handler to pause other instances
        $(this.cssSelector.jPlayer).bind($.jPlayer.event.play, function () {
            $(this).jPlayer("pauseOthers");
            //messic handle to resume any player animation
            mainResumeCurrentVinyl();
        });

        $(this.cssSelector.jPlayer).bind($.jPlayer.event.pause, function () {
            //messic handle to pause any player animation
            mainPauseCurrentVinyl();
        });

        $(this.cssSelector.jPlayer).bind($.jPlayer.event.error, function (event) {
            //alert("Error Event: type = " + event.jPlayer.error.type); // The actual error code string. Eg., "e_url" for $.jPlayer.error.URL error.
            switch (event.jPlayer.error.type) {
                case $.jPlayer.error.URL:
                    //reportBrokenMedia(event.jPlayer.error); // A function you might create to report the broken link to a server log.
                    UtilShowInfo(messicLang.playlistError1 + self.playlist[self.current].song + "     " + messicLang.playlistError2);

                    self.next();
                    $(this).blur();

                    break;
                case $.jPlayer.error.NO_SOLUTION:
                    // Do something
                    break;
            }
        });


        // Create a resize event handler to show the title in full screen mode.
        $(this.cssSelector.jPlayer).bind($.jPlayer.event.resize, function (event) {
            if (event.jPlayer.options.fullScreen) {
                $(self.cssSelector.title).show();
            } else {
                $(self.cssSelector.title).hide();
            }
        });

        // Create click handlers for the extra buttons that do playlist functions.
        $(this.cssSelector.previous).click(function () {
            self.previous();
            $(this).blur();
            return false;
        });

        $(this.cssSelector.next).click(function () {
            self.next();
            $(this).blur();
            return false;
        });

        $(this.cssSelector.shuffle).click(function () {
            self.shuffle(true);
            return false;
        });
        $(this.cssSelector.shuffleOff).click(function () {
            self.shuffle(false);
            return false;
        }).hide();

        // Put the title in its initial display state
        if (!this.options.fullScreen) {
            $(this.cssSelector.title).hide();
        }

        // Remove the empty <li> from the page HTML. Allows page to be valid HTML, while not interfereing with display animations
        $(this.cssSelector.playlist + " ul").empty();

        // Create .on() handlers for the playlist items along with the free media and remove controls.
        this._createItemHandlers();

        // Instance jPlayer
        $(this.cssSelector.jPlayer).jPlayer(this.options);
    };

    jPlayerPlaylist.prototype = {
        _cssSelector: { // static object, instanced in constructor
            jPlayer: "#jquery_jplayer_1",
            cssSelectorAncestor: "#jp_container_1"
        },
        _options: { // static object, instanced in constructor
            playlistOptions: {
                autoPlay: false,
                loopOnPrevious: false,
                shuffleOnLoop: true,
                enableRemoveControls: false,
                displayTime: 'slow',
                addTime: 'fast',
                removeTime: 'fast',
                shuffleTime: 'slow',
                itemClass: "jp-playlist-item",
                freeGroupClass: "jp-free-media",
                freeItemClass: "jp-playlist-item-free",
                removeItemClass: "jp-playlist-item-remove"
            }
        },
        option: function (option, value) { // For changing playlist options only
            if (value === undefined) {
                return this.options.playlistOptions[option];
            }

            this.options.playlistOptions[option] = value;

            switch (option) {
                case "enableRemoveControls":
                    this._updateControls();
                    break;
                case "itemClass":
                case "freeGroupClass":
                case "freeItemClass":
                case "removeItemClass":
                    this._refresh(true); // Instant
                    this._createItemHandlers();
                    break;
            }
            return this;
        },
        _init: function () {
            var self = this;
            this._refresh(function () {
                if (self.options.playlistOptions.autoPlay) {
                    self.play(self.current);
                } else {
                    self.select(self.current);
                }
            });
        },
        _initPlaylist: function (playlist) {
            this.current = 0;
            this.shuffled = false;
            this.removing = false;
            this.original = $.extend(true, [], playlist); // Copy the Array of Objects
            this._originalPlaylist();
        },
        _originalPlaylist: function () {
            var self = this;
            this.playlist = [];
            // Make both arrays point to the same object elements. Gives us 2 different arrays, each pointing to the same actual object. ie., Not copies of the object.
            $.each(this.original, function (i) {
                self.playlist[i] = self.original[i];
            });
        },
        _refresh: function (instant) {
            /* instant: Can be undefined, true or a function.
             *	undefined -> use animation timings
             *	true -> no animation
             *	function -> use animation timings and excute function at half way point.
             */
            var self = this;

            if (instant && !$.isFunction(instant)) {
                $(this.cssSelector.playlist + " ul").empty();
                $.each(this.playlist, function (i) {
                    $(self.cssSelector.playlist + " ul").append(self._createListItem(self.playlist[i]));
                });
                this._updateControls();
            } else {
                var displayTime = $(this.cssSelector.playlist + " ul").children().length ? this.options.playlistOptions.displayTime : 0;

                $(this.cssSelector.playlist + " ul").slideUp(displayTime, function () {
                    var $this = $(this);
                    $(this).empty();

                    $.each(self.playlist, function (i) {
                        $this.append(self._createListItem(self.playlist[i]));
                    });
                    self._updateControls();
                    if ($.isFunction(instant)) {
                        instant();
                    }
                    if (self.playlist.length) {
                        $(this).slideDown(self.options.playlistOptions.displayTime);
                    } else {
                        $(this).show();
                    }
                });
            }
        },
        _createListItem: function (media) {
            var self = this;
            var index = $(this).parent().parent().index();
            // Wrap the <li> contents in a <div>
            var listItem = "<li class='jplayer-playlist-li'>";

            /* This is the original jplayer.playlist structure

			// Create remove control 
			listItem += "<a href='javascript:;' class='" + this.options.playlistOptions.removeItemClass + "'>&times;</a>";

			// Create links to free media
			if(media.free) {
				var first = true;
				listItem += "<span class='" + this.options.playlistOptions.freeGroupClass + "'>(";
				$.each(media, function(property,value) {
					if($.jPlayer.prototype.format[property]) { // Check property is a media format.
						if(first) {
							first = false;
						} else {
							listItem += " | ";
						}
						listItem += "<a class='" + self.options.playlistOptions.freeItemClass + "' href='" + value + "' tabindex='1'>" + property + "</a>";
					}
				});
				listItem += ")</span>";
			}

			// The title is given next in the HTML otherwise the float:right on the free media corrupts in IE6/7
			listItem += "<a href='javascript:;' class='" + this.options.playlistOptions.itemClass + "' tabindex='1'>" + media.title + (media.artist ? " <span class='jp-artist'>by " + media.artist + "</span>" : "") + "</a>";
            */

            //let's modify it for messic
            listItem += "  <div class=\"jplayer-playlist-vinyl-container\" data-albumSid=\"" + media.albumSid + "\" data-songSid=\"" + media.songSid + "\" >";
            listItem += "    <div class=\"jplayer-playlist-radio animated rubberBand infinite\"></div>";
            listItem += "    <div class=\"jplayer-playlist-vinyl jplayer-playlist-vinylHide";

            if (media.songRate == 2) {
                listItem += " jplayer-playlist-vinyl-ratetwo";
            } else if (media.songRate > 2) {
                listItem += " jplayer-playlist-vinyl-ratethree";
            }
            listItem += "\"></div>";

            listItem += "    <div onclick=\"event.stopPropagation();mainShowSongOptions(" + media.songSid + ",this,$(this).parent()," + media.songRate + ");\" title=\"" + messicLang.playlistmoreoptions + "\" class=\"jplayer-playlist-song-menu\"></div>";
            listItem += "    <a href=\"javascript:;\" class=\"jplayer-playlist-remove jp-playlist-item-remove\"></a>";
            listItem += "    <div class=\"jplayer-playlist-vinylHand\"></div>";
            listItem += "    <img class=\"jplayer-playlist-vinylbox\" src=\"" + media.boxart + "\"></img>";
            listItem += "    <div class=\"jplayer-playlist-vinylPlayButton\" onclick=\"playVinyl($(this).parent().parent().index());\"></div>";
            listItem += "    <a href=\"javascript:;\" class=\"jplayer-playlist-vinyl-author " + this.options.playlistOptions.itemClass + "\" title=\"" + UtilEscapeHTML(media.author) + "\" tabindex=\"1\" onclick=\"showAuthorPage(" + media.authorSid + ")\">" + UtilEscapeHTML(media.author) + "</a>";
            listItem += "    <a href=\"javascript:;\" class=\"jplayer-playlist-vinyl-song " + this.options.playlistOptions.itemClass + "\" title=\"" + UtilEscapeHTML(media.song) + "\" tabindex=\"1\" onclick=\"exploreEditAlbum('" + media.albumSid + "')\">" + UtilEscapeHTML(media.song) + "</a>";
            listItem += "    <a href=\"javascript:;\" class=\"jplayer-playlist-vinyl-album " + this.options.playlistOptions.itemClass + "\" title=\"" + UtilEscapeHTML(media.album) + "\" tabindex=\"1\">" + UtilEscapeHTML(media.album) + "</a>";
            listItem += "  </div>";
            listItem += "</li>";
            //  this.parentNode.parentNode.className =\"jplayer-playlist-li-expanding\";this.parentNode.children[0].className =\"jplayer-playlist-vinyl jplayer-playlist-vinylPlaying\";this.parentNode.children[1].className =\"jplayer-playlist-vinylHand jplayer-playlist-vinylHandPlaying\";
            return listItem;
        },
        _createItemHandlers: function () {
            var self = this;
            // Create live handlers for the playlist items
            //messic modification. Play button funcitonality
            //$(this.cssSelector.playlist).off("click", "a." + this.options.playlistOptions.itemClass).on("click", "a." + this.options.playlistOptions.itemClass, function () {
            //    var index = $(this).parent().parent().index();
            //    if (self.current !== index) {
            //        self.play(index);
            //    } else {
            //        $(self.cssSelector.jPlayer).jPlayer("play");
            //    }
            //    $(this).blur();
            //    return false;
            //});
            //messic modification. Play button funcitonality
            $(this.cssSelector.playlist).on("click", "div.jplayer-playlist-vinylPlayButton", function () {
                var index = $(this).parent().parent().index();
                if (self.current !== index) {
                    self.play(index);
                } else {
                    $(self.cssSelector.jPlayer).jPlayer("play");
                }
                $(this).blur();
                return false;
            });

            // Create live handlers that disable free media links to force access via right click
            $(this.cssSelector.playlist).off("click", "a." + this.options.playlistOptions.freeItemClass).on("click", "a." + this.options.playlistOptions.freeItemClass, function () {
                $(this).parent().parent().find("." + self.options.playlistOptions.itemClass).click();
                $(this).blur();
                return false;
            });

            // Create live handlers for the remove controls
            $(this.cssSelector.playlist).off("click", "a." + this.options.playlistOptions.removeItemClass).on("click", "a." + this.options.playlistOptions.removeItemClass, function () {
                var index = $(this).parent().parent().index();
                self.remove(index);
                if (mainMessicRadio) {
                    mainMessicRadio.messicRadioRemovedSong(index);
                }
                $(this).blur();
                return false;
            });
        },
        _updateControls: function () {
            if (this.options.playlistOptions.enableRemoveControls) {
                $(this.cssSelector.playlist + " ." + this.options.playlistOptions.removeItemClass).show();
            } else {
                $(this.cssSelector.playlist + " ." + this.options.playlistOptions.removeItemClass).hide();
            }
            if (this.shuffled) {
                $(this.cssSelector.shuffleOff).show();
                $(this.cssSelector.shuffle).hide();
            } else {
                $(this.cssSelector.shuffleOff).hide();
                $(this.cssSelector.shuffle).show();
            }
        },
        _highlight: function (index) {
            if (this.playlist.length && index !== undefined) {
                $(this.cssSelector.playlist + " .jp-playlist-current").removeClass("jp-playlist-current");
                $(this.cssSelector.playlist + " li:nth-child(" + (index + 1) + ")").addClass("jp-playlist-current").find(".jp-playlist-item").addClass("jp-playlist-current");
                $(this.cssSelector.title + " li").html(this.playlist[index].title + (this.playlist[index].artist ? " <span class='jp-artist'>by " + this.playlist[index].artist + "</span>" : ""));
            }
        },
        setPlaylist: function (playlist) {
            this._initPlaylist(playlist);
            this._init();
        },
        move: function (oldPos, newPos) {
            //messic can move an existing media to a different position at the queue
            this.playlist.splice(newPos, 0, this.playlist.splice(oldPos, 1)[0]);

            if (oldPos == this.current) {
                //we are moving the current!
                this.current = newPos;
            } else if (oldPos > this.current && this.current >= newPos) {
                //we are moving a next song to an old song position, the back songs has been increased, so we need to move a position the current song.
                this.current++;
            } else if (oldPos < this.current && this.current <= newPos) {
                //we are moving a back song to a next song position, the back songs has been decreased, so we need to move a position the current song
                this.current--;
            }
        },
        add: function (media, playNow) {
            var at = this.playlist.length;
            if (playNow) {
                if (this.playlist.length > 0) {
                    at = this.current + 1;
                } else {
                    playNow = false;
                }
            }

            if (!playNow || this.playlist.length <= 0) {
                $(this.cssSelector.playlist + " ul").append(this._createListItem(media)).find("li:last-child").hide().slideDown(this.options.playlistOptions.addTime);
            } else {
                $(this._createListItem(media)).insertAfter(this.cssSelector.playlist + " ul li:nth-child(" + at + ")");
                $(this.cssSelector.playlist + " ul li:nth-child(" + (at + 1) + ")").hide().slideDown(this.options.playlistOptions.addTime);
            }

            this._updateControls();

            if (!playNow) {
                this.original.push(media);
                this.playlist.push(media); // Both array elements share the same object pointer. Comforms with _initPlaylist(p) system.
            } else {
                this.original.splice(at, 0, media);
                this.playlist.splice(at, 0, media); // Both array elements share the same object pointer. Comforms with _initPlaylist(p) system.
            }

            if (!media.dontplay) {
                if (playNow) {
                    this.play(at);
                } else {
                    if (this.original.length === 1) {
                        this.select(0);
                        this.play(0);
                    } else {
                        if (($('.jp-play').css('display') == 'block')) {
                            //then it's stopped or paused
                            this.play(this.playlist.length - 1);
                        }
                    }
                }
            }
        },
        remove: function (index) {
            var self = this;

            if (index === undefined) {
                this._initPlaylist([]);
                this._refresh(function () {
                    $(self.cssSelector.jPlayer).jPlayer("clearMedia");
                });
                return true;
            } else {

                if (this.removing) {
                    return false;
                } else {
                    index = (index < 0) ? self.original.length + index : index; // Negative index relates to end of array.
                    if (0 <= index && index < this.playlist.length) {
                        this.removing = true;

                        $(this.cssSelector.playlist + " li:nth-child(" + (index + 1) + ")").slideUp(this.options.playlistOptions.removeTime, function () {
                            $(this).remove();

                            if (self.shuffled) {
                                var item = self.playlist[index];
                                $.each(self.original, function (i) {
                                    if (self.original[i] === item) {
                                        self.original.splice(i, 1);
                                        return false; // Exit $.each
                                    }
                                });
                                self.playlist.splice(index, 1);
                            } else {
                                self.original.splice(index, 1);
                                self.playlist.splice(index, 1);
                            }

                            if (self.original.length) {
                                if (index === self.current) {
                                    self.current = (index < self.original.length) ? self.current : self.original.length - 1; // To cope when last element being selected when it was removed
                                    self.select(self.current);
                                } else if (index < self.current) {
                                    self.current--;
                                }
                            } else {
                                $(self.cssSelector.jPlayer).jPlayer("clearMedia");
                                self.current = 0;
                                self.shuffled = false;
                                self._updateControls();
                            }

                            self.removing = false;
                        });
                    }
                    return true;
                }
            }
        },
        select: function (index) {
            index = (index < 0) ? this.original.length + index : index; // Negative index relates to end of array.
            if (0 <= index && index < this.playlist.length) {
                this.current = index;
                playVinyl(index);
                this._highlight(index);

                $(this.cssSelector.jPlayer).jPlayer("setMedia", this.playlist[this.current]);
            } else {
                this.current = 0;
            }
        },
        play: function (index) {
            index = (index < 0) ? this.original.length + index : index; // Negative index relates to end of array.
            if (0 <= index && index < this.playlist.length) {
                if (this.playlist.length) {
                    this.select(index);

                    $(this.cssSelector.jPlayer).jPlayer("play");
                    //					var sb=$(".jp-seek-bar");
                    //					var self=this;
                    //					$(this.cssSelector.jPlayer).bind($.jPlayer.event.playing, function(event) {
                    //						$(self.cssSelector.jPlayer).unbind($.jPlayer.event.playing);
                    //						sb.click();
                    //					});
                }
            } else if (index === undefined) {
                $(this.cssSelector.jPlayer).jPlayer("play");
            }
        },
        pause: function () {
            $(this.cssSelector.jPlayer).jPlayer("pause");
            //messic, we need to pause the vinyl animation (css style of course)
            mainPauseCurrentVinyl();
        },
        next: function () {
            var index = (this.current + 1 < this.playlist.length) ? this.current + 1 : 0;

            if (this.loop) {
                // See if we need to shuffle before looping to start, and only shuffle if more than 1 item.
                if (index === 0 && this.shuffled && this.options.playlistOptions.shuffleOnLoop && this.playlist.length > 1) {
                    this.shuffle(true, true); // playNow
                } else {
                    this.play(index);
                }
            } else {
                // The index will be zero if it just looped round
                if (index > 0) {
                    this.play(index);
                }
            }
        },
        previous: function () {
            var index = (this.current - 1 >= 0) ? this.current - 1 : this.playlist.length - 1;

            if (this.loop && this.options.playlistOptions.loopOnPrevious || index < this.playlist.length - 1) {
                this.play(index);
            }
        },
        shuffle: function (shuffled, playNow) {
            var self = this;

            if (shuffled === undefined) {
                shuffled = !this.shuffled;
            }

            if (shuffled || shuffled !== this.shuffled) {

                $(this.cssSelector.playlist + " ul").slideUp(this.options.playlistOptions.shuffleTime, function () {
                    self.shuffled = shuffled;
                    if (shuffled) {
                        self.playlist.sort(function () {
                            return 0.5 - Math.random();
                        });
                    } else {
                        self._originalPlaylist();
                    }
                    self._refresh(true); // Instant

                    if (playNow || !$(self.cssSelector.jPlayer).data("jPlayer").status.paused) {
                        self.play(0);
                    } else {
                        self.select(0);
                    }

                    $(this).slideDown(self.options.playlistOptions.shuffleTime);
                });
            }
        }
    };
})(jQuery);
