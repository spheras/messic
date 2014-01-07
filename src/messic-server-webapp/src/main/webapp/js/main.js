function initMessic(){
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
 
	$("#messic-menu-home").click(mainCreateRandomLists);
    mainCreateRandomLists();


	$("#messic-menu-upload").click(function(){
		$.get("upload.do", function(data){ 
			$("#messic-page-content").empty();
		    var posts = $($.parseHTML(data)).filter('#content').children();
		    $("#messic-page-content").append(posts);
			initUpload();
		});
	});

    
}

function mainCreateRandomLists(){
		$("#messic-page-content").empty();

		$.getJSON( "services/randomlists", function( data ) {
			var randomlists=data.content;
			for(var i=0;i<randomlists.length;i++){
				var randomlist=randomlists[i];
				var code="<div class='messic-main-randomlist'>";
				code=code+"  <div class='messic-main-randomlist-title'>"+"Random"+"</div>";
				code=code+"  <div class='messic-main-randomlist-songs'>";
				for(var j=0;j<randomlist.length;j++){
					var song=randomlist[j];
					code=code+"<div class='messic-main-randomlist-song'>";
            		code=code+"    <div class='messic-main-randomlist-albumcover'>";
            		code=code+"        <div class='messic-main-randomlist-add' onclick='addSong(\"raro\",";

            		code=code+"\""+UtilEscapeQuotes(song.album.author.name)+"\",";
            		code=code+song.album.sid+",";
            		code=code+"\""+UtilEscapeQuotes(song.album.name)+"\",";
            		code=code+song.sid+",";
            		code=code+"\""+UtilEscapeQuotes(song.name)+"\");'></div>";

					code=code+"        <img  src='services/album/"+song.album.sid+"/cover/'></img>";
            		code=code+"        <div class='messic-main-randomlist-vinyl'></div>";
					code=code+"    </div>"
					code=code+"    <div class='messic-main-randomlist-albumauthor'>"+song.album.author.name+"</div>";
					code=code+"    <div class='messic-main-randomlist-albumtitle'>"+song.name+"</div>";
					code=code+"</div>";
				}
				code=code+"  </div>";
				code=code+"</div>";
			    $("#messic-page-content").append($(code));
			}

			$(".messic-main-randomlist-add").hover(function(){
				$("#messic-playlist-background").addClass("interesting");
			},function(){
				$("#messic-playlist-background").removeClass("interesting");
			});

		});
}

function addSong(titleA,authorName,albumSid,albumName,songSid,songName){
		    playlist.add({
		        title:titleA,
		        mp3:"services/song/"+songSid+"/audio",
		        author: authorName,
		        album: albumName,
		        song: songName,
		        boxart: "services/album/"+albumSid+"/cover/"
			});
			$("#messic-playlist").tinyscrollbar_update('bottom');
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



