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

//Existing upload processes
var uploadProcesses = new Array();
//new resources to upload
/* image, audio and other resources */
var uploadAlbum;

/* init the upload page */
function initUpload() {

    //just to show a message the first time the user upload songs
    var firstTime = $("#messic-upload-album-container").data("first");
    if (firstTime) {
        uploadFirstTimeMessage();
    }

    //function to leave the upload section
    VAR_changeSection = uploadChangeSection;

    //album to upload
    uploadAlbum = new UploadAlbum();

    //wizard button
    $("#messic-upload-album-wizard").click(function (event) {
        event.preventDefault(); // cancel default behavior
        uploadAlbum.wizard();
    });

    //send button
    $("#messic-upload-album-send").click(function (event) {
        event.preventDefault(); // cancel default behavior
        uploadSend();
    });

    //author combobox
    $("#messic-upload-album-author").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "services/authors",
                dataType: "json",
                data: {
                    filterName: request.term
                },
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.name,
                            id: item.sid,
                            abbrev: item.name
                        };
                    }));
                },
                select: function (event, ui) {
                    $('.tags_id').val(mapping[ui.item.value]);
                }
            });
        },
        minLength: 1,
        select: function (event, ui) {
            console.log(ui.item ?
                "Selected: " + ui.item.value + " aka " + ui.item.id :
                "Nothing selected, input was " + this.value);
        }
    });

    //title combobox
    $("#messic-upload-album-title").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "services/albums",
                dataType: "json",
                data: {
                    filterName: request.term
                },
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.name,
                            id: item.sid,
                            abbrev: item.name
                        };
                    }));
                }
            });
        },
        minLength: 1,
        select: function (event, ui) {
            console.log(ui.item ?
                "Selected: " + ui.item.value + " aka " + ui.item.id :
                "Nothing selected, input was " + this.value);
        }
    });

    //year spinner
    $("#messic-upload-album-year").spinner({
        max: 2100,
        min: 1500
    });

    //volumes spinner
    $("#messic-upload-album-volumes").spinner({
        max: 100,
        min: 1,
        change: function (event, ui) {
            uploadOnVolumesChange(ui.value);
        },
        spin: function (event, ui) {
            uploadOnVolumesChange(ui.value);
        }
    });

    //genre combobox
    $("#messic-upload-album-genre").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "services/genres",
                dataType: "json",
                data: {
                    term: request.term
                },
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.name,
                            id: item.sid,
                            abbrev: item.name
                        };
                    }));
                }
            });
        },
        minLength: 1,
        select: function (event, ui) {
            /*
            log(ui.item ?
                "Selected: " + ui.item.value + " aka " + ui.item.id :
                "Nothing selected, input was " + this.value);
                */
        }
    });

    $("#messic-upload-album-songs").tabs();
    uploadLoadValidation();

    updateInputFilesEvents();
}

//we need to update the inputfile envents each time a new volume is created
//each files is associated with the div container for this volume
function updateInputFilesEvents() {
    //link de click button with the hidden addinput (input type file)
    $(".messic-upload-song-addbutton").click(function () {
        //if the addbutton div is pressed, we simulate an input submit press (need to find the correct one due to you could have diffrent volumes
        $(this).parent().find("input").click();
    });


    //event change for the input type file hidden
    $(".messic-upload-song-addinput").change(function (evt) {
        var files = evt.target.files; // FileList object
        var volumeNumber = $(this).parent().find(".messic-upload-volume").data("volume");
        uploadAlbum.addFiles(files, volumeNumber);
    });

    //For the drop files area
    // Makes sure the dataTransfer information is sent when we
    // Drop the item in the drop box.
    jQuery.event.props.push('dataTransfer');
    $('.messic-upload-song-content-songs').bind('drop', function (e) {
        e.originalEvent.stopPropagation();
        e.originalEvent.preventDefault();

        // This variable represents the files that have been dragged into the drop area
        var transferFiles = e.dataTransfer.files;
        var volumeNumber = $(this).parent().find(".messic-upload-volume").data("volume");
        uploadAlbum.addFiles(transferFiles, volumeNumber);
    });
}

/** function to synchronized the ammount of volumes for the album... increasing or decreasing the number */
function uploadOnVolumesChange(volumes) {
    var containers = $(".messic-upload-container-songtab");
    var tabs = $("#messic-upload-container-tabs li");
    if (containers.length != volumes) {
        //there are a different number, we should create or remove volumes
        if (volumes > containers.length) {
            //we should create new containers for the volumes
            var lastContainer = containers[containers.length - 1];
            for (i = containers.length; i < volumes; i++) {
                var $cloned = $(lastContainer).clone();
                $cloned.attr("id", "messic-upload-container-tab" + (i + 1));
                $(lastContainer).after($cloned);
                lastContainer = $cloned;
                lastContainer.find(".messic-upload-song-content-songs").empty();
                lastContainer.find(".messic-upload-volume").text("" + (i + 1));
                lastContainer.find(".messic-upload-volume").data("volume", (i + 1));

                $(tabs[i - 1]).after($("<li><a href=\"#messic-upload-container-tab" + (i + 1) + "\">Vol." + (i + 1) + "</a></li>"));
            }
        } else {
            //then we should remove existing containers of volumes
            for (i = volumes; i < containers.length; i++) {
                $(containers[i]).remove();
                $(tabs[i]).remove();
            }
        }

        $("#messic-upload-album-songs").tabs("refresh");

        uploadAlbum.updateVolumesNumber(volumes);
        updateInputFilesEvents();

        UtilShowInfo(messicLang.uploadVolumesUpdated);
    }
}

/** Function to show a message the first time the user upload a song */
function uploadFirstTimeMessage() {
    var messages = [
	     messicLang.messicMessagesUpload1_1,
	     "||",
	     messicLang.messicMessagesUpload1_2,
	     "||",
	     messicLang.messicMessagesUpload1_3,
	     "||",
	     messicLang.messicMessagesUpload1_4,
	     "||",
	     messicLang.messicMessagesUpload1_5
	 	].join("");

    UtilShowMessic(messicLang.messicMessagesUpload1, messages);
}

/** function to change section in upload section */
function uploadChangeSection(nextFunction) {
    var nchildren = $(".messic-upload-song-content-songs-filedelete").length;
    var nocompleted = false;
    $(".messic-upload-finishbox-resource-progressbar").each(function (index) {
        if ($(this).width < 100) {
            nocompleted = true;
        }
    });
    if (nchildren > 0 || nocompleted) {
        $.confirm({
            'title': messicLang.uploadAlbumChangeSectionTitle,
            'message': messicLang.uploadAlbumChangeSectionMessage,
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
                    'action': function () {} // Nothing to do in this case. You can as well omit the action property.
                }
            }
        });
    } else {
        nextFunction();
    }
}

/* check if the form is valid */
function uploadValidate() {
    return $("#messic-upload-album-container").valid();
}

/* Validate all the information */
function uploadLoadValidation() {

    var validator = {
        errorPlacement: function (error, element) {
            //console.log(element);
            if ($(element).hasClass("ui-spinner-input")) {
                //this is to avoid the span created by spinners widget, and inset the error label after
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },
        rules: {
            Author: "required",
            Title: "required",
            Year: {
                required: true,
                minlength: 1,
                min: 1500,
                max: 2500
            },
            Genre: "required"
        },
        messages: {
            Author: messicLang.validationRequired,
            Title: messicLang.validationRequired,
            Year: {
                required: messicLang.validationRequired,
                min: "@TODO valor mínimo 1500",
                max: "@TODO valor máximo 2500",
            },
            Genre: messicLang.validationRequired
        }
    };

    $("#messic-upload-album-container").validate(validator);
}


/* Sends all the album information and songs to the server */
function uploadSend() {
    if (uploadValidate()) {
        $.confirm({
            'title': messicLang.uploadAlbumSendConfirmationTitle,
            'message': messicLang.uploadAlbumSendConfirmationMessage,
            'buttons': {
                'Yes': {
                    'title': messicLang.confirmationYes,
                    'class': 'blue',
                    'action': function () {
                        var process = new UploadAlbumProcess(uploadAlbum);
                        uploadProcesses.push(process);
                        process.start();
                        uploadAlbum = new UploadAlbum();
                    }
                },
                'No': {
                    'title': messicLang.confirmationNo,
                    'class': 'gray',
                    'action': function () {
                            UtilShowInfo(messicLang.uploadAlbumSendCancel);
                        } // Nothing to do in this case. You can as well omit the action property.
                }
            }
        });
    }
}