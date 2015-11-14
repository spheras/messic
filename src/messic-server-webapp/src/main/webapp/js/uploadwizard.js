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
var UploadWizard = function (albumVolumes, albumcode) {

    //we need the audioresources to work with
    this.volumes = albumVolumes;
    //the album code
    this.code = albumcode;

    /* MAIN Wizard function to try to obtain information from the audio resources to upload */
    this.wizard = function () {

        //files to be uploaded to the server
        var filesToUpload = [];

        for (var i = 0; i < this.volumes.length; i++) {
            if (this.volumes[i].audioResources.length > 0) {
                for (var j = 0; j < this.volumes[i].audioResources.length; j++) {
                    var newFile = {
                        volume: i + 1,
                        fileName: this.volumes[i].audioResources[j].file.name,
                        size: this.volumes[i].audioResources[j].file.size
                    }
                    filesToUpload.push(newFile);
                }
            }
        }

        if (filesToUpload.length > 0) {
            var self = this;

            $.ajax({
                url: 'services/albums/clear?albumCode=' + this.code, //Server script to process data
                type: 'POST',
                success: function (data) {
                    UtilHideWait();
                    for (var i = 0; i < self.volumes.length; i++) {
                        for (var j = 0; j < self.volumes[i].audioResources.length; j++) {
                            self.volumes[i].audioResources[j].uploaded = false;
                        }
                    }
                    wizardUploadSongs.call(self, data);
                },
                processData: false,
                data: JSON.stringify(filesToUpload),
                contentType: "application/json"
            });

            UtilShowWait(messicLang.uploadAlbumUploadWizard);
        } else {
            UtilShowInfo(messicLang.uploadAlbumWizardNotracks);
        }

    }

    /* function to START uploading the audio resources to the server, window showing the uploading process */
    var wizardUploadSongs = function (uploadedFiles) {
        var up = new UploadPool();
        var self = this;
        this.uploadResourcesRest = 0;
        for (var i = 0; i < self.volumes.length; i++) {
            this.uploadResourcesRest = this.uploadResourcesRest + this.volumes[i].audioResources.length;
        }

        var code = "<div id=\"messic-upload-wizard-overlay\">";
        code = code + "  <div id=\"messic-upload-wizard-window\">";
        code = code + "    <div id=\"messic-upload-wizard-title\">" + messicLang.uploadWizardTitle + "</div>";
        code = code + "    <div id=\"messic-upload-wizard-content\"></div>";
        code = code + "    <button id=\"messic-upload-wizard-cancel\">" + messicLang.uploadWizardCancel + "</button>";
        code = code + "  </div>";
        code = code + "</div>";
        $("body").append($(code));

        //to cancel the process
        $("#messic-upload-wizard-cancel").click(function () {
            for (var i = 0; i < self.volumes.length; i++) {
                for (var j = 0; j < self.volumes[i].audioResources.length; j++) {
                    var arxhr = self.volumes[i].audioResources[j].xhr
                    if (arxhr) {
                        arxhr.abort();
                    }
                }
            }
            $("#messic-upload-wizard-overlay").remove();
        });

        //let's start uploading
        for (var i = 0; i < self.volumes.length; i++) {
            for (var j = 0; j < this.volumes[i].audioResources.length; j++) {
                var uploadResource = this.volumes[i].audioResources[j];

                //just to inidicate the volume
                var filenameToShow = UtilEscapeHTML(uploadResource.file.name);
                if (self.volumes.length > 1) {
                    filenameToShow = "[" + (i + 1) + "] - " + filenameToShow;
                }

                //creating the divs to place the filename and the progress
                var code = "<div class=\"messic-upload-finishbox-resource\">";
                code = code + "  <div class=\"messic-upload-finishbox-resource-status\"></div>";
                code = code + "  <div class=\"messic-upload-finishbox-resource-filename\">" + filenameToShow + "</div>";
                code = code + "  <div class=\"messic-upload-finishbox-resource-progress\">";
                code = code + "    <div class=\"messic-upload-finishbox-resource-progressbar\"></div>";
                code = code + "  </div>";
                code = code + "</div>";
                var divcode = $(code);
                $("#messic-upload-wizard-content").append(divcode);


                //var to know if the file is already at the server (server told us previously)
                var uploadedPreviously = false;
                if (uploadedFiles) {
                    for (var k = 0; k < uploadedFiles.length; k++) {
                        if (uploadResource.file.name == uploadedFiles[k].fileName && uploadResource.file.size == uploadedFiles[k].size) {
                            uploadedPreviously = true;
                        }
                    }
                }

                if (uploadedPreviously) {
                    divcode.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
                    divcode.find('.messic-upload-finishbox-resource-progressbar').width('100%');
                    self.uploadResourcesRest = self.uploadResourcesRest - 1;
                    if (self.uploadResourcesRest == 0) {
                        self.wizardObtainInfoForm.call(self, this.code);
                    }
                } else {

                    //success function, when the resources has been uploaded
                    var successFunction = (function (it, audioResource, thedivcode, albumCode) {
                        var myfunction = function () {
                            thedivcode.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
                            thedivcode.find('.messic-upload-finishbox-resource-progressbar').width('100%');

                            it.uploadResourcesRest = it.uploadResourcesRest - 1;
                            audioResource.uploaded = true;
                            if (it.uploadResourcesRest == 0) {
                                it.wizardObtainInfoForm.call(it, albumCode);
                            }
                        }
                        return myfunction;
                    })(this, this.volumes[i].audioResources[j], divcode, this.code);

                    //error function, when some error occurs while trying to upload the resource
                    var errorFunction = (function (it) {
                        var myfunction = function () {
                            it.uploadResourcesRest = it.uploadResourcesRest - 1;
                            if (it.uploadResourcesRest == 0) {
                                //TODO
                                alert('not expected error!')
                            }
                        }
                        return myfunction;
                    })(this);

                    var xhrFunction = function (audioResource, thedivcode) {
                        var myfunction = function () {
                            var xhr = new window.XMLHttpRequest();

                            audioResource.xhr = xhr;

                            //Upload progress
                            xhr.upload.addEventListener("progress", function (evt) {
                                if (evt.lengthComputable) {
                                    var percentComplete = evt.loaded / evt.total;
                                    // calculate upload progress
                                    thedivcode.find('.messic-upload-finishbox-resource-progressbar').width((percentComplete * 100) + '%');
                                }
                            }, false);
                            return xhr;
                        }
                        return myfunction;
                    }(this.volumes[i].audioResources[j], divcode);

                    up.addUpload(this.code, uploadResource, successFunction, errorFunction, xhrFunction);
                }
            }
        }
        //starting the pool upload
        up.start();
    }

    /* Once uploaded all the songs, this function organize the form of the wizard to obtain info from different providers */
    this.wizardObtainInfoForm = function (albumCode) {
        var self = this;
        $("#messic-upload-wizard-window").empty();
        UtilShowWait(messicLang.uploadAlbumUploadWizard);

        $.getJSON("services/albums/" + albumCode + "/wizard", function (data) {

            var newcode = "<div id=\"messic-upload-wizard-subtitle\">" + messicLang.uploadWizardSubtitle2 + "</div>";
            newcode = newcode + "<div id=\"messic-upload-wizard-subtitle2\">" + messicLang.uploadWizardSubtitle3 + "</div>";

            if (data.length > 1) {
                newcode = newcode + "<ul class=\"messic-upload-wizard-menu\">";

                for (var i = 1; i < data.length; i++) {
                    var name = data[i].name;
                    newcode = newcode + "<li id=\"messic-upload-wizard-menu-item" + i + "\" title=\"" + UtilEscapeHTML(name) + "\" class=\"messic-upload-wizard-menuitem\" data-pluginname=\"" + UtilEscapeHTML(name) + "\">" + UtilEscapeHTML(name) + "</li>";
                }

                newcode = newcode + "</ul>";
            }
            newcode = newcode + "<div id=\"messic-upload-wizard-plugins-content\"></div>";

            newcode = newcode + "<div id=\"messic-upload-wizard-title\">" + messicLang.uploadWizardTitle2 + "</div>";
            newcode = newcode + "<div id=\"messic-upload-wizard-albuminfo-head\">";
            newcode = newcode + "<div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadAuthorTitle + "</div>";
            newcode = newcode + "<input type=\"text\" id=\"messic-upload-wizard-authorname\" class=\"messic-upload-wizard-albuminfofield\" value=\"" + UtilEscapeHTML(data[0].albums[0].author.name) + "\"/>";
            newcode = newcode + "<div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadAlbumTitle + "</div>";
            newcode = newcode + "<input type=\"text\" id=\"messic-upload-wizard-albumtitle\" class=\"messic-upload-wizard-albuminfofield\" value=\"" + UtilEscapeHTML(data[0].albums[0].name) + "\"/>";
            newcode = newcode + "<div class=\"messic-upload-wizard-albumtitle\" min=\"1900\" max=\"4000\">" + messicLang.uploadYearTitle + "</div>";
            newcode = newcode + "<input type=\"number\" id=\"messic-upload-wizard-albumyear\" class=\"messic-upload-wizard-albuminfofield\" value=\"" + UtilEscapeHTML(data[0].albums[0].year) + "\"/>";
            newcode = newcode + "<div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadGenreTitle + "</div>";
            newcode = newcode + "<input type=\"text\" id=\"messic-upload-wizard-genre\" class=\"messic-upload-wizard-albuminfofield\" value=\"" + UtilEscapeHTML(data[0].albums[0].genre.name) + "\"/>";
            newcode = newcode + "<div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadCommentsTitle + "</div>";
            newcode = newcode + "<textarea  maxlength=\"255\" type=\"text\" id=\"messic-upload-wizard-albumcomments\" class=\"messic-upload-wizard-albuminfofield\">" + UtilEscapeHTML(data[0].albums[0].comments) + "</textarea/>";
            newcode = newcode + "<div id=\"messic-upload-wizard-albuminfo-actions\">";
            newcode = newcode + "  <button id=\"messic-upload-wizard-ok\">" + messicLang.uploadWizardOk + "</button>";
            newcode = newcode + "  <button id=\"messic-upload-wizard-cancel\" onclick=\"$('#messic-upload-wizard-overlay').remove()\">" + messicLang.uploadWizardCancel + "</button>";
            newcode = newcode + "</div>";
            newcode = newcode + "</div>";


            var totalvolumes = data[0].albums[0].songs[data[0].albums[0].songs.length - 1].volume;
            newcode = newcode + "<div id=\"messic-upload-wizard-albuminfo-body-tabs\">";
            newcode = newcode + "<ul id=\"messic-upload-wizard-albuminfo-body-tabheader\">";
            for (var j = 0; j < totalvolumes; j++) {
                newcode = newcode + "<li><a href=\"#messic-upload-wizard-albuminfo-body" + j + "\">Vol " + (j + 1) + "</a></li>";
            }
            newcode = newcode + "</ul>";

            for (var j = 0; j < totalvolumes; j++) {
                newcode = newcode + "<div id=\"messic-upload-wizard-albuminfo-body" + j + "\" class=\"messic-upload-wizard-albuminfo-body-real\">";
                newcode = newcode + "  <div class=\"messic-upload-wizard-albuminfo-albumsong-track-title\" min=\"1\" max=\"100000\">" + messicLang.uploadWizardTrack + "</div>";
                newcode = newcode + "  <div class=\"messic-upload-wizard-albuminfo-albumsong-name-title\">" + messicLang.uploadWizardName + "</div>";
                if (data[0].albums[0].songs) {
                    for (var k = 0; k < data[0].albums[0].songs.length; k++) {
                        var song = data[0].albums[0].songs[k];
                        if (song.volume == j + 1) {
                            newcode = newcode + "<label>" + song.fileName + "</label>";
                            newcode = newcode + "<input type=\"number\" class=\"messic-upload-wizard-albumsong-track\" value=\"" + song.track + "\"/>";
                            newcode = newcode + "<input type=\"text\" class=\"messic-upload-wizard-albumsong-name\" value=\"" + song.name + "\"/>";
                        }
                    }
                }
                newcode = newcode + "</div>";
            }

            newcode = newcode + "</div>";



            var divcode = $(newcode);
            $("#messic-upload-wizard-window").append(divcode);
            $("#messic-upload-wizard-albuminfo-body-tabs").tabs();

            $("#messic-upload-wizard-ok").click(function () {
                self.wizardAccept();
            });

            UtilHideWait();

            $(".messic-upload-wizard-menuitem").click(function () {
                self.wizardProviderClick.call(this, self);
            });


        }).error(function () {
            UtilShowInfo("ERROR getting info!");
            UtilHideWait();
        });
    }

    /*
     * The accept button functionality. The user wants to put this info into as the album info.
     */
    this.wizardAccept = function () {
        var authorCombo = $("#messic-upload-album-author");
        var titleCombo = $("#messic-upload-album-title");
        var genreCombo = $("#messic-upload-album-genre");
        var yearEdit = $("#messic-upload-album-year");

        authorCombo.val($("#messic-upload-wizard-authorname").val());
        titleCombo.val($("#messic-upload-wizard-albumtitle").val());
        genreCombo.val($("#messic-upload-wizard-genre").val());
        yearEdit.val($("#messic-upload-wizard-albumyear").val());
        var comments = $("#messic-upload-wizard-albumcomments").val();
        if (comments.length > 255) {
            comments = comments.substr(0, 254);
        }
        $("#messic-upload-album-comments").val(comments);

        //trying to catch the songs tracks and names, volume by volume
        var volumedivs = $(".messic-upload-wizard-albuminfo-body-real");
        var volumerealdivs = $(".messic-upload-container-songtab");
        var totalvolumes = volumedivs.length;

        for (var i = 0; i < totalvolumes; i++) {

            var divtracks = $(volumedivs[i]).find(".messic-upload-wizard-albumsong-track");
            var divnames = $(volumedivs[i]).find(".messic-upload-wizard-albumsong-name");
            var divfilenames = $(volumedivs[i]).find("label");
            var divrealfilenames = $(volumerealdivs[i]).find(".messic-upload-song-content-header-realfilename");
            var divfilerows = $(volumerealdivs[i]).find(".messic-upload-song-content-songs-filedelete");

            for (var j = 0; j < divtracks.length; j++) {
                var track = divtracks.eq(j).val();
                var name = divnames.eq(j).val();
                var filename = divfilenames.eq(j).text();
                for (var k = 0; k < divrealfilenames.length; k++) {
                    if (divrealfilenames.eq(k).text() == filename) {
                        var divfiletrack = divfilerows.eq(k).find(".messic-upload-song-content-header-tracknumber");
                        if (divfiletrack.length > 0) {
                            divfiletrack.val(track)
                        }
                        var divfilename = divfilerows.eq(k).find(".messic-upload-song-content-header-filename");
                        if (divfilename.length > 0) {
                            divfilename.val(name)
                        }
                    }
                }
            }

            this.volumes[i].reorderSongsDivTableByTrackNumber();
        }

        $("#messic-upload-wizard-overlay").remove();

        //this.uploadReorderSongs();
        //reorderSongsDivTableByTrackNumber();
    }

    /*
     * This shows the provider info 
     */
    this.wizardProviderClick = function (self) {
        $(".messic-upload-wizard-menuitem-selected").removeClass("messic-upload-wizard-menuitem-selected");
        $(this).addClass("messic-upload-wizard-menuitem-selected");

        var pluginname = $(this).data("pluginname");
        if (pluginname) {
            var albumTitle = $("#messic-upload-wizard-albumtitle").val();
            var authorName = $("#messic-upload-wizard-authorname").val();
            if (albumTitle.length <= 0 && authorName.length <= 0) {
                UtilShowInfo(messicLang.uploadWizardAtLeast);
                return;
            }

            var albumHelpInfo = {
                name: $("#messic-upload-wizard-albumtitle").val(),
                year: $("#messic-upload-wizard-albumyear").val(),
                author: {
                    name: $("#messic-upload-wizard-authorname").val()
                },
                genre: {
                    name: $("#messic-upload-wizard-genre").val()
                },
                comments: $("#messic-upload-wizard-albumcomments").val(),
                songs: []
            }

            var curTabPanel = $(".messic-upload-wizard-albuminfo-body-real")[$("#messic-upload-wizard-albuminfo-body-tabs").tabs('option', 'active')];
            var bodychildren = $(curTabPanel).find("input");
            for (var i = 0; i < bodychildren.length; i = i + 2) {
                var song = {
                    track: bodychildren.eq(i).val(),
                    name: bodychildren.eq(i + 1).val()
                }
                albumHelpInfo.songs.push(song);
            }

            UtilShowWait(messicLang.uploadWizardWaitProvider + " " + pluginname);

            $.ajax({
                url: "services/albums/" + self.code + "/wizard?pluginName=" + pluginname, //Server script to process data
                type: 'POST',
                success: function (data) {
                    UtilHideWait();
                    if (data[0].albums && data[0].albums.length > 0) {
                        var plugincode = "<div id=\"messic-upload-wizard-plugin-content\">";
                        plugincode = plugincode + "  <div class=\"messic-upload-wizard-albuminfo-title\">" + messicLang.uploadWizardResultsFromProvider + " " + pluginname + "</div>";

                        var albums = data[0].albums;
                        for (var j = 0; j < albums.length; j++) {
                            plugincode = plugincode + "<div class=\"messic-upload-wizard-pluginresult\">";

                            if ((bodychildren.length / 2) != albums[j].songs.length) {
                                plugincode = plugincode + "<div class=\"messic-upload-wizard-pluginresult-warning\">";
                                plugincode = plugincode + messicLang.uploadWizardPluginWarning;
                                plugincode = plugincode + "</div>";
                            }

                            plugincode = plugincode + "  <div class=\"messic-upload-wizard-albuminfo-actions\">";
                            plugincode = plugincode + "    <a class=\"messic-upload-wizard-albuminfo-action-select-head messic-upload-wizard-albuminfo-action-select\" href=\"#\">" + messicLang.uploadWizardChooseHead + "</a>";
                            plugincode = plugincode + "    <a class=\"messic-upload-wizard-albuminfo-action-select-body messic-upload-wizard-albuminfo-action-select\" href=\"#\">" + messicLang.uploadWizardChooseBody + "</a>";
                            /*
                            this button remove the option, temporaly commented
                            plugincode = plugincode + "    <a href=\"#\" onclick=\"$(this).parent().parent().remove(); if($('.messic-upload-wizard-pluginresult').length<=0){$('#messic-upload-wizard-plugins-content').empty();$('.messic-upload-wizard-menuitem-selected').removeClass('messic-upload-wizard-menuitem-selected');}\">" + messicLang.uploadWizardRemove + "</a>";
                            */
                            plugincode = plugincode + "  </div>";

                            plugincode = plugincode + "  <div class=\"messic-upload-wizard-albuminfo-head\">";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadAuthorTitle + "</div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-author\" title=\"" + UtilEscapeHTML(albums[j].author.name) + "\">" + UtilEscapeHTML(albums[j].author.name) + "<div class=\"messic-upload-wizard-albuminfo-action-copy-authorname messic-upload-wizard-albuminfo-action-copy\"></div></div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadAlbumTitle + "</div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-title\" title=\"" + UtilEscapeHTML(albums[j].name) + "\">" + UtilEscapeHTML(albums[j].name) + "<div class=\"messic-upload-wizard-albuminfo-action-copy-albumname messic-upload-wizard-albuminfo-action-copy\"></div></div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadYearTitle + "</div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-year\" title=\"" + UtilEscapeHTML(albums[j].year) + "\">" + UtilEscapeHTML(albums[j].year) + "<div class=\"messic-upload-wizard-albuminfo-action-copy-albumyear messic-upload-wizard-albuminfo-action-copy\"></div></div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadGenreTitle + "</div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-genre\" title=\"" + UtilEscapeHTML(albums[j].genre.name) + "\">" + UtilEscapeHTML(albums[j].genre.name) + "<div class=\"messic-upload-wizard-albuminfo-action-copy-genrename messic-upload-wizard-albuminfo-action-copy\"></div></div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albumtitle\">" + messicLang.uploadCommentsTitle + "</div>";
                            plugincode = plugincode + "    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-comments\" title=\"" + UtilEscapeHTML(albums[j].comments) + "\">" + UtilEscapeHTML(albums[j].comments) + "<div class=\"messic-upload-wizard-albuminfo-action-copy-comments messic-upload-wizard-albuminfo-action-copy\"></div></div>";
                            plugincode = plugincode + "  </div>";
                            plugincode = plugincode + "  <div class=\"messic-upload-wizard-albuminfo-body\">";
                            if (albums[j].songs) {
                                for (var k = 0; k < albums[j].songs.length; k++) {
                                    var song = albums[j].songs[k];
                                    plugincode = plugincode + "<div class=\"messic-upload-wizard-albumsong-track\">" + song.track + "</div>";
                                    plugincode = plugincode + "<div class=\"messic-upload-wizard-albumsong-name\" title=\"" + UtilEscapeHTML(song.name) + "\">" + UtilEscapeHTML(song.name) + "</div>";
                                }
                            }
                            plugincode = plugincode + "  </div>";
                            plugincode = plugincode + "  <div class=\"divclearer\"></div>";
                            plugincode = plugincode + "</div>";
                        }
                        plugincode = plugincode + "<div class=\"divclearer\"></div>";
                        var divplugincode = $(plugincode);

                        //divs to copy specifical content-----------------------------
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-copy-authorname").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchCopyAuthorName($(this).parent().parent());
                        });
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-copy-albumname").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchCopyAlbumTitle($(this).parent().parent());
                        });
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-copy-albumyear").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchCopyAlbumYear($(this).parent().parent());
                        });
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-copy-genrename").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchCopyAlbumGenre($(this).parent().parent());
                        });
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-copy-comments").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchCopyAlbumComments($(this).parent().parent());
                        });
                        //-----------------------------------------------------------

                        //div to copy the whole head info
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-select-head").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatchGeneralInfo($(this).parent().parent());
                        });

                        //div to copy the songs info
                        $(divplugincode).find(".messic-upload-wizard-albuminfo-action-select-body").click(function () {
                            var wizardMatcher = new UploadWizardMatcher();
                            wizardMatcher.uploadwizardmatch($(this).parent().parent());
                        });

                        $("#messic-upload-wizard-plugins-content").empty();
                        $("#messic-upload-wizard-plugins-content").append(divplugincode);
                    } else {
                        UtilShowInfo(messicLang.uploadAlbumWizardNothingfound);
                    }
                },
                processData: false,
                data: JSON.stringify(albumHelpInfo),
                contentType: "application/json"
            });
        } else {
            self.wizardObtainInfoForm.call(self, self.code);
        }
    }

}