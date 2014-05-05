/* genereated code UID for the album we are seeing */
var VAR_AlbumCode;

/* init the album page */
function initAlbum(){
	$("#messic-album-songs-body-songaction-play").hover(function(){
		$("#messic-playlist-background").addClass("interesting");
	},function(){
		$("#messic-playlist-background").removeClass("interesting");
	});

	$("#messic-album-songs-head-songaction-add").click(function(){
		$("#messic-album-songs-head-songaction-addinput").click();
	});
	//event change for the input type file hidden 
	$("#messic-album-songs-head-songaction-addinput").change(function(evt){
		var files = evt.target.files; // FileList object
		albumAddFiles(files);
	});
	
	VAR_AlbumCode=UtilGetGUID();

	//cleaning all temp files
    $.ajax({
        url: 'services/albums/clear',
        type: 'POST',
        success: (function(){
        	
        }),
        error: (function(){
        	
        })
    });
	
}

/* change the status of the album to editing */
function albumEditStatus(){
	$("#messic-album-menuoption-edit").hide();
	$("#messic-album-menuoption-save").show();
}

/* function that add the selected files by the user to the webpage of the album (need to be saved) */
function albumAddFiles(files){
	albumEditStatus();
	
	for (var i = 0, f; f = files[i]; i++) {
		if(f.type.match('image*')){
			albumAddFileImage(f);
		}else if(f.type.match('audio.*')){
			albumAddFileAudio(f);
		}else{
			albumAddFileOther(f);
		}
	}
}

/* function to add an audio file to the album */
function albumAddFileAudio(file){
	$.getJSON( "services/songs/"+file.name+"/wizard", function( data ) {
		
		var code="<div class=\"messic-album-songs-bodyrow messic-album-songs-bodyrow-new\">";
		code=code+"  <input type='text' class=\"messic-album-songs-bodyfield messic-album-songs-body-songtrack\" value=\""+data.track+"\">";
		code=code+"  <input type='text' class=\"messic-album-songs-bodyfield messic-album-songs-body-songname\" value=\""+data.name+"\">";
		code=code+"  <div class='messic-album-songs-uploading'><div class='messic-album-songs-uploading-percent'></div></div>";
		code=code+"  <div class=\"messic-album-songs-bodyfield messic-album-songs-body-songaction\">";
		code=code+"    <div title=\"Delete song\" class='messic-album-songs-body-songaction-remove' onclick='albumRemoveLocalSong()'></div>";
		code=code+"  </div>";
		code=code+"  <div class=\"divclearer\"></div>";
		code=code+"</div>";

		var newelement=$(code);
		$("#messic-album-songs-body").prepend(newelement);
		var percentdiv=$(newelement).find(".messic-album-songs-uploading-percent");

		//reading the file to show the image
		var reader = new FileReader();
		// Closure to capture the file information.
		reader.onload = function(eldiv){
			var fsend=function(e) {
			    var bin = e.target.result;
			    $.ajax({
			    	url: 'services/albums/'+VAR_AlbumCode+"?fileName="+encodeURIComponent(file.name),
			        type: 'PUT',
			        //Ajax events
			        success: (function(){
			        	 //at the end we put 100% completed, and add filename data to the div (this is also a way to know if the upload have been finished
					     eldiv.width('100');
					     eldiv.data('file',file.name);
			        }),
			        error: (function(){
			        	UtilShowInfo("Error uploading the new song!");			        	
					     eldiv.addClass("messic-album-songs-uploading-percent-failed");							 
			        }),
					xhr: (function()
					{
						 var xhr = new window.XMLHttpRequest();
						 //Upload progress
						 xhr.upload.addEventListener("progress", function(evt){
						   if (evt.lengthComputable) {
						     var percentComplete = evt.loaded / evt.total;
						     //Do something with upload progress
						     console.log(percentComplete);
						     eldiv.width((percentComplete*100)+'%');							 
						   }
						 }, false);
						 return xhr;
					}),
			        processData: false,
			        data: bin
			     });
			}
			
			return fsend;
		}(percentdiv);
					    
		// Read in the image file as a data URL.
		reader.readAsArrayBuffer(file);
	});
}
		
		
		
/* function to add an image file to the album */
function albumAddFileImage(file){
	var code="<div class='messic-album-songs-bodyrow messic-album-songs-bodyrow-artwork messic-album-songs-bodyrow-artwork-new'>";
	code=code+"  <div class='messic-album-songs-bodyfield messic-album-songs-body-artwork' onclick='albumShowLocalArtwork($(this).parent())'></div>";
	code=code+"  <div class=\"messic-album-songs-bodyfield messic-album-songs-body-artworkname\">"+file.name+"</div>";
	code=code+"  <div class=\"messic-album-songs-bodyfield messic-album-songs-body-artworkaction\">";
	code=code+"    <div class='messic-album-songs-body-songaction-show' onclick='albumShowLocalArtwork(\"${artwork.sid}\")'></div>";
	code=code+"    <div class='messic-album-songs-body-songaction-remove' onclick='albumRemoveLocalResource()'></div>";
	code=code+"  </div>";
	code=code+"  <div class=\"divclearer\"></div>";
	code=code+"</div>";
	
	var newelement=$(code);
	$("#messic-album-songs-body").prepend(newelement);
	
	//reading the file to show the image
	var reader = new FileReader();
	// Closure to capture the file information.
	reader.onload = (function(theFile,element) {
    	return function(e) {
    		// Create a new image.
		    var img = new Image();
		    // Set the img src property using the data URL.
		    img.src = e.target.result;
		    // Add the image to the page.
		    element.find(".messic-album-songs-bodyfield.messic-album-songs-body-artwork").append(img);
        };
    })(file,newelement);
	
	// Read in the image file as a data URL.
	reader.readAsDataURL(file);
}
/* function to add an other file to the album */
function albumAddFileOther(file){
	
}

function albumShowArtworkDestroy(){
    $('.messic-album-artwork-show-overlay').remove();
    $('.messic-album-artwork-show').remove();
}

/* function to show a local resource (not uploaded) at the page */
function albumShowLocalArtwork(resource){
    	var code='<div class=\"messic-album-artwork-show-overlay\" onclick=\"albumShowArtworkDestroy()\"></div>';
    	code=code+'<div class=\"messic-album-artwork-show\">';
    	code=code+"   <img></img>";
    	code=code+"</div>";
    	
    	var codeobj=$(code);
    	codeobj.find("img").attr("src",$(resource).find("img").attr("src"));
    	codeobj.hide().appendTo('body');
    	codeobj.fadeIn();
}

/* function to show a resource at the page */
function albumShowArtwork(resourceSid){
    	var code='<div class=\"messic-album-artwork-show-overlay\" onclick=\"albumShowArtworkDestroy()\"></div>';
    	code=code+'<div class=\"messic-album-artwork-show\">';
    	code=code+"   <img src='services/albums/"+resourceSid+"/resource'></img>";
    	code=code+"   <a href='services/albums/"+resourceSid+"/resource' target='_blank'></a>"
    	code=code+"</div>";
    	
    	$(code).hide().appendTo('body').fadeIn();
}

/* delete a song from the album */
function albumRemoveResource(resourceSid, div){

    $.confirm({
        'title'		: "Remove Resource",
        'message'	: "Are you sure? The resource will be completely removed from the system." ,
        'buttons'	: {
		    'Yes'	: {
		    	'title' : "YES, Remove",
		        'class'	: 'blue',
		        'action': function(){
		            	$.ajax({
		            	    url: 'services/albumresources/'+resourceSid,
		            	    type: 'DELETE',
		            	    success: function(result) {
		            	    	UtilShowInfo("Resource removed, RIP");
					$(div).fadeOut();
					$(div).remove();
		            	    },
		    		    fail: function(result){
		    			UtilShowInfo("OhOh! there was some kind of error removing the song.. :( ");
		    		    }
		            	});
				}
		    },
		    'No'	: {
		    	'title' : "NO, DON'T remove",
		        'class'	: 'gray',
		        'action': function(){
				UtilShowInfo("Uff, process cancelled");
			}
		    }
		}
    });

}


/* delete a song from the album */
function albumRemoveSong(songSid, songName, div){

    $.confirm({
        'title'		: "Remove Track",
        'message'	: "Are you sure? The track ("+songName+") will be removed completely from the system." ,
        'buttons'	: {
		    'Yes'	: {
		    	'title' : "YES, Remove",
		        'class'	: 'blue',
		        'action': function(){
		            	$.ajax({
		            	    url: 'services/songs/'+songSid,
		            	    type: 'DELETE',
		            	    success: function(result) {
		            	    	UtilShowInfo("Song removed, RIP");
					$(div).fadeOut();
					$(div).remove();
		            	    },
		    		    fail: function(result){
		    			UtilShowInfo("OhOh! there was some kind of error removing the song.. :( ");
		    		    }
		            	});
				}
		    },
		    'No'	: {
		    	'title' : "NO, DON'T remove",
		        'class'	: 'gray',
		        'action': function(){
				UtilShowInfo("Uff, process cancelled");
			}
		    }
		}
    });

}

/* Show the musicinfo obtained from a plugin in the web page */
function albumShowMusicInfo(authorName, albumName, pluginName, div){
	//selected the new one
	$(".messic-album-plugincontainer-menuitem").removeClass("messic-album-plugincontainer-menuitem-selected");
	$(div).addClass("messic-album-plugincontainer-menuitem-selected");
	
	var contentdiv=$(".messic-album-plugincontainer-content");
	$(contentdiv).empty();
	$(contentdiv).append("<div class='messic-album-plugincontainer-content-wait'>Loading Content from 3rd provider</div>");
	
	$.getJSON( "services/musicinfo?pluginName="+pluginName+"&albumName="+albumName+"&authorName="+authorName, function( data ) {
		var resulthtml=data.htmlContent;
		
		$(contentdiv).empty();
		$(contentdiv).append(resulthtml);
	});
}

/* Remove the album */
function albumRemove(albumSid){
    $.confirm({
        'title'		: "Remove Album",
        'message'	: "¡IMPORTANT! If you confirm this window, the album will be REMOVED entirely from the database and the filesystem. YOU WILL LOSS ALL THE SONGS OF THIS ALBUM" ,
        'buttons'	: {
            'Yes'	: {
            	'title' : "YES, Remove",
                'class'	: 'blue',
                'action': function(){
        			

                    	$.ajax({
                    	    url: 'services/albums/'+albumSid,
                    	    type: 'DELETE',
                    	    success: function(result) {
                    	    	UtilShowInfo("Album removed, RIP");
                    			$.get("explore.do", function(data){ 
                    				$("#messic-page-content").empty();
                    			    var posts = $($.parseHTML(data)).filter('#content').children();
                    			    $("#messic-page-content").append(posts);
                    				initExplore();
                    			});
                    	    },
                    		fail: function(result){
                    			UtilShowInfo("OhOh! there was some kind of error removing the album.. :( ");
                    		}
                    	});
                	
                }
            },
            'No'	: {
            	'title' : "NO, DON'T remove",
                'class'	: 'gray',
                'action': function(){
                			UtilShowInfo("process cancelled");
                }	// Nothing to do in this case. You can as well omit the action property.
            }
        }
    });

}
