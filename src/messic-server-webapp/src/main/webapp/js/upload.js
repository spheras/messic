var imageResources=new Array();
var audioResources=new Array();
var otherResources=new Array();

/* init the upload page */
function initUpload(){
	filesToUpload=[];

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


/* Add Files to the  uploadBox */
function uploadsongAddFiles(files){
		$('#messic-upload-song-content-songs').empty();

	    //Deleting the existing files
	    for (var i = 0, f; f = files[i]; i++) {
			uploadsongExistFile(f,true);
		}

	    // files is a FileList of File objects. List some properties.
	    for (var i = 0, f; f = files[i]; i++) {

	    	var code="";
	    	var lastTrack=audioResources.length;
			var lastElement=audioResources.length+imageResources.length+otherResources.length;
			var newID="messic-upload-song-content-songs-file"+lastElement;
    		//alert("filetype;"+f.type);
	    	if(f.type.match('image*')){
				code="";
				code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
				code=code+'  <img class="messic-upload-song-content-images" title="' + escape(f.name)+'"/>';
		    	code=code+'  <div class="messic-upload-song-content-header-filename">'+escape(f.name)+'</div>';
				code=code+'  <div class="messic-upload-song-content-header-fileaction">';
				code=code+'    <a href="#">&nbsp;</a>';
				code=code+'  </div>';
				code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
				code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
				code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
				code=code+'</li>';
				var resource=new UploadSongResource(2,f,$(code));
				imageResources.push(resource);
				var ir=imageResources.length;

				var removeFunction=function(resource){
					var dofunction=function(event){
						uploadsongRemoveElement(resource);
					}

					return dofunction;
				}
				resource.domElement.find("a").click(removeFunction(resource));
			
	    		var reader = new FileReader();
				// Closure to capture the file information.
				reader.onload = (function(theFile,ir) {
		        	return function(e) {
						imageResources[ir-1].domElement.find(".messic-upload-song-content-images").attr("src",e.target.result);
			        };
			    })(f,ir);
				// Read in the image file as a data URL.
				reader.readAsDataURL(f);
	    	}else if(f.type.match('audio.*')){
		    	code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete">';
		    	code=code+'  <div class="messic-upload-song-content-header-track">';
		    	code=code+'    <input type="number" value="'+(lastTrack+1)+'" class="messic-upload-song-content-header-tracknumber"/>';
		    	code=code+'  </div>';
		    	code=code+'  <input type="text" class="messic-upload-song-content-header-filename" value="'+UtilGetFileNameWithoutExtension(escape(f.name))+'"/>';
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
					}

					return dofunction;
				}
				resource.domElement.find("a").click(removeFunction(resource));

				//document.getElementById('messic-upload-song-content-songs').innerHTML += code;
	    	}else{
				code="";
		    	code=code+'<li id="'+newID+'" class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
				code=code+'  <div class="messic-upload-song-content-unknown"></div>';
		    	code=code+'  <div class="messic-upload-song-content-header-filename">'+escape(f.name)+'</div>';
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
					}

					return dofunction;
				}
				resource.domElement.find("a").click(removeFunction(resource));
	    	}
	    }
		uploadsongOrderAll();
}


function uploadsongOrderAll(){
	//document.getElementById('messic-upload-song-content-songs').innerHTML="";
	var divContent=$("#messic-upload-song-content-songs");
	divContent.empty();
	for(var i=0;i<audioResources.length;i++)
	{
		//audioResources[i].alert();
		divContent.append(audioResources[i].domElement);
	}
	for(var i=0;i<imageResources.length;i++)
	{
		divContent.append(imageResources[i].domElement);
	}
	for(var i=0;i<otherResources.length;i++)
	{
		divContent.append(otherResources[i].domElement);
	}
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

