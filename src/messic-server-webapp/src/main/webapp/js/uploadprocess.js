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
		$("#messic-upload-song-content-songs").empty();


		//creating new process and adding to the content
		var code="<div class=\"messic-upload-finishbox\">";
		code=code+"  <div class=\"messic-upload-finishbox-cover-content\">";
		code=code+"     <div class=\"messic-upload-finishbox-cover\"></div>";
		code=code+"     <div class=\"messic-upload-finishbox-authortitle\">"+UtilEscapeHTML(this.albumData.author.name)+"</div>";
		code=code+"     <div class=\"messic-upload-finishbox-albumtitle\">"+UtilEscapeHTML(this.albumData.name)+"</div>";
		code=code+"  </div>";
		code=code+"  <div class=\"messic-upload-finishbox-content\"></div>";
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
		var up=new UploadPool();
		
		resourceRestToUpload=resourceRestToUpload+resources.length;
		for(var i=0;i<resources.length;i++){
			var code="<div class=\"messic-upload-finishbox-resource\"><div class=\"messic-upload-finishbox-resource-status\"></div><div class=\"messic-upload-finishbox-resource-filename\">"+UtilEscapeHTML(resources[i].file.name)+"</div><div class=\"messic-upload-finishbox-resource-progress\"><div class=\"messic-upload-finishbox-resource-progressbar\"></div></div></div>";
			var resourceElement=$(code);
			resources[i].domElement=resourceElement;
			domElement.find('.messic-upload-finishbox-content').append(resourceElement);

			if(!resources[i].uploaded){
				
				var successFunction=function(resource,album,self){
					var myfunction=function(){
						resource.domElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
						resource.domElement.find('.messic-upload-finishbox-resource-progressbar').width('100%');
						resourceRestToUpload=resourceRestToUpload-1;
						resource.uploaded=true;
						if(resourceRestToUpload==0){
							ending.call(self);
						}
					}
					return myfunction;
		        }(resources[i],this.album, this);
					
				var errorFunction=function(resource,album,self){
					var myfunction=function(){
						resource.domElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-failed');
						resourceRestToUpload=resourceRestToUpload-1;
						if(resourceRestToUpload==0){
							ending();
						}
					}
					return myfunction;
		        }(resources[i],this.album, this);

				var xhrFunction=function(resource,album,self){
					var myfunction=function(){
						 var xhr = new window.XMLHttpRequest();
						 //Upload progress
						 xhr.upload.addEventListener("progress", function(evt){
						   if (evt.lengthComputable) {
						     var percentComplete = evt.loaded / evt.total;
						     //Do something with upload progress
						     //console.log(percentComplete);

								// calculate upload progress
								resource.domElement.find('.messic-upload-finishbox-resource-progressbar').width((percentComplete*100)+'%');
						   }
						 }, false);
						 return xhr;
					}
					return myfunction;
		        }(resources[i],this.album, this);

				up.addUpload(this.album.code, resources[i].file, successFunction, errorFunction, xhrFunction);

			}else{
				resourceRestToUpload=resourceRestToUpload-1;
				resourceElement.find('.messic-upload-finishbox-resource-progressbar').width('100%');
				resourceElement.find('.messic-upload-finishbox-resource-status').addClass('messic-upload-finished');
			}
		}
		//starting the pool upload
		up.start();
		
	}

	function ending(){
		$.ajax({
			url: 'services/albums',  //Server script to process data
			type: 'POST',
			success: function(){
				domElement.find('.messic-upload-finishbox-cover').addClass("messic-upload-finishbox-ok");
				UtilShowInfo(messicLang.uploadSucesfull);
			},
			error: function(e){
				domElement.find('.messic-upload-finishbox-cover').addClass("messic-upload-finishbox-fail");
				UtilShowInfo(messicLang.uploadError);
			},
			processData: false,
			data: JSON.stringify(this.albumData),
			contentType: "application/json"
		});
	}
}
