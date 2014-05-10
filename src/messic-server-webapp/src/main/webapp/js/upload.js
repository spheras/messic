
//Existing upload processes
var uploadProcesses=new Array();
//new resources to upload
/* image, audio and other resources */
var uploadAlbum;
/* kendo validator for the form */
var uploadValidator;


/* init the upload page */
function initUpload(){

		//function to leave the upload section
		VAR_changeSection=function(nextFunction){
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
		}

		uploadAlbum=new UploadAlbum();
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
                        change: function(){
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
						},
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
	            change: function(){
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
	            },
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
		UtilShowInfo('Validation error!');
	}
}
