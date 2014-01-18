var UploadAlbum=function(){
	this.audioResources=new Array();
	this.imageResources=new Array();
	this.otherResources=new Array();
	/* cover Image resource */
	this.coverImageResource;
	//code for this resource
	this.code=UtilGetGUID();
	//flag to known howm meny resources rest to upload
	this.uploadResourcesRest=0;

	//Get the json data of the album
	this.getData=function(){
    	var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
    	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
		var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
		var yearEdit   = $("#messic-upload-album-year").data("kendoNumericTextBox");

		var albumSid= 0;
		var authorSid=0;
		var genreSid=0;
		if(titleCombo.value()!=titleCombo.text()){
			albumSid=titleCombo.value();
		}
		if(authorCombo.value()!=authorCombo.text()){
			authorSid=authorCombo.value();
		}
		if(genreCombo.value()!=genreCombo.text()){
			genreSid=genreCombo.value();
		}

    	var albumData={
    		sid: albumSid,
    		code: this.code,
    		name: titleCombo.text(),
    		year: yearEdit.value(),
    		cover:{
    			code:'',
    			fileName:''
    		},
    		author: {
    			sid: authorSid,
    			name: authorCombo.text(),
    		},
    		songs: [],
			artworks: [],
			others: [],
			genre: {
				sid: genreSid,
				name: genreCombo.text()
			},
			comments: $("#messic-upload-album-comments").text()
    	};

    	if(this.coverImageResource){
	    	albumData.cover.code=this.coverImageResource.code;
	    	albumData.cover.fileName=this.coverImageResource.file.name;
    	}

    	for(var i=0;i<this.audioResources.length;i++){
    		albumData.songs.push({
    			code: this.audioResources[i].code,
    			track: parseInt(this.audioResources[i].getSongTrack()),
    			name: this.audioResources[i].getSongName(),
    			fileName: this.audioResources[i].file.name
    		});
    	}
    	for(var i=0;i<this.imageResources.length;i++){
    		albumData.artworks.push({
    			code: this.imageResources[i].code,
    			fileName: this.imageResources[i].file.name
    		});
    	}
    	for(var i=0;i<this.otherResources.length;i++){
    		albumData.others.push({
    			code: this.otherResources[i].code,
    			fileName: this.otherResources[i].file.name
    		});
    	}

    	return albumData;
	}

	/* put an image as cover for the album */
	var selectImageAsCover=function (theImageResource){
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
			if(this.coverImageResource){
				$(this.coverImageResource.domElement).removeClass("messic-upload-song-content-images-cover");
			}
			//we put the new class to the selected cover
			$(theImageResource.domElement).addClass("messic-upload-song-content-images-cover");

			//we save the new image resource selected
			this.coverImageResource=theImageResource;
	}

	/* Determine if a file is a cover for the album */
	var isCoverImage=function (theFile){
		var fileName=theFile.name;
		if(fileName.indexOf('cover')>=0 || fileName.indexOf('front')>=0)
			return true;
		else
			return false;
	}


	/* remove a resource from the list of resources */
	var removeElement=function(resource){
		$(resource.domElement).remove();

		for(var i=0;i<this.audioResources.length;i++)
		{
			if(resource==this.audioResources[i]){
				this.audioResources.splice(i,1);
			}
		}
		for(var i=0;i<this.imageResources.length;i++)
		{
			if(resource==this.imageResources[i]){
				this.imageResources.splice(i,1);
			}
		}
		for(var i=0;i<this.otherResources.length;i++)
		{
			if(resource==this.otherResources[i]){
				this.otherResources.splice(i,1);
			}
		}
	}


	/* Add Files to the  uploadBox */
	this.addFiles=function (receivedFiles){
			this.coverImageResource=null;
			$('#messic-upload-song-content-songs').empty();

			var files=[];
		    // receivedFiles is a FileList of File objects
		    for (var i = 0, f; f = receivedFiles[i]; i++) {
		    	files.push(f);
		    }

		    //Deleting the existing files
		    for (var i = 0, f; f = files[i]; i++) {
				existFile.call(this,f,true);
			}

			//adding the remaining files
			for(var i=0;i<this.audioResources.length;i++)
			{
				files.push(this.audioResources[i].file);
			}
			this.audioResources=new Array();
			//adding image resources
			for(var i=0;i<this.imageResources.length;i++)
			{
				files.push(this.imageResources[i].file);
			}
			this.imageResources=new Array();
			for(var i=0;i<this.otherResources.length;i++)
			{
				files.push(this.otherResources[i].file);
			}
			this.otherResources=new Array();


		    for (var i = 0, f; f = files[i]; i++) {
		    	var code="";
	    		//alert("filetype;"+f.type);
		    	if(f.type.match('image*')){
					//construct the code
					code="";
					code=code+'<li class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
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
					var iresource=new UploadResource(2,f,$(code));
					this.imageResources.push(iresource);
					var ir=this.imageResources.length;

					//Remove element function
					var removeFunction=function(iresource, it){
						var dofunction=function(event){
							removeElement.call(it,iresource);
							UtilShowInfo(messicLang.uploadImageRemoved);
						}
						return dofunction;
					};

					iresource.domElement.find("a").click(removeFunction(iresource,this));

					//select image as a cover function
					var coverFunction=function(iresource,it){
						var dofunction=function(event){
							selectImageAsCover.call(it,iresource);
							UtilShowInfo(messicLang.uploadCoverSelected);
						}
						return dofunction;
					}
					iresource.domElement.find(".messic-upload-song-content-images").click(coverFunction(iresource,this));

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
								ir.domElement.find(".messic-upload-song-content-images").append(img);
				        };
				    })(f,this.imageResources[ir-1]);
					// Read in the image file as a data URL.
					reader.readAsDataURL(f);
		    	}else if(f.type.match('audio.*')){
			    	code=code+'<li class="messic-upload-song-content-songs-filedelete">';
			    	code=code+'  <div class="messic-upload-song-content-header-track">';
			    	code=code+'    <input type="number" value="'+(this.audioResources.length+1)+'" class="messic-upload-song-content-header-tracknumber"/>';
			    	code=code+'  </div>';
			    	code=code+'  <input type="text" class="messic-upload-song-content-header-filename" value="'+UtilRemoveTrackNumberFromFileName(f.name)+'"/>';
			    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
			    	code=code+'    <a href="#">&nbsp;</a>';
			    	code=code+'  </div>';
			    	code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
					code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
					code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
					code=code+'</li>';
					var resource=new UploadResource(1,f,$(code));
					this.audioResources.push(resource);

					var removeFunction=function(resource,it){
						var dofunction=function(event){
							removeElement.call(it,resource);
							UtilShowInfo(messicLang.uploadTrackRemoved);
						}

						return dofunction;
					}
					resource.domElement.find("a").click(removeFunction(resource,this));

					//document.getElementById('messic-upload-song-content-songs').innerHTML += code;
		    	}else{
					code="";
			    	code=code+'<li class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
					code=code+'  <div class="messic-upload-song-content-unknown"></div>';
			    	code=code+'  <div class="messic-upload-song-content-header-filename">'+f.name+'</div>';
			    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
			    	code=code+'    <a href="#">&nbsp;</a>';
			    	code=code+'  </div>';
			    	code=code+'  <div class="messic-upload-song-content-header-filestatus">0%</div>';
					code=code+'  <div class="messic-upload-song-content-header-filesize">'+Math.round(f.size/1024,2)+' Kb</div>';
					code=code+'  <div class="messic-upload-song-content-header-clearer">&nbsp;</div>';
					code=code+'</li>';
					var resource=new UploadResource(3,f,$(code));
					this.otherResources.push(resource);

					var removeFunction=function(resource,it){
						var dofunction=function(event){
							removeElement.call(it,resource);
							UtilShowInfo(messicLang.uploadResourceRemoved);
						}

						return dofunction;
					}
					resource.domElement.find("a").click(removeFunction(resource,this));
		    	}
		    }
			orderAllResources.call(this);
	}

	/* order in the list all the resources that user wants to upload */
	var orderAllResources=function(){
		var divContent=$("#messic-upload-song-content-songs");
		divContent.empty();

		//adding audio resources
		for(var i=0;i<this.audioResources.length;i++)
		{
			divContent.append(this.audioResources[i].domElement);
		}
		//adding image resources
		for(var i=0;i<this.imageResources.length;i++)
		{
			divContent.append(this.imageResources[i].domElement);
		}

		//if there are image resources then we must select a cover
		this.coverImageResource=null;
		if(this.imageResources.length>0){
			for(var i=0;i<this.imageResources.length;i++)
			{
				var isCover=isCoverImage.call(this,this.imageResources[i].file);
				if(isCover){
					selectImageAsCover.call(this,this.imageResources[i]);
				}
			}
			if(!this.coverImageResource){
				selectImageAsCover.call(this,this.imageResources[0]);
			}
		}

		//adding other resources
		for(var i=0;i<this.otherResources.length;i++)
		{
			divContent.append(this.otherResources[i].domElement);
		}
	}


	/* check if the file is already selected to be uploaded.
	   if remove=True then it is removed from the list in order to add the new one */
	var existFile=function (file,remove){
		for(var i=0;i<this.audioResources.length;i++)
		{
			if(this.audioResources[i].file.name==file.name && this.audioResources[i].file.size==file.size){
				if(remove){
					this.audioResources.splice(i,1);
				}
				return true;
			}
		}
		for(var i=0;i<this.imageResources.length;i++)
		{
			if(this.imageResources[i].file.name==file.name && this.imageResources[i].file.size==file.size){
				if(remove){
					this.imageResources.splice(i,1);
				}
				return true;
			}
		}
		for(var i=0;i<this.otherResources.length;i++)
		{
			if(this.otherResources[i].file.name==file.name && this.otherResources[i].file.size==file.size){
				if(remove){
					this.otherResources.splice(i,1);
				}
				return true;
			}
		}

		return false;
	}



	/* Wizard function to try to obtain information from the audio resources to upload */
	this.wizard=function(){
		if(this.audioResources.length>0){
			UtilShowWait(messicLang.uploadAlbumUploadWizard);

			//first, we reset any previos uploaded info
		    $.ajax({
		        url: 'services/albums/clear',  //Server script to process data
		        type: 'POST',
		        //Ajax events
		        success: (function(self){
		        	var myfunction=function(){
		        		for(var i=0;i<self.audioResources.length;i++){
		        				self.audioResources[i].uploaded=false;
		        		}
			        	wizardUploadSongs.call(self);
		        	}
		        	return myfunction;
		        })(this),
		        error: function(){
		        	UtilShowInfo('Messic Server Unknown Error!');
		        },
		        processData: false
		    });
		}else{
			UtilShowInfo(messicLang.uploadAlbumWizardNotracks);
		}
	}

	/* function to upload the audio resources to the server, to obtain wizard tag information from tracks */
	var wizardUploadSongs=function(){
		this.uploadResourcesRest=this.audioResources.length;
		for(var i=0;i<this.audioResources.length;i++){
			var theFile=this.audioResources[i].file;
					//reading the file to show the image
		    		var reader = new FileReader();
					// Closure to capture the file information.
					reader.onload = (function(theFile, albumCode, audioResource, self) {
					//reader.onload = (function(theFile) {
			        	return function(e) {
							    var bin = e.target.result;

							    //code for audio resources: 1000-1999
							     $.ajax({
							        url: 'services/albums/'+albumCode+"?fileName="+escape(theFile.name),
							        type: 'POST',
							        //Ajax events
							        success: (function(it, audioResource){
							        	var myfunction=function(){
											it.uploadResourcesRest=it.uploadResourcesRest-1;
											audioResource.uploaded=true;
											if(it.uploadResourcesRest==0){
												$.getJSON( "services/albums/"+albumCode+"/wizard", function( data ) {
													if(data.author.name){
														$("#messic-upload-album-author").data("kendoComboBox").text(data.author.name);
													}
													if(data.name){
														$("#messic-upload-album-title").data("kendoComboBox").text(data.name);
													}
													if(data.genre.name){
														$("#messic-upload-album-genre").data("kendoComboBox").text(data.genre.name);
													}
													if(data.comments){
														$("#messic-upload-album-comments").text(data.comments);	
													}
													if(data.year){
														$("#messic-upload-album-year").data("kendoNumericTextBox").value(data.year);	
													}

													/*	
													TODO see if the album is new or we are editing an existing one
						                        	var autorCombo = $("#messic-upload-album-author").data("kendoComboBox");
						                        	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
						                        	var autorValue=autorCombo.value();
						                        	var autorText=autorCombo.text();
									            	var titleValue=titleCombo.value();
									            	var titleText=titleCombo.text();
									            	if(autorValue!=autorText && titleValue!=titleText){
														$("#messic-upload-album-editnew").get(0).lastChild.nodeValue = messicLang.uploadAlbumEdit;
									            		$("#messic-upload-album-editnew").attr('class', 'messic-upload-album-edit');
									            	}
									            	*/

													UtilHideWait();
												});
												UtilHideWait();
											}
							        	}
							        	return myfunction;
							        })(self,audioResource),
							        error: (function(it){
							        	var myfunction=function(){
											it.uploadResourcesRest=it.uploadResourcesRest-1;
											if(it.uploadResourcesRest==0){
												UtilHideWait();
											}
							        	}
							        	return myfunction;
							        })(self), 
							        processData: false,
							        data: bin
							    });
				        };
				    })(theFile,this.code, this.audioResources[i], this);
					// Read in the image file as a data URL.
					reader.readAsArrayBuffer(theFile);
		}
	}


	//Validate if the information about the resources of the album is OK.
	this.validate=function(){
		//checking all songs have a name and a track number
		for(var i=0;i<this.audioResources.length;i++){
			var name=this.audioResources[i].getSongName();
			if(!name || name.length==0){
				UtilShowInfo("All tracks must have a name!");
				return false;
			}
			var track=this.audioResources[i].getSongTrack();
			if(!track || track.length==0){
				UtilShowInfo("All songs must have a track number!");
				return false;
			}
		}

		//checking all songs have different names
		for(var i=0;i<this.audioResources.length;i++){
			var name=this.audioResources[i].getSongName();
			var repetitions=0;
			for(var j=0;j<this.audioResources.length;j++){
				var name2=this.audioResources[j].getSongName();
				if(name==name2){
					repetitions=repetitions+1;
				}
			}

			if(repetitions>1){
				UtilShowInfo("Error!! song with name:'"+name + "' is repeated!");
				return false;
			}
		}

		return true;
	}
}