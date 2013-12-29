var imageResources=new Array();
var audioResources=new Array();
var otherResources=new Array();
var coverImageResource;

/* init the upload page */
function initUpload(){
	filesToUpload=[];

		$("#messic-upload-algum-wizard").click(wizard);

        $("#selector").kendoDropDownList({
            change: function () {
                tooltip.show($("#target" + this.value()));
            }
        });

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
            			url: "services/author",
          				type: "GET",
            			dataType: "json"
			        }
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
	            		authorCombo.value(selectedData.genre.sid);
	            		authorCombo.text(selectedData.genre.name);

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
	            		//$("#messic-upload-album-editnew").text("New Album");
	            		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-new');
	            	}
	            },
	        dataSource: {
				schema: {
					model: { id: "sid" }
				},
			    transport: {
        			read: {
            			url: "services/album",
          				type: "GET",
            			dataType: "json"
			        }
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
            			url: "services/genre",
          				type: "GET",
            			dataType: "json"
			        }
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
		uploadsongAddFiles(files);
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
		uploadsongAddFiles(transferFiles);
	});	

}

var uploadSongsToUpload=0;

function wizardDataObtained(){
	showInfo('Yeah! we have a wizard')
}

function wizardSongUploaded(){
	uploadSongsToUpload=uploadSongsToUpload-1;
	if(uploadSongsToUpload==0){
		UtilShowInfo('songs Uploaded');

	    $.ajax({
	        url: 'services/album/wizard',  //Server script to process data
	        type: 'GET',
	        //Ajax events
	        success: wizardDataObtained,
	        /*
	        error: errorHandler,
			*/
	        processData: false
	    });
	}
}
function wizardSongUploadedError(){
	UtilShowInfo('songs Error!');
	uploadSongsToUpload=uploadSongsToUpload-1;
}

function wizardAddAllResources(){
	uploadSongsToUpload=audioResources.length;
	for(var i=0;i<audioResources.length;i++){
		var theFile=audioResources[i].file;

				//reading the file to show the image
	    		var reader = new FileReader();
				// Closure to capture the file information.
				reader.onload = (function(theFile) {
		        	return function(e) {
						    var bin = e.target.result;


						     $.ajax({
						        url: 'services/album/wizard',  //Server script to process data
						        type: 'PUT',
						        //Ajax events
						        success: wizardSongUploaded,
						        error: wizardSongUploadedError,
						        processData: false,
						        data: bin
						    });

						     /*
							var xhr = new XMLHttpRequest();
							xhr.open("PUT", "services/album/wizard");
							xhr.overrideMimeType('text/plain; charset=x-user-defined-binary');
							xhr.sendAsBinary(bin);

							xhr.upload.addEventListener("progress", function(e) {
								if (e.lengthComputable) {
								    var percentage = Math.round((e.loaded * 100) / e.total);
								    showInfo(""+percentage)
								}
							}, false);
							*/

			        };
			    })(theFile);
				// Read in the image file as a data URL.
				reader.readAsArrayBuffer(theFile);

	}
}

/* Wizard function to try to obtain information from the audio resources to upload */
function wizard(){
	if(audioResources.length>0){
	    $.ajax({
	        url: 'services/album/wizard/reset',  //Server script to process data
	        type: 'GET',
	        //Ajax events
	        success: wizardAddAllResources,
	        /*
	        error: errorHandler,
			*/
	        processData: false
	    });
	}else{
		UtilShowInfo("No audio tracks to extract information!");
	}
}

/* Add Files to the  uploadBox */
function uploadsongAddFiles(receivedFiles){
		coverImageResource=null;
		$('#messic-upload-song-content-songs').empty();

		var files=[];
	    // receivedFiles is a FileList of File objects
	    for (var i = 0, f; f = receivedFiles[i]; i++) {
	    	files.push(f);
	    }

	    //Deleting the existing files
	    for (var i = 0, f; f = files[i]; i++) {
			uploadsongExistFile(f,true);
		}

		//adding the remaining files
		for(var i=0;i<audioResources.length;i++)
		{
			files.push(audioResources[i].file);
		}
		audioResources=new Array();
		//adding image resources
		for(var i=0;i<imageResources.length;i++)
		{
			files.push(imageResources[i].file);
		}
		imageResources=new Array();
		for(var i=0;i<otherResources.length;i++)
		{
			files.push(otherResources[i].file);
		}
		otherResources=new Array();


	    for (var i = 0, f; f = files[i]; i++) {
	    	var code="";
	    	var lastTrack=audioResources.length;
			var lastElement=audioResources.length+imageResources.length+otherResources.length;
			var newID="messic-upload-song-content-songs-file"+lastElement;
    		//alert("filetype;"+f.type);
	    	if(f.type.match('image*')){
				//construct the code
				code="";
				code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
				code=code+'  <div class="messic-upload-song-content-images" title="' + f.name+'"></div>';
		    	code=code+'  <div class="messic-upload-song-content-header-filename">'+f.name+'</div>';
				code=code+'  <div class="messic-upload-song-content-header-fileaction">';
				code=code+'    <a href="#">&nbsp;</a>';
				code=code+'  </div>';
				code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
				code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
				code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
				code=code+'</li>';

				//create the resource
				var iresource=new UploadSongResource(2,f,$(code));
				imageResources.push(iresource);
				var ir=imageResources.length;

				//Remove element function
				var removeFunction=function(iresource){
					var dofunction=function(event){
						uploadsongRemoveElement(iresource);
						UtilShowInfo(messicLang.uploadImageRemoved);
					}

					return dofunction;
				}
				iresource.domElement.find("a").click(removeFunction(iresource));

				//select image as a cover function
				var coverFunction=function(iresource){
					var dofunction=function(event){
						uploadsongSelectImageAsCover(iresource);
						UtilShowInfo(messicLang.uploadCoverSelected);
					}
					return dofunction;
				}
				iresource.domElement.find(".messic-upload-song-content-images").click(coverFunction(iresource));

				//reading the file to show the image
	    		var reader = new FileReader();
				// Closure to capture the file information.
				reader.onload = (function(theFile,ir) {
		        	return function(e) {
		        		// Create a new image.
						    var img = new Image();
						    // Set the img src property using the data URL.
						    img.src = e.target.result;
						    // Add the image to the page.
							imageResources[ir-1].domElement.find(".messic-upload-song-content-images").append(img);
			        };
			    })(f,ir);
				// Read in the image file as a data URL.
				reader.readAsDataURL(f);
	    	}else if(f.type.match('audio.*')){
		    	code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete">';
		    	code=code+'  <div class="messic-upload-song-content-header-track">';
		    	code=code+'    <input type="number" value="'+(lastTrack+1)+'" class="messic-upload-song-content-header-tracknumber"/>';
		    	code=code+'  </div>';
		    	code=code+'  <input type="text" class="messic-upload-song-content-header-filename" value="'+UtilGetFileNameWithoutExtension(f.name)+'"/>';
		    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
		    	code=code+'    <a href="#">&nbsp;</a>';
		    	code=code+'  </div>';
		    	code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
				code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
				code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
				code=code+'</li>';
				var resource=new UploadSongResource(1,f,$(code));
				audioResources.push(resource);

				var removeFunction=function(resource){
					var dofunction=function(event){
						uploadsongRemoveElement(resource);
						UtilShowInfo(messicLang.uploadTrackRemoved);
					}

					return dofunction;
				}
				resource.domElement.find("a").click(removeFunction(resource));

				//document.getElementById('messic-upload-song-content-songs').innerHTML += code;
	    	}else{
				code="";
		    	code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
				code=code+'  <div class="messic-upload-song-content-unknown"></div>';
		    	code=code+'  <div class="messic-upload-song-content-header-filename">'+f.name+'</div>';
		    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
		    	code=code+'    <a href="#">&nbsp;</a>';
		    	code=code+'  </div>';
		    	code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
				code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
				code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
				code=code+'</li>';
				var resource=new UploadSongResource(3,f,$(code));
				otherResources.push(resource);

				var removeFunction=function(resource){
					var dofunction=function(event){
						uploadsongRemoveElement(resource);
						UtilShowInfo(messicLang.uploadResourceRemoved);
					}

					return dofunction;
				}
				resource.domElement.find("a").click(removeFunction(resource));
	    	}
	    }
		uploadsongOrderAll();
}


function uploadsongOrderAll(){
	var divContent=$("#messic-upload-song-content-songs");
	divContent.empty();

	//adding audio resources
	for(var i=0;i<audioResources.length;i++)
	{
		divContent.append(audioResources[i].domElement);
	}
	//adding image resources
	for(var i=0;i<imageResources.length;i++)
	{
		divContent.append(imageResources[i].domElement);
	}

	//if there are image resources then we must select a cover
	coverImageResource=null;
	if(imageResources.length>0){
		for(var i=0;i<imageResources.length;i++)
		{
			var isCover=uploadsongIsCoverImage(imageResources[i].file);
			if(isCover){
				uploadsongSelectImageAsCover(imageResources[i]);
			}
		}
		if(!coverImageResource){
			uploadsongSelectImageAsCover(imageResources[0]);
		}
	}

	//adding other resources
	for(var i=0;i<otherResources.length;i++)
	{
		divContent.append(otherResources[i].domElement);
	}
}

/* put an image as cover for the album */
function uploadsongSelectImageAsCover(theImageResource){
		//first we put the new image cover
		var reader = new FileReader();
		reader.onload = (function(theFile) {
        	return function(e) {
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
		if(coverImageResource){
			$(coverImageResource.domElement).removeClass("messic-upload-song-content-images-cover");
		}
		//we put the new class to the selected cover
		$(theImageResource.domElement).addClass("messic-upload-song-content-images-cover");

		//we save the new image resource selected
		coverImageResource=theImageResource;
}

/* Determine if a file is a cover for the album */
function uploadsongIsCoverImage(theFile){
		var fileName=theFile.name;
		if(fileName.indexOf('cover')>0 || fileName.indexOf('front')>0)
			return true;
		else
			return false;
}

/* remove a resource from the list of resources */
function uploadsongRemoveElement(resource){

	$(resource.domElement).remove();

	for(var i=0;i<audioResources.length;i++)
	{
		if(resource==audioResources[i]){
			audioResources.splice(i,1);
		}
	}
	for(var i=0;i<imageResources.length;i++)
	{
		if(resource==imageResources[i]){
			imageResources.splice(i,1);
		}
	}
	for(var i=0;i<otherResources.length;i++)
	{
		if(resource==otherResources[i]){
			otherResources.splice(i,1);
		}
	}
}

/* check if the file is already selected to be uploaded.
   if remove=True then it is removed from the list in order to add the new one */
function uploadsongExistFile(file,remove){
	for(var i=0;i<audioResources.length;i++)
	{
		if(audioResources[i].file.name==file.name && audioResources[i].file.size==file.size){
			if(remove){
				audioResources.splice(i,1);
			}
			return true;
		}
	}
	for(var i=0;i<imageResources.length;i++)
	{
		if(imageResources[i].file.name==file.name && imageResources[i].file.size==file.size){
			if(remove){
				imageResources.splice(i,1);
			}
			return true;
		}
	}
	for(var i=0;i<otherResources.length;i++)
	{
		if(otherResources[i].file.name==file.name && otherResources[i].file.size==file.size){
			if(remove){
				otherResources.splice(i,1);
			}
			return true;
		}
	}

	return false;
}


/*
* Class UploadSongResource
* constructor
* type int, type of resource (1:music, 2:graphic, 3:other)
*/
var UploadSongResource=function (type,file,domElement){
	//resource Type, 1->music, 2->bitmap, 3->other
	this.type=type;
	//Resource File associated
	this.file=file;
	//track, only valid for songs
	this.track=0;
	//the DOM element
	this.domElement=domElement;
	
	this.alert=function(){
		alert("UploadResource, type:"+this.type+", track:"+this.track);
	}
}

