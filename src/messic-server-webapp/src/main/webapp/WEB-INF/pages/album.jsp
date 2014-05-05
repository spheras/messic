<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
	</head>
	
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
		</c:if>

		<div id="content">
			<div id="messic-album-container">
				<div id='messic-album-cover'>
					<div class="messic-album-covercontainer">
			        	<div class="messic-album-add" onclick="addAlbum(${album.sid})"></div>
						<img src="services/albums/${album.sid}/cover/"/>
					</div>
		        	<div class='messic-album-vinyl'></div>
		        </div>
				
				<div id="messic-album-author" class="messic-H1">${album.author.name}</div>
				<div id="messic-album-name" class="messic-H2">${album.name}</div>
				<div id="messic-album-menu">
					<div id="messic-album-menuoption-remove" class="messic-album-menuoption" title="remove album" onclick="albumRemove(${album.sid})"></div>
					<div id="messic-album-menuoption-edit" class="messic-album-menuoption" title="edit album" onclick="albumEdit(${album.sid})"></div>
					<div id="messic-album-menuoption-save" class="messic-album-menuoption" title="save changes" onclick="alert('todo')"></div>
				</div>
				<div id="messic-album-year" class="messic-album-predata"><div>Year:</div>${album.year}&nbsp;</div>
				<div id="messic-album-genre" class="messic-album-predata"><div>Genre:</div>${album.genre.name}&nbsp;</div>
				<div id="messic-album-comments" class="messic-album-predata"><div>Comments:</div>${album.comments}&nbsp;</div>
				
				<div id="messic-album-songs-container">
					<div id="messic-album-songs-head">
						<div id="messic-album-songs-head-songtrack" class="messic-album-songs-headfield">#Track</div>
						<div id="messic-album-songs-head-songname" class="messic-album-songs-headfield">Name</div>
						<div id="messic-album-songs-head-songaction" class="messic-album-songs-headfield">
							<div id="messic-album-songs-head-songaction-add" onaction="albumAddSong()"></div>
							<input id="messic-album-songs-head-songaction-addinput" type="file" multiple>
						</div>
						<div class="divclearer"></div>
					</div>
					<div id="messic-album-songs-body">
						<c:forEach var="song" items="${album.songs}">
							<div class="messic-album-songs-bodyrow">
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songtrack">${song.track}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songname">${song.name}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songaction">
									<div title="Add song to the playlist" class='messic-album-songs-body-songaction-play' onclick='addSong(UtilEscapeQuotes("${song.name}"),UtilEscapeQuotes("${album.author.name}"),${album.sid},UtilEscapeQuotes("${album.name}"),${song.sid},UtilEscapeQuotes("${song.name}"))'></div>
									<div title="Delete song" class='messic-album-songs-body-songaction-remove' onclick='albumRemoveSong("${song.sid}","${song.track}-${song.name}",$(this).parent().parent())'></div>
								</div>
								<div class="divclearer"></div>
							</div>
						</c:forEach>				
						<c:forEach var="artwork" items="${album.artworks}">
							<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-artwork">
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artwork"><img src="services/albums/${artwork.sid}/resource" onclick='albumShowArtwork("${artwork.sid}")'/></div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkname">${artwork.fileName}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkaction">
									<div class='messic-album-songs-body-songaction-show' onclick='albumShowArtwork("${artwork.sid}")'></div>
									<div class='messic-album-songs-body-songaction-remove' onclick='albumRemoveResource(${artwork.sid},$(this).parent().parent())'></div>
								</div>
								<div class="divclearer"></div>
							</div>
						</c:forEach>				
						<c:forEach var="other" items="${album.others}">
							<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-other">
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artwork">..</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkname">${other.fileName}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkaction">
									<div class='messic-album-songs-body-songaction-remove' onclick='albumRemoveResource(${other.sid},$(this).parent().parent())'></div>
								</div>
								<div class="divclearer"></div>
							</div>
						</c:forEach>				
					</div>
				</div>
			</div>
			<div id="messic-album-plugincontainer">
				<ul class="messic-album-plugincontainer-menu">
					<c:forEach var="plugin" items="${plugins}">
					<li title="Get Information from ${plugin.getProviderName()}" class="messic-album-plugincontainer-menuitem" onclick="albumShowMusicInfo('${album.author.name}','${album.name}','${plugin.getName()}',this)">
						<img class="messic-album-musicinfoplugin-icon" src="services/musicinfo/providericon?pluginName=${plugin.getName()}"/>
					</li>
					</c:forEach>
				</ul>
				<div class="messic-album-plugincontainer-content">
					<div class="messic-album-plugincontainer-content-message">Click on any provider option to get information of this album</div>
				</div>
			</div>
			<div class="divclearer"></div>
		</div>
    </body>
</html>
