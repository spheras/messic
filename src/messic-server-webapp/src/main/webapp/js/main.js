//function to check before to change to another section
var VAR_changeSection=function(nextFunction){
	nextFunction();
	//by default, nothing to do
	//this function should be overwritten by each module in order to be checked
}


//Select a menu
function selectOption(optionSelected)
{
	$(".messic-menu-icon").each(function(index){
		$(this).removeClass("messic-menu-icon-selected");
	});

	$(optionSelected).addClass("messic-menu-icon-selected");
}

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

    mainCreateRandomLists();

	$("#messic-menu-home").click(function(){
		var self=this;
		var nextFunction=function(){
			selectOption(self);
			mainCreateRandomLists();
			VAR_changeSection=function(nextFunction){
				nextFunction();
			}
		}
		VAR_changeSection(nextFunction);
	});

	$("#messic-menu-radio").click(function(){
		var self=this;
		var nextFunction=function(){
			selectOption(self);
			initAPIDoc();
			VAR_changeSection=function(nextFunction){
				nextFunction();
			}
		}
		VAR_changeSection(nextFunction);
	});

	$("#messic-menu-upload").click(function(){
		var self=this;
		var nextFunction=function(){
			selectOption(self);
			$.get("upload.do", function(data){
				$("#messic-page-content").empty();
				var posts = $($.parseHTML(data)).filter('#content').children();
				$("#messic-page-content").append(posts);
				initUpload();
			});
		}
		VAR_changeSection(nextFunction);
	});

	$("#messic-menu-explore").click(function(){
		var self=this;
		var nextFunction=function(){
			selectOption(self);
			$.get("explore.do", function(data){
				$("#messic-page-content").empty();
				var posts = $($.parseHTML(data)).filter('#content').children();
				$("#messic-page-content").append(posts);
				initExplore();
			});
			VAR_changeSection=function(nextFunction){
				nextFunction();
			}
		}
		VAR_changeSection(nextFunction);
	});

	$("#messic-search-text").keypress(function (e) {
	  if (e.which == 13) {
	    mainSearch();
	    e.preventDefault();
	  }
	});

	$("#messic-search-do").click(function(){
	    mainSearch();
	});

	$(".messic-playlist-action").hover(function(){
		$("#messic-playlist-background").addClass("interesting");
	},function(){
		$("#messic-playlist-background").removeClass("interesting");
	});

}
/* show the author page */
function showAuthorPage(authorSid){
	var self=this;
	var nextFunction=function(){
		$.get("author.do?authorSid="+authorSid, function(data){
			$("#messic-page-content").empty();
			var posts = $($.parseHTML(data)).filter('#content').children();
			$("#messic-page-content").append(posts);
			initAuthor();
		});
	}
	VAR_changeSection(nextFunction);
}

function mainSearch(){
	var content=$("#messic-search-text").val();
	$.getJSON( "services/search?content="+content, function( data ) {
		if(data){
			var code=mainCreateRandomList(data);
		    $("#messic-page-content").prepend($(code));
			$(".messic-main-randomlist").tinycarousel({display:9,duration:800});

			$(".messic-main-randomlist-add").hover(function(){
				$("#messic-playlist-background").addClass("interesting");
			},function(){
				$("#messic-playlist-background").removeClass("interesting");
			});
		}else{
			UtilShowInfo("We haven't found anything");
		}
	});
}

//play all the songs of a randomlist
function mainPlayRandomList(div){
	var eldiv=$(div);
	var elparent=eldiv.parent().parent();
	var elfind=elparent.find(".messic-main-randomlist-add");
	elfind.each(function(){
		var elonclick=$(this).attr("onclick");
		elonclick="function tmpcall(event){"+elonclick+"}  tmpcall(null);";
		eval(elonclick);
	});
}

//Creates the randomlist divs
function mainCreateRandomList(randomlist, lastTitleType){

				var code="<div class=\"messic-main-randomlist\">";
				code=code+"  <div class=\"messic-main-randomlist-name ";

				var titleType=UtilGetRandom(1,7);
				while(titleType==lastTitleType){
					titleType=UtilGetRandom(1,7);
				}
				lastTitleType=titleType;

				switch(titleType){
					case 1:
						code=code+"blue";
						break;
					case 2:
						code=code+"green";
						break;
					case 3:
						code=code+"orange";
						break;
					case 4:
						code=code+"pink";
						break;
					case 5:
						code=code+"purple";
						break;
					case 6:
						code=code+"red";
						break;
					case 7:
						code=code+"yellow";
						break;
				}

				code=code+ "\">"+messicLang.search(randomlist.name)+"</div>";
				code=code+"  <div class=\"messic-main-randomlist-title-container\">";
				code=code+"     <div class=\"messic-main-randomlist-title\">"+messicLang.search(randomlist.title)+"</div>";
				code=code+"     <div class=\"messic-main-randomlist-playall\" onclick=\"mainPlayRandomList(this)\" title=\""+messicLang.randomlistplayall+"\"></div>";
				code=code+"     <div class=\"messic-main-randomlist-details\">";
				if(randomlist.details){
					for(var k=0;k<randomlist.details.length;k++){
						var detail=randomlist.details[k];
						code=code+"        <div class=\"messic-main-randomlist-detail\">"+detail+"</div>";
					}
				}
				code=code+"     </div>";
				code=code+"  </div>";
				code=code+"  <a class=\"buttons prev\" href=\"#\"></a>";
				code=code+"  <div class=\"viewport\">";
				code=code+"      <ul class=\"overview\">";


						for(var j=0;randomlist.songs && j<randomlist.songs.length;j++){
							var song=randomlist.songs[j];
							code=code+"<li>";
		            		code=code+"    <div class=\"messic-main-randomlist-albumcover\" title=\""+UtilEscapeHTML(song.album.author.name)+"\n"+UtilEscapeHTML(song.album.name)+"\n"+UtilEscapeHTML(song.name)+"\" onclick=\"exploreEditAlbum('"+song.album.sid+"')\">";
		            		code=code+"        <div class=\"messic-main-randomlist-add\" onclick=\"if(event){event.stopPropagation();}addSong('raro',";

		            		code=code+"'"+UtilEscapeJS(song.album.author.name)+"',";
		            		code=code+song.album.sid+",";
		            		code=code+"'"+UtilEscapeJS(song.album.name)+"',";
		            		code=code+song.sid+",";
		            		code=code+"'"+UtilEscapeJS(song.name)+"');\"></div>";

							code=code+"        <img  src=\"services/albums/"+song.album.sid+"/cover/\"></img>";
		            		code=code+"        <div class=\"messic-main-randomlist-vinyl\"></div>";
							code=code+"    </div>"
							code=code+"    <div class=\"messic-main-randomlist-albumauthor\" title=\""+UtilEscapeHTML(song.album.author.name)+"\">"+UtilEscapeHTML(song.album.author.name)+"</div>";
							code=code+"    <div class=\"messic-main-randomlist-albumtitle\" title=\""+UtilEscapeHTML(song.name)+"\">"+UtilEscapeHTML(song.name)+"</div>";
							code=code+"</li>";
						}

							//at the end we put an empty box, only to make sure it's enough space
							code=code+"<li> <div></div> </li>";

				code=code+"      </ul>";
				code=code+"  </div>";
				code=code+"  <a class=\"buttons next\" href=\"#\"></a>";
				code=code+"</div>";
				return code;
}

function mainCreateRandomLists(){
		$("#messic-page-content").empty();

		$.getJSON( "services/randomlists", function( data ) {
			var randomlists=data;
			var lastTitleType=0;
			for(var i=0;i<randomlists.length;i++){
				var randomlist=randomlists[i];
				var code=mainCreateRandomList(randomlist,lastTitleType);
			    $("#messic-page-content").append($(code));
			}

			$(".messic-main-randomlist").tinycarousel({display:9,duration:800});

			$(".messic-main-randomlist-add").hover(function(){
				$("#messic-playlist-background").addClass("interesting");
			},function(){
				$("#messic-playlist-background").removeClass("interesting");
			});

			$(".messic-main-randomlist-playall").hover(function(){
				$("#messic-playlist-background").addClass("interesting");
			},function(){
				$("#messic-playlist-background").removeClass("interesting");
			});

		});
}

//Add an album to the playlist of songs (query the songs of the album to the server)
function addAlbum(albumSid){
	$.getJSON( "services/albums/"+albumSid+"?songsInfo=true&authorInfo=true",function(data){
		for(var z=0;z<data.songs.length;z++){
			addSong("raro",
					UtilEscapeHTML(data.author.name),
					data.sid,
					UtilEscapeHTML(data.name),
					data.songs[z].sid,
					UtilEscapeHTML(data.songs[z].name)
				   );
		}
		UtilShowInfo("Great! " + data.songs.length + " songs added to the playlist!");
	});
}

//Add a song to the playlist of songs
function addSong(titleA,authorName,albumSid,albumName,songSid,songName){
		    playlist.add({
		        title:titleA,
		        mp3:"services/songs/"+songSid+"/audio",
		        author: authorName,
		        album: albumName,
		        song: songName,
		        boxart: "services/albums/"+albumSid+"/cover/"
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
