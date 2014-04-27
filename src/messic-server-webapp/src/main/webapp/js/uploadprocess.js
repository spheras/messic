/*
* Class UploadSongResource
* constructor
* type int, type of resource (1:music, 2:graphic, 3:other)
*/
var UploadAlbumProcess=function (album){
	//0 ready
	//1 running
	//2 finished
	this.state=0;
	//album to process
	this.album=album;
	this.albumData=album.getData();

	//to known how many files rest to upload
	var resourceRestToUpload=0;
	var domElement;


	this.start=function(){
		//changing state to running
		this.sate=1;

		//cleaning form data and creating uploading form
    	var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
    	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
		var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
		var yearEdit   = $("#messic-upload-album-year").data("kendoNumericTextBox");
		authorCombo.value("");
		authorCombo.text("");
		titleCombo.value("");
		titleCombo.text("");
		genreCombo.value("");
		genreCombo.text("");
		yearEdit.value(1900);
		$("#messic-upload-album-comments").text("");
		$('#messic-upload-song-content-songs').val('');
		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-new');


		//creating new process and adding to the content
		var code="<div class='messic-upload-finishbox'>";
		code=code+"  <div class='messic-upload-finishbox-cover-content'>";
		code=code+"     <div class='messic-upload-finishbox-cover'></div>";
		code=code+"     <div class='messic-upload-finishbox-authortitle'>"+this.albumData.author.name+"</div>";
		code=code+"     <div class='messic-upload-finishbox-albumtitle'>"+this.albumData.name+"</div>";
		code=code+"  </div>";
		code=code+"  <div class='messic-upload-finishbox-content'></div>";
		code=code+"</div>";
		domElement=$(code);

		//removing the cover
		var oldCover=$("#messic-upload-album-cover img");
		if(oldCover){
			var img=new Image();
			if(oldCover.attr("src")){
				img.src=oldCover.attr("src");
			}
			domElement.find('.messic-upload-finishbox-cover').append(img);
		}
		$("#messic-upload-album-cover").empty();

		//adding the new content finish-box
		$("#messic-page-content").prepend(domElement);

		//Start uploading resources
		this.uploadAll(this.album.audioResources);
		this.uploadAll(this.album.imageResources);
		this.uploadAll(this.album.otherResources);

		//checking if ended
		if(resourceRestToUpload==0){
			ending.call(this);
		}
	}

	this.uploadAll=function(resources){
		resourceRestToUpload=resourceRestToUpload+resources.length;
		for(var i=0;i<resources.length;i++){
			var code="<div class='messic-upload-finishbox-resource'><div class='messic-upload-finishbox-resource-status'></div><div class='messic-upload-finishbox-resource-filename'>"+resources[i].file.name+"</div><div class='messic-upload-finishbox-resource-progress'><div class='messic-upload-finishbox-resource-progressbar'></div></div></div>";
			var resourceElement=$(code);
			resources[i].domElement=resourceElement;
			domElement.find('.messic-upload-finishbox-content').append(resourceElement);

			if(!resources[i].uploaded){

				//reading the file to show the image
	    		var reader = new FileReader();
				// Closure to capture the file information.
				reader.onload = (function(resource,album, self) {
		        	return function(e) {
						    var bin = e.target.result;
						     $.ajax({
						        url: 'services/albums/'+album.code+"?fileName="+encodeURIComponent(resource.file.name),
						        type: 'PUT',
						        //Ajax success
						        success: function(){
									resource.domElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
									resource.domElement.find('.messic-upload-finishbox-resource-progressbar').width('100%');
									resourceRestToUpload=resourceRestToUpload-1;
									resource.uploaded=true;
									if(resourceRestToUpload==0){
										ending.call(self);
									}
						        },
						        error: function(){
									resource.domElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-failed');
									resourceRestToUpload=resourceRestToUpload-1;
									if(resourceRestToUpload==0){
										ending();
									}
						        },
								xhr: function()
								{
									 var xhr = new window.XMLHttpRequest();
									 //Upload progress
									 xhr.upload.addEventListener("progress", function(evt){
									   if (evt.lengthComputable) {
									     var percentComplete = evt.loaded / evt.total;
									     //Do something with upload progress
									     //console.log(percentComplete);

											// calculate upload progress
											//var percentage = Math.floor((progress.total / progress.totalSize) * 100);
											// log upload progress to console
											//console.log('progress', percentComplete);
											resource.domElement.find('.messic-upload-finishbox-resource-progressbar').width((percentComplete*100)+'%');
									   }
									 }, false);
									 //Download progress
									 /*
									 xhr.addEventListener("progress", function(evt){
									   if (evt.lengthComputable) {
									     var percentComplete = evt.loaded / evt.total;
									     //Do something with download progress
									     console.log(percentComplete);
									   }
									 }, false); */
									 return xhr;
								},
						        processData: false,
						        data: bin
						    });
			        };
			    })(resources[i],this.album, this);
				// Read in the image file as a data URL.
				reader.readAsArrayBuffer(resources[i].file);
			}else{
				resourceRestToUpload=resourceRestToUpload-1;
				resourceElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
			}
		}
	}

	function ending(){
		$.ajax({
			url: 'services/albums',  //Server script to process data
			type: 'POST',
			success: function(){
				domElement.find('.messic-upload-finishbox-cover').addClass("messic-upload-finishbox-ok");
				UtilShowInfo("Album uploaded successfully!");
			},
			error: function(e){
				domElement.find('.messic-upload-finishbox-cover').addClass("messic-upload-finishbox-fail");
				UtilShowInfo("ERROR uploading album!");
			},
			processData: false,
			data: JSON.stringify(this.albumData),
			contentType: "application/json"
		});
	}
}