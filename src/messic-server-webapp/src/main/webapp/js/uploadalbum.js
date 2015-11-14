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

var UploadAlbum = function () {
    /* cover Image resource */
    this.coverImageResource;
    //code for this resource
    this.code = UtilGetGUID();
    //flag to known how many resources rest to upload
    this.uploadResourcesRest = 0;

    //list of volumes
    this.volumes = new Array();
    this.volumes.push(new UploadVolume(1, this)); //at least one

    //update the ammount of volumes in this album... creating or removing volumes
    this.updateVolumesNumber = function (newVolumeNumber) {
        if (this.volumes.length < newVolumeNumber) {
            for (var i = this.volumes.length; i < newVolumeNumber; i++) {
                this.volumes.push(new UploadVolume(i + 1, this));
            }
        } else {
            this.volumes.splice(newVolumeNumber + 1, this.volumes.length - newVolumeNumber);
        }
    }

    //Get the json data of the album
    this.getData = function () {
        var authorCombo = $("#messic-upload-album-author");
        var titleCombo = $("#messic-upload-album-title");
        var genreCombo = $("#messic-upload-album-genre");
        var yearEdit = $("#messic-upload-album-year");
        var volumeSpin = $("#messic-upload-album-volumes");

        var albumSid = 0;
        var authorSid = 0;
        var genreSid = 0;

        /* TODO averiguar cómo se accede al sid
        if (titleCombo.value() != titleCombo.text()) {
            albumSid = titleCombo.value();
        }
        if (authorCombo.value() != authorCombo.text()) {
            authorSid = authorCombo.value();
        }
        if (genreCombo.value() != genreCombo.text()) {
            genreSid = genreCombo.value();
        }
        */

        var albumData = {
            sid: albumSid,
            code: this.code,
            name: titleCombo.val(),
            volumes: volumeSpin.val(),
            year: yearEdit.val(),
            cover: {
                code: '',
                fileName: ''
            },
            author: {
                sid: authorSid,
                name: authorCombo.val(),
            },
            songs: [],
            artworks: [],
            others: [],
            genre: {
                sid: genreSid,
                name: genreCombo.val()
            },
            comments: $("#messic-upload-album-comments").val()
        };

        if (this.coverImageResource) {
            albumData.cover.code = this.coverImageResource.code;
            albumData.cover.fileName = this.coverImageResource.file.name;
        }

        //now we get the data of all the volumes of the album
        for (var i = 0; i < this.volumes.length; i++) {
            this.volumes[i].getData(albumData);
        }

        return albumData;
    }

    /* put an image as cover for the album */
    this.selectImageAsCover = function (theImageResource) {
        //first we put the new image cover
        var reader = new FileReader();
        reader.onload = (function (theFile) {
            return function (e) {
                // Create a new image
                var img = new Image();
                // Set the img src property using the data URL.
                img.src = e.target.result;
                // Add the image to the page.
                $("#messic-upload-album-cover").empty();
                $("#messic-upload-album-cover").append(img);
            };
        })(theImageResource.file);
        // Read in the image file as a data URL.
        reader.readAsDataURL(theImageResource.file);

        //after we remove the cover from the old resource
        if (this.coverImageResource) {
            $(this.coverImageResource.domElement).removeClass("messic-upload-song-content-images-cover");
        }
        //we put the new class to the selected cover
        $(theImageResource.domElement).addClass("messic-upload-song-content-images-cover");

        //we save the new image resource selected
        this.coverImageResource = theImageResource;

        UtilShowInfo(messicLang.uploadCoverSelected);
    }

    /* Try to Determine if a file is a cover for the album */
    this.isCoverImage = function (theFile) {
        var fileName = theFile.name;
        if (fileName.toUpperCase().indexOf('COVER') >= 0 || fileName.toUpperCase().indexOf('FRONT') >= 0)
            return true;
        else
            return false;
    }

    /* this function determine what image should be the cover for the album*/
    this.decideCoverImage = function () {
        //if there are image resources then we must select a cover from the first volume only
        this.coverImageResource = null;
        if (this.volumes[0].imageResources.length > 0) {
            for (var i = 0; i < this.volumes[0].imageResources.length; i++) {
                var isCover = this.isCoverImage.call(this, this.volumes[0].imageResources[i].file);
                if (isCover) {
                    this.selectImageAsCover.call(this, this.volumes[0].imageResources[i]);
                }
            }
            if (!this.coverImageResource) {
                this.selectImageAsCover.call(this, this.volumes[0].imageResources[0]);
            }
        } else {
            //we must search in all volumes
            for (var v = 0; v < this.volumes.length && !this.coverImageResource; v++) {
                for (var i = 0; i < this.volumes[v].imageResources.length; i++) {
                    var isCover = this.isCoverImage.call(this, this.volumes[v].imageResources[i].file);
                    if (isCover) {
                        this.selectImageAsCover.call(this, this.volumes[v].imageResources[i]);
                    }
                }
            }

            //We select the first one if wasn't selected one
            for (var v = 0; v < this.volumes.length && !this.coverImageResource; v++) {
                if (!this.coverImageResource && this.volumes[v].imageResources.length >= 1) {
                    this.selectImageAsCover.call(this, this.volumes[v].imageResources[0]);
                }
            }
        }

    }

    //Validate if the information about the resources of the album is OK.
    this.validate = function () {
        //let's validate all the volumes
        for (var i = 0; i < this.volumes.length; i++) {
            var result = this.volumes[i].validateData();
            if (result == false) {
                return false;
            }
        }
        return true;
    }


    /* add the file to the volume indicated */
    this.addFiles = function (files, volumeNumber) {
        this.volumes[volumeNumber - 1].addFiles(files);
    }

    /* Wizard function to try to obtain information from the audio resources to upload */
    this.wizard = function () {
        var wizard = new UploadWizard(this.volumes, this.code);
        wizard.wizard();
    }
}