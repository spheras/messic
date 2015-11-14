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

var UploadVolume = function (volume, parent) {
    //number of the volume
    this.volumeNumber = volume;

    //the parent UploadAlbum
    this.parentAlbum = parent;

    //we save the div on which is referenced this volume
    this.divContent = $(".messic-upload-song-content-songs")[this.volumeNumber - 1];


    /* resources for the volume*/
    this.audioResources = new Array();
    this.imageResources = new Array();
    this.otherResources = new Array();


    this.getData = function (albumData) {
        for (var i = 0; i < this.audioResources.length; i++) {
            albumData.songs.push({
                volume: this.volumeNumber,
                code: this.audioResources[i].code,
                track: parseInt(this.audioResources[i].getSongTrack()),
                name: this.audioResources[i].getSongName(),
                fileName: this.audioResources[i].file.name
            });
        }
        for (var i = 0; i < this.imageResources.length; i++) {
            albumData.artworks.push({
                volume: this.volumeNumber,
                code: this.imageResources[i].code,
                fileName: this.imageResources[i].file.name
            });
        }
        for (var i = 0; i < this.otherResources.length; i++) {
            albumData.others.push({
                volume: this.volumeNumber,
                code: this.otherResources[i].code,
                fileName: this.otherResources[i].file.name
            });
        }

    }

    /* remove a resource from the list of resources */
    var removeElement = function (resource) {
        $(resource.domElement).remove();

        for (var i = 0; i < this.audioResources.length; i++) {
            if (resource == this.audioResources[i]) {
                this.audioResources.splice(i, 1);
            }
        }
        for (var i = 0; i < this.imageResources.length; i++) {
            if (resource == this.imageResources[i]) {
                this.imageResources.splice(i, 1);
            }
        }
        for (var i = 0; i < this.otherResources.length; i++) {
            if (resource == this.otherResources[i]) {
                this.otherResources.splice(i, 1);
            }
        }
    }


    /* check if the file is already selected to be uploaded.
	   if remove=True then it is removed from the list in order to add the new one */
    var existFile = function (file, remove) {
        for (var i = 0; i < this.audioResources.length; i++) {
            if (this.audioResources[i].file.name == file.name && this.audioResources[i].file.size == file.size) {
                if (remove) {
                    this.audioResources.splice(i, 1);
                }
                return true;
            }
        }
        for (var i = 0; i < this.imageResources.length; i++) {
            if (this.imageResources[i].file.name == file.name && this.imageResources[i].file.size == file.size) {
                if (remove) {
                    this.imageResources.splice(i, 1);
                }
                return true;
            }
        }
        for (var i = 0; i < this.otherResources.length; i++) {
            if (this.otherResources[i].file.name == file.name && this.otherResources[i].file.size == file.size) {
                if (remove) {
                    this.otherResources.splice(i, 1);
                }
                return true;
            }
        }

        return false;
    }


    /* order in the list all the resources that user wants to upload, based on the type of resource (first audio, after images and others) */
    this.orderAllResources = function () {
        $(this.divContent).empty();

        //adding audio resources
        for (var i = 0; i < this.audioResources.length; i++) {
            $(this.divContent).append(this.audioResources[i].domElement);
        }
        this.reorderSongsDivTableByTrackNumber();

        //adding image resources
        for (var i = 0; i < this.imageResources.length; i++) {
            $(this.divContent).append(this.imageResources[i].domElement);
        }

        //adding other resources
        for (var i = 0; i < this.otherResources.length; i++) {
            $(this.divContent).append(this.otherResources[i].domElement);
        }

        //the album must decide what is the cover image
        //we only decide de cover if we are modifying the first volume, or we haven't yet any coverImage
        if (this.volumeNumber == 1 || !this.parentAlbum.coverImageResource) {
            this.parentAlbum.decideCoverImage();
        }
    }

    /**
     * Reorder the songs table div based on the track number
     */
    this.reorderSongsDivTableByTrackNumber = function () {
        var divs = $(this.divContent).find(".messic-upload-song-content-songs-filedelete");

        divs.sort(function (a, b) {
            var atn = $(a).find(".messic-upload-song-content-header-tracknumber").val();
            var btn = $(b).find(".messic-upload-song-content-header-tracknumber").val();
            if (typeof atn === "undefined") {
                return 1;
            }

            if (typeof btn === "undefined") {
                return -1;
            }

            return atn - btn;
        });

        divs.detach();
        for (var i = 0; i < divs.length; i++) {
            $(this.divContent).append(divs[i]);
        }

    }


    //Validate if the information about the resources of the album is OK.
    this.validateData = function () {
        //checking all songs have a name and a track number
        for (var i = 0; i < this.audioResources.length; i++) {
            var name = this.audioResources[i].getSongName();
            if (!name || name.length == 0) {
                UtilShowInfo(messicLang.uploadErrorTracksWithoutName);
                return false;
            }
            var track = this.audioResources[i].getSongTrack();
            if (!track || track.length == 0) {
                UtilShowInfo(messicLang.uploadErrorTrackWithoutNumber);
                return false;
            }
        }

        //checking all songs have different names
        for (var i = 0; i < this.audioResources.length; i++) {
            var name = this.audioResources[i].getSongName();
            var track = this.audioResources[i].getSongTrack();

            var repetitions = 0;
            for (var j = 0; j < this.audioResources.length; j++) {
                var name2 = this.audioResources[j].getSongName();
                if (name == name2) {
                    var track2 = this.audioResources[j].getSongTrack();
                    if (track == track2) {
                        repetitions = repetitions + 1;
                    }
                }
            }

            if (repetitions > 1) {
                UtilShowInfo(messicLang.uploadErrorNameRepeated + name);
                return false;
            }
        }

        return true;
    }


    /* Add Files to the  uploadBox */
    this.addFiles = function (receivedFiles) {
        this.coverImageResource = null;
        $(this.divContent).empty();

        var files = [];
        // receivedFiles is a FileList of File objects
        for (var i = 0, f; f = receivedFiles[i]; i++) {
            files.push(f);
        }

        //Deleting the existing files
        for (var i = 0, f; f = files[i]; i++) {
            existFile.call(this, f, true);
        }

        //adding the remaining files
        for (var i = 0; i < this.audioResources.length; i++) {
            files.push(this.audioResources[i].file);
        }
        this.audioResources = new Array();

        //adding image resources
        for (var i = 0; i < this.imageResources.length; i++) {
            files.push(this.imageResources[i].file);
        }
        this.imageResources = new Array();

        //adding other resources
        for (var i = 0; i < this.otherResources.length; i++) {
            files.push(this.otherResources[i].file);
        }
        this.otherResources = new Array();


        for (var i = 0, f; f = files[i]; i++) {
            var code = "";
            //alert("filetype;"+f.type);
            if (f.type.match('image*')) {
                //construct the code
                code = "";
                code = code + '<li class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
                code = code + '  <div class="messic-upload-song-content-images" title="' + UtilEscapeHTML(f.name) + '"></div>';
                code = code + '  <div class="messic-upload-song-content-header-filename">' + UtilEscapeHTML(f.name) + '</div>';
                code = code + '  <div class="messic-upload-song-content-header-fileaction">';
                code = code + '    <a href="#">&nbsp;</a>';
                code = code + '  </div>';
                code = code + '  <div class="messic-upload-song-content-header-filesize">' + Math.round(f.size / 1024, 2) + ' Kb</div>';
                code = code + '  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
                code = code + '</li>';

                //create the resource
                var iresource = new UploadResource(2, f, this.volumeNumber, $(code));
                this.imageResources.push(iresource);
                var ir = this.imageResources.length;

                //Remove element function
                var removeFunction = function (iresource, it) {
                    var dofunction = function (event) {
                        removeElement.call(it, iresource);
                        UtilShowInfo(messicLang.uploadImageRemoved);
                    }
                    return dofunction;
                };

                iresource.domElement.find("a").click(removeFunction(iresource, this));

                //select image as a cover function
                var coverFunction = function (iresource, it) {
                    var dofunction = function (event) {
                        it.parentAlbum.selectImageAsCover.call(it, iresource);
                    }
                    return dofunction;
                }
                iresource.domElement.find(".messic-upload-song-content-images").click(coverFunction(iresource, this));

                //reading the file to show the image
                var reader = new FileReader();
                // Closure to capture the file information.
                reader.onload = (function (theFile, ir) {
                    return function (e) {
                        // Create a new image.
                        var img = new Image();
                        // Set the img src property using the data URL.
                        img.src = e.target.result;
                        // Add the image to the page.
                        ir.domElement.find(".messic-upload-song-content-images").append(img);
                    };
                })(f, this.imageResources[ir - 1]);
                // Read in the image file as a data URL.
                reader.readAsDataURL(f);
            } else if (f.type.match('audio.*') && !UtilIsPlayListFile(f)) {
                code = code + '<li class="messic-upload-song-content-songs-filedelete">';
                code = code + '  <div class="messic-upload-song-content-header-realfilename">' + f.name + '</div>';
                code = code + '  <div class="messic-upload-song-content-header-track">';
                code = code + '    <input type="number" value="" class="messic-upload-song-content-header-tracknumber"/>';
                code = code + '  </div>';
                code = code + '  <input type="text" class="messic-upload-song-content-header-filename" value=""/>';
                code = code + '  <div class="messic-upload-song-content-header-fileaction">';
                code = code + '    <a href="#">&nbsp;</a>';
                code = code + '  </div>';
                code = code + '  <div class="messic-upload-song-content-header-filesize">' + Math.round(f.size / 1024, 2) + ' Kb</div>';
                code = code + '  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
                code = code + '</li>';
                var resource = new UploadResource(1, f, this.volumeNumber, $(code));
                this.audioResources.push(resource);
                var removeFunction = function (resource, it) {
                    var dofunction = function (event) {
                        removeElement.call(it, resource);
                        UtilShowInfo(messicLang.uploadTrackRemoved);
                    }

                    return dofunction;
                }
                resource.domElement.find("a").click(removeFunction(resource, this));

                $.ajax({
                    url: "services/songs/" + encodeURIComponent(f.name) + "/wizard",
                    dataType: 'json',
                    async: false,
                    success: function (theFile, self, resource) {
                        return function (data) {
                            resource.domElement.find(".messic-upload-song-content-header-tracknumber").val(data.track)
                            resource.domElement.find(".messic-upload-song-content-header-filename").val(data.name)
                        }
                    }(f, this, resource),
                });
            } else {
                code = "";
                code = code + '<li class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
                code = code + '  <div class="messic-upload-song-content-unknown"></div>';
                code = code + '  <div class="messic-upload-song-content-header-filename">' + f.name + '</div>';
                code = code + '  <div class="messic-upload-song-content-header-fileaction">';
                code = code + '    <a href="#">&nbsp;</a>';
                code = code + '  </div>';
                code = code + '  <div class="messic-upload-song-content-header-filesize">' + Math.round(f.size / 1024, 2) + ' Kb</div>';
                code = code + '  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
                code = code + '</li>';
                var resource = new UploadResource(3, f, this.volumeNumber, $(code));
                this.otherResources.push(resource);

                var removeFunction = function (resource, it) {
                    var dofunction = function (event) {
                        removeElement.call(it, resource);
                        UtilShowInfo(messicLang.uploadResourceRemoved);
                    }

                    return dofunction;
                }
                resource.domElement.find("a").click(removeFunction(resource, this));
            }
        }
        this.orderAllResources();

        //------------------------------------------------------------------------------------
        //just setting the upload flag to those songs that have been uploaded, to avoid upload again
        var filesToUpload = [];
        for (var i = 0; i < this.audioResources.length; i++) {
            var newFile = {
                fileName: this.audioResources[i].file.name,
                size: this.audioResources[i].file.size
            }
            filesToUpload.push(newFile);
        }
        var self = this;
        $.ajax({
            url: 'services/albums/clear?albumCode=' + self.parentAlbum.code,
            async: false,
            type: 'POST',
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    for (var j = 0; j < self.audioResources.length; j++) {
                        if (self.audioResources[j].file.name == data[i].fileName) {
                            if (self.audioResources[j].file.size == data[i].size) {
                                self.audioResources[j].uploaded = true;
                                break;
                            }
                        }
                    }
                }
            },
            processData: false,
            data: JSON.stringify(filesToUpload),
            contentType: "application/json"
        });
        //------------------------------------------------------------------------------------

    }
}