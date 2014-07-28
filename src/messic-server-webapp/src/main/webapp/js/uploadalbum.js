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

var UploadAlbum=function(){
	/* resources */
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
			comments: $("#messic-upload-album-comments").val()
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
			
			//adding other resources
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
					code=code+'  <div class="messic-upload-song-content-images" title="' + UtilEscapeHTML(f.name)+'"></div>';
			    	code=code+'  <div class="messic-upload-song-content-header-filename">'+UtilEscapeHTML(f.name)+'</div>';
					code=code+'  <div class="messic-upload-song-content-header-fileaction">';
					code=code+'    <a href="#">&nbsp;</a>';
					code=code+'  </div>';
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
			    	code=code+'    <input type="number" value="" class="messic-upload-song-content-header-tracknumber"/>';
			    	code=code+'  </div>';
			    	code=code+'  <input type="text" class="messic-upload-song-content-header-filename" value=""/>';
			    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
			    	code=code+'    <a href="#">&nbsp;</a>';
			    	code=code+'  </div>';
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
		    		
		    		$.getJSON("services/songs/" + encodeURIComponent(f.name) + "/wizard",function(theFile,self,resource) {
		    			return function(data){
		    				resource.domElement.find(".messic-upload-song-content-header-tracknumber").val(data.track)
		    				resource.domElement.find(".messic-upload-song-content-header-filename").val(data.name)
		    			}
		    		}(f,this,resource));
		    	}else{
					code="";
			    	code=code+'<li class="messic-upload-song-content-songs-filedelete messic-upload-song-content-songs-image">';
					code=code+'  <div class="messic-upload-song-content-unknown"></div>';
			    	code=code+'  <div class="messic-upload-song-content-header-filename">'+f.name+'</div>';
			    	code=code+'  <div class="messic-upload-song-content-header-fileaction">';
			    	code=code+'    <a href="#">&nbsp;</a>';
			    	code=code+'  </div>';
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

			//------------------------------------------------------------------------------------
			//just setting the upload flag to those songs that have been uploaded, to avoid upload again
			var filesToUpload=[];
			for(var i=0;i<this.audioResources.length;i++){
				var newFile={
						fileName:this.audioResources[i].file.name,
						size:this.audioResources[i].file.size
				}
				filesToUpload.push(newFile);
			}
			var self=this;
			$.ajax({
				url: 'services/albums/clear?albumCode='+self.code,
				async: false,
				type: 'POST',
				success: function(data){
					for(var i=0;i<data.length;i++){
						for(var j=0;j<self.audioResources.length;j++){
							if(self.audioResources[j].file.name==data[i].fileName){
								if(self.audioResources[j].file.size==data[i].size){
									self.audioResources[j].uploaded=true;
									break;
								}
							}
						}
					}
				},
				error: function(e){
				},
				processData: false,
				data: JSON.stringify(filesToUpload),
				contentType: "application/json"
			});			
			//------------------------------------------------------------------------------------

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

			var filesToUpload=[];
			for(var i=0;i<this.audioResources.length;i++){
				var newFile={
						fileName:this.audioResources[i].file.name,
						size:this.audioResources[i].file.size
				}
				filesToUpload.push(newFile);
			}

			var self=this;

			$.ajax({
				url: 'services/albums/clear?albumCode='+this.code,  //Server script to process data
				type: 'POST',
				success: function(data){
					UtilHideWait();
	        		for(var i=0;i<self.audioResources.length;i++){
        				self.audioResources[i].uploaded=false;
	        		}
		        	wizardUploadSongs.call(self,data);
				},
				error: function(e){
					UtilHideWait();
					UtilShowInfo("ERROR uploading songs!");
				},
				processData: false,
				data: JSON.stringify(filesToUpload),
				contentType: "application/json"
			});			

			UtilShowWait(messicLang.uploadAlbumUploadWizard);

		}else{
			UtilShowInfo(messicLang.uploadAlbumWizardNotracks);
		}
	}

	/* function to upload the audio resources to the server, to obtain wizard tag information from tracks */
	var wizardUploadSongs=function(uploadedFiles){
		var up=new UploadPool();
		var self=this;
		this.uploadResourcesRest=this.audioResources.length;
		
		var code="<div id=\"messic-upload-wizard-window\">";
		code=code+"  <div id=\"messic-upload-wizard-title\">"+messicLang.uploadWizardTitle+"</div>";
		code=code+"  <div id=\"messic-upload-wizard-content\"></div>";
		code=code+"  <button id=\"messic-upload-wizard-cancel\">"+messicLang.uploadWizardCancel+"</button>";
		code=code+"</div>";
		$("#messic-page-content").append($(code));
		
		$("#messic-upload-wizard-cancel").click(function(){
			for(var i=0;i<self.audioResources.length;i++){
				var arxhr=self.audioResources[i].xhr
				if(arxhr){
					arxhr.abort();
				}
			}
			$("#messic-upload-wizard-window").remove();
		});
		
		for(var i=0;i<this.audioResources.length;i++){
			var theFile=this.audioResources[i].file;


			var code="<div class=\"messic-upload-finishbox-resource\">";
			code=code+"  <div class=\"messic-upload-finishbox-resource-status\"></div>";
			code=code+"  <div class=\"messic-upload-finishbox-resource-filename\">"+UtilEscapeHTML(theFile.name)+"</div>";
			code=code+"  <div class=\"messic-upload-finishbox-resource-progress\">";
			code=code+"    <div class=\"messic-upload-finishbox-resource-progressbar\"></div>";
			code=code+"  </div>";
			code=code+"</div>";
			var divcode=$(code);
			
			$("#messic-upload-wizard-content").append(divcode);
			
			//var to know if the file is already at the server (server tell us previously)
			var uploadedPreviously=false;
			if(uploadedFiles){
				for(var j=0;j<uploadedFiles.length;j++){
					if(theFile.name==uploadedFiles[j].fileName && theFile.size==uploadedFiles[j].size){
						uploadedPreviously=true;
					}
				}
			}

			if(uploadedPreviously){
				divcode.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
				divcode.find('.messic-upload-finishbox-resource-progressbar').width('100%');
				self.uploadResourcesRest=self.uploadResourcesRest-1;				
				if(self.uploadResourcesRest==0){
					self.wizardObtainInfoForm.call(self,this.code);
				}
			}else{
				var successFunction=(function(it, audioResource,thedivcode, albumCode){
		        	var myfunction=function(){
						thedivcode.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
						thedivcode.find('.messic-upload-finishbox-resource-progressbar').width('100%');
		        		
						it.uploadResourcesRest=it.uploadResourcesRest-1;
						audioResource.uploaded=true;
						if(it.uploadResourcesRest==0){
							it.wizardObtainInfoForm.call(it,albumCode);
						}
		        	}
		        	return myfunction;
	        	})(this,this.audioResources[i],divcode,this.code);
				
				var errorFunction=(function(it){
		        	var myfunction=function(){
						it.uploadResourcesRest=it.uploadResourcesRest-1;
						if(it.uploadResourcesRest==0){
							//TODO
							alert('un error!')
						}
		        	}
		        	return myfunction;
		        })(this);
				
				var xhrFunction=function(audioResource, thedivcode)
				{
					var myfunction=function(){
						 var xhr = new window.XMLHttpRequest();
						 
						 audioResource.xhr=xhr;
						 
						 //Upload progress
						 xhr.upload.addEventListener("progress", function(evt){
						   if (evt.lengthComputable) {
						     var percentComplete = evt.loaded / evt.total;
								// calculate upload progress
								thedivcode.find('.messic-upload-finishbox-resource-progressbar').width((percentComplete*100)+'%');
						   }
						 }, false);
						 return xhr;
					}
					return myfunction;
				}(this.audioResources[i],divcode);
				
				up.addUpload(this.code, theFile, successFunction, errorFunction, xhrFunction);
			}
		}
		//starting the pool upload
		up.start();
	}

	/* Once uploaded all the songs, this function organize the form of the wizard to obtain info from different providers */
	this.wizardObtainInfoForm=function(albumCode){
		var self=this;
		$("#messic-upload-wizard-window").empty();
		UtilShowWait(messicLang.uploadAlbumUploadWizard);

		$.getJSON("services/albums/"+albumCode+"/wizard", function(data) {
			
			var newcode="<div id=\"messic-upload-wizard-subtitle\">"+messicLang.uploadWizardSubtitle2+"</div>";
			newcode=newcode+"<div id=\"messic-upload-wizard-subtitle2\">"+messicLang.uploadWizardSubtitle3+"</div>";
			
			if(data.length>1){
				newcode=newcode+"<ul class=\"messic-upload-wizard-menu\">";
			
				for(var i=1;i<data.length;i++){
					var name=data[i].name;
					newcode=newcode+"<li id=\"messic-upload-wizard-menu-item"+i+"\" title=\""+UtilEscapeHTML(name)+"\" class=\"messic-upload-wizard-menuitem\" data-pluginname=\""+UtilEscapeHTML(name)+"\">"+UtilEscapeHTML(name)+"</li>";
				}
				
				newcode=newcode+"</ul>";
			}
			newcode=newcode+"<div id=\"messic-upload-wizard-plugins-content\"></div>";
			
			newcode=newcode+"<div id=\"messic-upload-wizard-title\">"+messicLang.uploadWizardTitle2+"</div>";
			newcode=newcode+"<div id=\"messic-upload-wizard-albuminfo-head\">";
			newcode=newcode+"<div class=\"messic-upload-wizard-albumtitle\">Author</div>";
			newcode=newcode+"<input type=\"text\" id=\"messic-upload-wizard-authorname\" class=\"messic-upload-wizard-albuminfofield\" value=\""+UtilEscapeHTML(data[0].albums[0].author.name)+"\"/>";
			newcode=newcode+"<div class=\"messic-upload-wizard-albumtitle\">Title</div>";
			newcode=newcode+"<input type=\"text\" id=\"messic-upload-wizard-albumtitle\" class=\"messic-upload-wizard-albuminfofield\" value=\""+UtilEscapeHTML(data[0].albums[0].name)+"\"/>";
			newcode=newcode+"<div class=\"messic-upload-wizard-albumtitle\" min=\"1900\" max=\"4000\">Year</div>";
			newcode=newcode+"<input type=\"number\" id=\"messic-upload-wizard-albumyear\" class=\"messic-upload-wizard-albuminfofield\" value=\""+UtilEscapeHTML(data[0].albums[0].year)+"\"/>";
			newcode=newcode+"<div class=\"messic-upload-wizard-albumtitle\">Genre</div>";
			newcode=newcode+"<input type=\"text\" id=\"messic-upload-wizard-genre\" class=\"messic-upload-wizard-albuminfofield\" value=\""+UtilEscapeHTML(data[0].albums[0].genre.name)+"\"/>";
			newcode=newcode+"<div class=\"messic-upload-wizard-albumtitle\">Comments</div>";
			newcode=newcode+"<textarea  maxlength=\"255\" type=\"text\" id=\"messic-upload-wizard-albumcomments\" class=\"messic-upload-wizard-albuminfofield\">"+UtilEscapeHTML(data[0].albums[0].comments)+"</textarea/>";
			newcode=newcode+"</div>";
			newcode=newcode+"<div id=\"messic-upload-wizard-albuminfo-body\" class=\"messic-upload-wizard-albuminfo-body-real\">";
			newcode=newcode+"  <div class=\"messic-upload-wizard-albuminfo-albumsong-track-title\" min=\"1\" max=\"100000\">"+messicLang.uploadWizardTrack+"</div>";
			newcode=newcode+"  <div class=\"messic-upload-wizard-albuminfo-albumsong-name-title\">"+messicLang.uploadWizardName+"</div>";
			if(data[0].albums[0].songs){
				for(var j=0;j<data[0].albums[0].songs.length;j++){
					var song=data[0].albums[0].songs[j];
					newcode=newcode+"<input type=\"number\" class=\"messic-upload-wizard-albumsong-track\" value=\""+song.track+"\"/>";
					newcode=newcode+"<input type=\"text\" class=\"messic-upload-wizard-albumsong-name\" value=\""+song.name+"\"/>";
				}
			}
			newcode=newcode+"</div>";
			newcode=newcode+"<div id=\"messic-upload-wizard-albuminfo-actions\">";
			newcode=newcode+"  <button id=\"messic-upload-wizard-ok\">Choose this</button>";
			newcode=newcode+"  <button id=\"messic-upload-wizard-cancel\" onclick=\"$('#messic-upload-wizard-window').remove()\">Cancelar</button>";
			newcode=newcode+"</div>";

			
			
			var divcode=$(newcode);
			$("#messic-upload-wizard-window").append(divcode);
			
			$("#messic-upload-wizard-ok").click(function(){
				var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
		    	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
				var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
				var yearEdit   = $("#messic-upload-album-year").data("kendoNumericTextBox");
				
		    	authorCombo.text($("#messic-upload-wizard-authorname").val());
		    	titleCombo.text($("#messic-upload-wizard-albumtitle").val());
		    	genreCombo.text($("#messic-upload-wizard-genre").val());
				yearEdit.value($("#messic-upload-wizard-albumyear").val());
				var comments=$("#messic-upload-wizard-albumcomments").val();
				if(comments.length>255){
					comments=comments.substr(0,254);
				}
	    		$("#messic-upload-album-comments").text(comments);

	    		//trying to catch the songs tracks and names
	    		var divtracks=$(".messic-upload-wizard-albuminfo-body-real .messic-upload-wizard-albumsong-track");
	    		var divnames=$(".messic-upload-wizard-albuminfo-body-real .messic-upload-wizard-albumsong-name");
	    		var divfilerows=$(".messic-upload-song-content-songs-filedelete");
	    		for(var i=0;i<divtracks.length;i++){
	    			var track=divtracks.eq(i).val();
	    			var name=divnames.eq(i).val();
	    			if(divfilerows.length>i){
	    				var divfiletrack=divfilerows.eq(i).find(".messic-upload-song-content-header-tracknumber");
	    				if(divfiletrack.length>0){
	    					divfiletrack.val(track)
	    				}
	    				var divfilename=divfilerows.eq(i).find(".messic-upload-song-content-header-filename");
	    				if(divfilename.length>0){
	    					divfilename.val(name)
	    				}
	    			}
	    		}
	    		
				$("#messic-upload-wizard-window").remove();
			});
			
			UtilHideWait();
			
			$(".messic-upload-wizard-menuitem").click(function(){
				$(".messic-upload-wizard-menuitem-selected").removeClass("messic-upload-wizard-menuitem-selected");
				$(this).addClass("messic-upload-wizard-menuitem-selected");
				
				var pluginname=$(this).data("pluginname");
				if(pluginname){
					var albumTitle= $("#messic-upload-wizard-albumtitle").val();
					var authorName=$("#messic-upload-wizard-authorname").val();
					if(albumTitle.length<=0 && authorName.length<=0){
						UtilShowInfo(messicLang.uploadWizardAtLeast);
						return;
					}
					
					var albumHelpInfo={
							name: $("#messic-upload-wizard-albumtitle").val(),
							year: $("#messic-upload-wizard-albumyear").val(),
							author:{
								name:$("#messic-upload-wizard-authorname").val()
							},
							genre:{
								name:$("#messic-upload-wizard-genre").val()
							},
							comments:$("#messic-upload-wizard-albumcomments").val(),
							songs: []
					}
					
					var bodychildren=$("#messic-upload-wizard-albuminfo-body").children();
					for(var i=0;i<bodychildren.length;i=i+2){
						var song={
								track: bodychildren.eq(i).val(),
								name: bodychildren.eq(i+1).val()
						}
						albumHelpInfo.songs.push(song);
					}
					
					UtilShowWait("Wait until loading info from " + pluginname);

					$.ajax({
						url: "services/albums/"+albumCode+"/wizard?pluginName="+pluginname,  //Server script to process data
						type: 'POST',
						success: function(data){
							UtilHideWait();
							if(data[0].albums && data[0].albums.length>0){
								var plugincode="<div id=\"messic-upload-wizard-plugin-content\">";
								plugincode=plugincode+"  <div class=\"messic-upload-wizard-albuminfo-title\">Results from provider "+pluginname+"</div>";
								
								var albums=data[0].albums;
								for(var j=0;j<albums.length;j++){
									plugincode=plugincode+"<div class=\"messic-upload-wizard-pluginresult\">";
									plugincode=plugincode+"  <div class=\"messic-upload-wizard-albuminfo-actions\">";
									plugincode=plugincode+"    <a class=\"messic-upload-wizard-albuminfo-action-select\" href=\"#\">Select</a>";
									plugincode=plugincode+"    <a href=\"#\" onclick=\"$(this).parent().parent().remove(); if($('.messic-upload-wizard-pluginresult').length<=0){$('#messic-upload-wizard-plugins-content').empty();$('.messic-upload-wizard-menuitem-selected').removeClass('messic-upload-wizard-menuitem-selected');}\">Remove</a>";
									plugincode=plugincode+"  </div>";
									plugincode=plugincode+"  <div class=\"messic-upload-wizard-albuminfo-head\">";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albumtitle\">Author</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-author\" title=\""+UtilEscapeHTML(albums[j].author.name)+"\">"+UtilEscapeHTML(albums[j].author.name)+"</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albumtitle\">Title</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-title\" title=\""+UtilEscapeHTML(albums[j].name)+"\">"+UtilEscapeHTML(albums[j].name)+"</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albumtitle\">Year</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-year\" title=\""+UtilEscapeHTML(albums[j].year)+"\">"+UtilEscapeHTML(albums[j].year)+"</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albumtitle\">Genre</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-genre\" title=\""+UtilEscapeHTML(albums[j].genre.name)+"\">"+UtilEscapeHTML(albums[j].genre.name)+"</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albumtitle\">Comments</div>";
									plugincode=plugincode+"    <div class=\"messic-upload-wizard-albuminfofield messic-upload-wizard-albuminfofield-comments\" title=\""+UtilEscapeHTML(albums[j].comments)+"\">"+UtilEscapeHTML(albums[j].comments)+"</div>";
									plugincode=plugincode+"  </div>";
									plugincode=plugincode+"  <div class=\"messic-upload-wizard-albuminfo-body\">";
									if(albums[j].songs){
										for(var k=0;k<albums[j].songs.length;k++){
											var song=albums[j].songs[k];
											plugincode=plugincode+"<div class=\"messic-upload-wizard-albumsong-track\">"+song.track+"</div>";
											plugincode=plugincode+"<div class=\"messic-upload-wizard-albumsong-name\" title=\""+UtilEscapeHTML(song.name)+"\">"+UtilEscapeHTML(song.name)+"</div>";
										}
									}
									plugincode=plugincode+"  </div>";
									plugincode=plugincode+"  <div class=\"divclearer\"></div>";
									plugincode=plugincode+"</div>";
								}
								plugincode=plugincode+"<div class=\"divclearer\"></div>";
								var divplugincode=$(plugincode);
								
								$(divplugincode).find(".messic-upload-wizard-albuminfo-action-select").click(function(){
									var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
							    	var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
									var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
									var yearEdit   = $("#messic-upload-album-year").data("kendoNumericTextBox");
									
									var divpluginresult=$(this).parent().parent();
									
							    	authorCombo.text(divpluginresult.find(".messic-upload-wizard-albuminfofield-author").text());
							    	titleCombo.text(divpluginresult.find(".messic-upload-wizard-albuminfofield-title").text());
							    	genreCombo.text(divpluginresult.find(".messic-upload-wizard-albuminfofield-genre").text());
									yearEdit.value(divpluginresult.find(".messic-upload-wizard-albuminfofield-year").text());
									var comments=divpluginresult.find(".messic-upload-wizard-albuminfofield-comments").text();
									if(comments.length>255){
										comments=comments.substr(0,254);
									}
						    		$("#messic-upload-album-comments").text(comments);

						    		//trying to catch the songs tracks and names
						    		var divtracks=divpluginresult.find(".messic-upload-wizard-albumsong-track");
						    		var divnames=divpluginresult.find(".messic-upload-wizard-albumsong-name");
						    		var divfilerows=$(".messic-upload-song-content-songs-filedelete");
						    		for(var i=0;i<divtracks.length;i++){
						    			var track=divtracks.eq(i).text();
						    			var name=divnames.eq(i).text();
						    			if(divfilerows.length>i){
						    				var divfiletrack=divfilerows.eq(i).find(".messic-upload-song-content-header-tracknumber");
						    				if(divfiletrack.length>0){
						    					divfiletrack.val(track)
						    				}
						    				var divfilename=divfilerows.eq(i).find(".messic-upload-song-content-header-filename");
						    				if(divfilename.length>0){
						    					divfilename.val(name)
						    				}
						    			}
						    		}
						    		
						    		$("#messic-upload-wizard-window").remove();
								});

								$("#messic-upload-wizard-plugins-content").empty();							
								$("#messic-upload-wizard-plugins-content").append(divplugincode);							
							}else{
								UtilShowInfo("Nothing found! :(")
							}
						},
						error: function(e){
							UtilHideWait();
							UtilShowInfo("ERROR getting info!");
						},
						processData: false,
						data: JSON.stringify(albumHelpInfo),
						contentType: "application/json"
					});			
				}else{
					self.wizardObtainInfoForm.call(self,albumCode);
				}
			});
			

		}).error(function(){
			UtilShowInfo("ERROR getting info!");
			UtilHideWait();
		});
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
