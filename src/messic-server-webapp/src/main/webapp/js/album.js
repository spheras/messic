/* init the album page */
function initAlbum(){
	
}

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