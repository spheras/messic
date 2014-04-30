/* init the album page */
function initAlbum(){
	
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
