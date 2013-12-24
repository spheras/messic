function initMain(){
	(function($){
		var playlist=$("#messic-playlist");
		playlist.tinyscrollbar({ axis: 'x'});
		playlist.tinyscrollbar_update('bottom');
	})(jQuery);

    playlist = new jPlayerPlaylist(
        {
            jPlayer: "#jquery_jplayer",
            cssSelectorAncestor: "#jquery_jplayer_content",
            cssSelectorPlaylist: "#messic-playlist-background",
        },
        [],
        {
			playlistOptions: {
				enableRemoveControls: true
			},
            wmode: "window",
            errorAlerts: true,
      		swfPath: "js/vendor/jplayer",
        }
    );
 

	$("#messic-menu-upload").click(function(){
		$.get("upload.do", function(data){ 
			$("#messic-page-content").empty();
		    var posts = $($.parseHTML(data)).filter('#content').children();
		    $("#messic-page-content").append(posts);
			initUpload();
		});
	});
    
}

function playVinyl(index){
	var newli=$("#messic-playlist-container li:nth-child(" + (index + 1) + ")");
	var oldli=$(".jp-playlist-current");

	//first, remove the current player	
	oldli.removeClass("jplayer-playlist-li-expanding");
	oldli.addClass("jplayer-playlist-li");
	oldli.find(".jplayer-playlist-vinyl").attr("class","jplayer-playlist-vinyl jplayer-playlist-vinylHide");
	oldli.find(".jplayer-playlist-vinylHand").attr("class","jplayer-playlist-vinylHand");

	//last, add the new player
	var vinyl=newli.find(".jplayer-playlist-vinyl");
	var vinylHand=newli.find(".jplayer-playlist-vinylHand"); 
	newli.removeClass("jplayer-playlist-li");
	newli.addClass("jplayer-playlist-li-expanding");
	vinyl.attr("class","jplayer-playlist-vinyl jplayer-playlist-vinylPlaying");
	vinylHand.attr("class","jplayer-playlist-vinylHand jplayer-playlist-vinylHandPlaying");

	/*
	this.parentNode.parentNode.className =\"jplayer-playlist-li-expanding\";
	this.parentNode.children[0].className =\"jplayer-playlist-vinyl jplayer-playlist-vinylPlaying\";
	this.parentNode.children[1].className =\"jplayer-playlist-vinylHand jplayer-playlist-vinylHandPlaying\";
	*/
}

function addsong(){
    playlist.add({
        title:"Partir",
        mp3:"http://www.jplayer.org/audio/mp3/Miaow-09-Partir.mp3",
        author: "Autor de prueba",
        album: "Album de prueba",
        song: "Canci&oacute;n de prueba",
        boxart: "css/themes/hans/box-art-example.png"
        });

	$("#messic-playlist").tinyscrollbar_update('bottom');
}



