/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the 
 *  JavaScript code in this page.
 *
 * Copyright (C) 2013  José Amuedo Salmerón
 *
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
var uploadProcesses=new Array();
//new resources to upload
/* image, audio and other resources */
var uploadAlbum;
/* kendo validator for the form */
var uploadValidator;

/* init the upload page */
function initUpload(){

		//just to show a message the first time the user upload songs
		var  firstTime=$("#messic-upload-album-container").data("first");
		if(firstTime){
			uploadFirstTimeMessage();
		}
	
		//function to leave the upload section
		VAR_changeSection=uploadChangeSection;

		//album to upload
		uploadAlbum=new UploadAlbum();
		
		//validator to validate the fields of the form
		uploadValidator = $("#messic-upload-album-container").kendoValidator().data("kendoValidator");

		$("#messic-upload-album-wizard").click(function(){uploadAlbum.wizard();});
		$("#messic-upload-album-send").click(uploadSend);

	    $("#messic-upload-album-author").kendoComboBox({
	        placeholder: messicLang.uploadAuthorPlaceholder,
	        dataTextField: "name",
	        dataValueField: "sid",
	        filter: "contains",
	        autoBind: false,
	        minLength: 1,
            change: uploadAuthorComboChange,
	        dataSource: {
			   transport: {
        			read: {
            			url: "services/authors",
          				type: "GET",
            			dataType: "json"
			        }
			    },
			    schema: {
					data: function(response) {
						return response;
					},
					model: { id: "sid" }
				}
	        }
	    });

	    $('#messic-upload-album-year').kendoNumericTextBox({
	    	placeholder: messicLang.uploadYearPlaceholder,
	    	format: "#0",
	    	decimals: 0,
	    	max: 2100,
	    	min: 1500,
	    	spinners: true,
	    	step: 1
	    });

	    $("#messic-upload-album-title").kendoComboBox({
	        placeholder: messicLang.uploadTitlePlaceholder,
	        dataTextField: "name",
	        dataValueField: "sid",
	        filter: "contains",
	        autoBind: false,
	        minLength: 1,
	            change: uploadTitleComboChange,
	        dataSource: {
				schema: {
					model: { id: "sid" }
				},
			    transport: {
        			read: {
            			url: "services/albums",
          				type: "GET",
            			dataType: "json"
			        }
			    },
			    schema: {
					data: function(response) {
						return response;
					},
					model: { id: "sid" }
				}
	        }
	    });

	    $("#messic-upload-album-genre").kendoComboBox({
	        placeholder: messicLang.uploadGenrePlaceholder,
	        dataTextField: "name",
	        dataValueField: "sid",
	        filter: "contains",
	        autoBind: false,
	        minLength: 1,
	        dataSource: {
			   transport: {
        			read: {
            			url: "services/genres",
          				type: "GET",
            			dataType: "json"
			        }
			    },
			    schema: {
					data: function(response) {
						return response;
					},
					model: { id: "sid" }
				}
	        }
	    });


	//link de click button with the hidden addinput (input type file)
	$("#messic-upload-song-addbutton").click(function(){
		$("#messic-upload-song-addinput").click();
	});
	//event change for the input type file hidden
	$("#messic-upload-song-addinput").change(function(evt){
		var files = evt.target.files; // FileList object
		uploadAlbum.addFiles(files);
	});

	//For the drop files area
	// Makes sure the dataTransfer information is sent when we
	// Drop the item in the drop box.
	jQuery.event.props.push('dataTransfer');
	$('#messic-upload-song-content-songs').bind('drop', function(e) {
		e.originalEvent.stopPropagation();
		e.originalEvent.preventDefault();

		// This variable represents the files that have been dragged into the drop area
		var transferFiles = e.dataTransfer.files;
		uploadAlbum.addFiles(transferFiles);
	});

}

/** Function to show a message the first time the user upload a song */
function uploadFirstTimeMessage(){
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
	
	UtilShowMessic(messicLang.messicMessagesUpload1,messages);
}

/** function to change section in upload section */
function uploadChangeSection(nextFunction){
	var nchildren=$(".messic-upload-song-content-songs-filedelete").length;
	var nocompleted=false;
	$(".messic-upload-finishbox-resource-progressbar").each(function(index){
		if($(this).width<100){
			nocompleted=true;
		}
	});
	if(nchildren>0 || nocompleted){
		$.confirm({
	        'title'		: messicLang.uploadAlbumChangeSectionTitle,
	        'message'	: messicLang.uploadAlbumChangeSectionMessage,
	        'buttons'	: {
	            'Yes'	: {
	            	'title' : messicLang.confirmationYes,
	                'class'	: 'blue',
	                'action': function(){
	                	nextFunction();
	                }
	            },
	            'No'	: {
	            	'title' : messicLang.confirmationNo,
	                'class'	: 'gray',
	                'action': function(){
	                }	// Nothing to do in this case. You can as well omit the action property.
	            }
	        }
			});
	}else{
    	nextFunction();
	}
}
//change event for the combo box of authors
function uploadAuthorComboChange(){
	var autorCombo = $("#messic-upload-album-author").data("kendoComboBox");
	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
	var autorValue=autorCombo.value();
	var autorText=autorCombo.text();
	if(autorValue!=autorText){
		//UtilShowInfo("adding filter");
		//user selected a value from the combo
		//we filter the titles availables only for those of the author selected
    	var valueFilter={ field: "author.sid", operator: "eq", value: parseInt(autorCombo.value()) };
		titleCombo.dataSource.filter(valueFilter);

		//remove the text and value of the title combo
    	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
    	var titleValue=titleCombo.value("");
    	var titleText=titleCombo.text("");
	}else{
		//UtilShowInfo("removing filter");
		//user selected a value not from the combo
		//removing filter
		titleCombo.dataSource.filter([]);
		$("#messic-upload-album-editnew").get(0).lastChild.nodeValue = messicLang.uploadAlbumNew;
		//$("#messic-upload-album-editnew").text("New Album");
		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-new');
	}
}
//change event for the combo box of album title
function uploadTitleComboChange(){
	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
	var titleValue=titleCombo.value();
	var titleText=titleCombo.text();
	if(titleValue!=titleText){
		//User have selected an existing album
		var selectedData=titleCombo.dataSource.get(parseInt(titleValue));
		var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
		authorCombo.value(selectedData.author.sid);
		authorCombo.text(selectedData.author.name);
		var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
		genreCombo.value(selectedData.genre.sid);
		genreCombo.text(selectedData.genre.name);

		if(selectedData.comments){
    		$("#messic-upload-album-comments").text(selectedData.comments);
		}else{
    		$("#messic-upload-album-comments").text("");
		}

		var yearEdit=$("#messic-upload-album-year").data("kendoNumericTextBox");
		if(selectedData.year){
    		yearEdit.value(selectedData.year);
		}else{
    		yearEdit.value("");
		}


		$("#messic-upload-album-editnew").get(0).lastChild.nodeValue = messicLang.uploadAlbumEdit;
		//$("#messic-upload-album-editnew").text("Existing Album!");
		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-edit');
		//TODO, fill year, genre comments, and cover?
	}else{
		$("#messic-upload-album-editnew").get(0).lastChild.nodeValue = messicLang.uploadAlbumNew;
		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-new');
	}
}


/* Validate all the information */
function uploadValidate(){
	return (uploadAlbum.validate() && uploadValidator.validate());
}


/* Sends all the album information and songs to the server */
function uploadSend(){
	if(uploadValidate()){
	    $.confirm({
	        'title'		: messicLang.uploadAlbumSendConfirmationTitle,
	        'message'	: messicLang.uploadAlbumSendConfirmationMessage,
	        'buttons'	: {
	            'Yes'	: {
	            	'title' : messicLang.confirmationYes,
	                'class'	: 'blue',
	                'action': function(){
	                	var process=new UploadAlbumProcess(uploadAlbum);
	                	uploadProcesses.push(process);
	                	process.start();
	                	uploadAlbum=new UploadAlbum();
	                }
	            },
	            'No'	: {
	            	'title' : messicLang.confirmationNo,
	                'class'	: 'gray',
	                'action': function(){
	                			UtilShowInfo(messicLang.uploadAlbumSendCancel);
	                }	// Nothing to do in this case. You can as well omit the action property.
	            }
	        }
	    });
	}else{
		UtilShowInfo(messicLang.uploadWizardValidationError);
	}
}
