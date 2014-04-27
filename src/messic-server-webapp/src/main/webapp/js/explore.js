/* init the explore page */
function initExplore(){
	
	//adding the code to the click for the order by author or order by album
	$(".messic-explore-orderby").each(function(index){
		var type=$(this).data('type');
		if(type=="author"){
			$(this).click(function(){
				if(!$(this).hasClass("messic-explore-orderby-selected")){
					$(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
					$(this).addClass("messic-explore-orderby-selected");
				}
				$(".messic-explore-values").empty();
				exploreByAuthor();
				
			})
		}else{
			$(this).click(function(){
				if(!$(this).hasClass("messic-explore-orderby-selected")){
					$(".messic-explore-orderby-selected").removeClass("messic-explore-orderby-selected");
					$(this).addClass("messic-explore-orderby-selected");
				}
				$(".messic-explore-values").empty();
				exploreByAlbum();
			})
		}
	});

	exploreByAuthor();
}

//Fill the web page with the whole list of albums, ordered by name
function exploreByAlbum(){
	$.getJSON( "services/albums?authorInfo=true&songsInfo=false",function( data ) {
		
		$(".messic-explore-words").empty();
		
		var lastChar="";
		for(var i=0;i<data.length;i++){
			var code="";
			var firstword=data[i].name.substring(0,1).toUpperCase();
			var changedWord=false;

			code=code+"<div class='messic-explore-albumcontainer' title='"+data[i].name+"'>";

			if(i==0 || lastChar!=firstword){
				code=code+"<a name='messic-explore-word"+firstword+"' class='messic-explore-anchor'></a>";
				lastChar=firstword;
				changedWord=true;
			}
			
			if(changedWord){
				code=code+"<div class='messic-explore-albumstartword'>"+lastChar.toUpperCase()+"</div>";
				
				codewords="<a class='messic-explore-word' href='#messic-explore-word"+lastChar.toUpperCase()+"'>"+lastChar.toUpperCase()+"</a>";
				$(".messic-explore-words").append(codewords);
			}
			

	        		code=code+"    <div class='messic-explore-albumcover'>";
	        		code=code+"        <div class='messic-explore-add' onclick='addAlbum("+data[i].sid+")'></div>";
	        		code=code+"        <div class='messic-explore-vinyl-detail' title='Edit Album' onclick='exploreEditAlbum("+data[i].sid+")'>...</div>";
					code=code+"        <img  src='services/albums/"+data[i].sid+"/cover/' onclick='exploreEditAlbum("+data[i].sid+")'></img>";
	        		code=code+"        <div class='messic-explore-vinyl'></div>";
					code=code+"    </div>"
					code=code+"    <div class='messic-explore-albumtitle' title='"+data[i].name+"'>"+data[i].name+"</div>";
					code=code+"    <div class='messic-explore-albumauthortitle' title='"+data[i].author.name+"'>"+data[i].author.name+"</div>";
					code=code+"</div>";

			code=code+"</div>";
			$(".messic-explore-values").append(code);
		}

		$(".messic-explore-add").hover(function(){
			$("#messic-playlist-background").addClass("interesting");
		},function(){
			$("#messic-playlist-background").removeClass("interesting");
		});

	});
}

//fill the web page with the whole list of album categorized by authors
function exploreByAuthor(){
	$.getJSON( "services/authors?albumsInfo=true&songsInfo=false",function( data ) {
		$(".messic-explore-words").empty();
		
		var lastChar="";
		for(var i=0;i<data.length;i++){
			var code="";
			var firstword=data[i].name.substring(0,1).toUpperCase();
			var changedWord=false;
			if(i==0 || lastChar!=firstword){
				code=code+"<a name='messic-explore-word"+firstword+"' class='messic-explore-anchor'></a>";
				lastChar=firstword;
				changedWord=true;
			}
			code=code+"<div class='messic-explore-authortitle' title='"+data[i].name+"'><div class='messic-explore-authortitle-text'>"+data[i].name+"</div>";
			if(changedWord){
				code=code+"<div class='messic-explore-albumstartword'>"+lastChar.toUpperCase()+"</div>";
				
				codewords="<a class='messic-explore-word' href='#messic-explore-word"+lastChar.toUpperCase()+"'>"+lastChar.toUpperCase()+"</a>";
				$(".messic-explore-words").append(codewords);
			}
			
			//we add some albums, just to debug and test
			//var debugRandom=UtilGetRandom(1,5);
			//for(var debug=0;debug<debugRandom;debug++){
				for(var j=0;j<data[i].albums.length;j++){
					var album=data[i].albums[j];
					code=code+"<div class='messic-explore-albumcontainer' title='"+album.name+"'>";
	        		code=code+"    <div class='messic-explore-albumcover'>";
	        		code=code+"        <div class='messic-explore-add' onclick='addAlbum("+album.sid+")'></div>";
	        		code=code+"        <div class='messic-explore-vinyl-detail' title='Edit Album' onclick='exploreEditAlbum("+album.sid+")'>...</div>";
					code=code+"        <img  src='services/albums/"+album.sid+"/cover/' onclick='exploreEditAlbum("+album.sid+")'></img>";
	        		code=code+"        <div class='messic-explore-vinyl'></div>";
					code=code+"    </div>"
					code=code+"    <div class='messic-explore-albumtitle' title='"+album.name+"'>"+album.name+"</div>";
					code=code+"</div>";
				}
			//}
			
			code=code+"</div>";
			$(".messic-explore-values").append(code);
		}
		
		$(".messic-explore-add").hover(function(){
			$("#messic-playlist-background").addClass("interesting");
		},function(){
			$("#messic-playlist-background").removeClass("interesting");
		});
	});
}

function exploreEditAlbum(albumSid){
	$.get("album.do?albumSid="+albumSid, function(data){ 
		$("#messic-page-content").empty();
	    var posts = $($.parseHTML(data)).filter('#content').children();
	    $("#messic-page-content").append(posts);
	});
}