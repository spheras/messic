<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="messic" uri="/WEB-INF/functions.tld" %>

<html>
	<head>
	</head>
	
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
		</c:if>
		
		<div id="content">
			<div id="messic-album-container">
			
				<div id="cc" class="${messic:escapeHTML(variable1)}"></div>
				<div id="messic-album-cover">
					<div class="messic-album-covercontainer">
			        	<div class="messic-album-add" onclick="addAlbum(${messic:escapeAll(album.sid)})" title="Add all the album to the playlist"></div>
						<img src="services/albums/${album.sid}/cover/" onclick="albumShowCover('${messic:escapeAll(album.sid)}')"/>
					</div>
		        	<div class="messic-album-vinyl"></div>
		        </div>
				
				<div id="messic-album-author" class="messic-H1 messic-album-editable">
					<div id="messic-album-author-edit" class="messic-album-editbutton" onclick="albumAuthorEdit()"></div>
					${messic:escapeHTML(album.author.name)}
				</div>
				<div id="messic-album-name" class="messic-H2 messic-album-editable">
					<div id="messic-album-name-edit" class="messic-album-editbutton" onclick="albumTitleEdit()"></div>
					${messic:escapeHTML(album.name)}
				</div>
				
				<div id="messic-album-menu">
					<div id="messic-album-menuoption-remove" class="messic-album-menuoption" title="remove album" onclick="albumRemove(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-download" class="messic-album-menuoption" title="download album" onclick="albumDownload(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-save" class="messic-album-menuoption" title="Save changes" onclick="albumSaveChanges(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-discard" class="messic-album-menuoption" title="Discard changes" onclick="albumDiscardChanges(${messic:escapeAll(album.sid)})"></div>
				</div>
				<div id="messic-album-year" class="messic-album-predata messic-album-editable">
					<div>Year:</div>
					<div id="messic-album-year-edit" class="messic-album-editbutton" onclick="albumYearEdit()"></div>
					${messic:escapeHTML(album.year)}&nbsp;
				</div>
				<div id="messic-album-genre" class="messic-album-predata messic-album-editable">
					<div>Genre:</div>
					<div id="messic-album-genre-edit" class="messic-album-editbutton" onclick="albumGenreEdit()"></div>
					${messic:escapeHTML(album.genre.name)}&nbsp;
				</div>
				<div id="messic-album-comments" class="messic-album-predata messic-album-editable">
					<div>Comments:</div>
					<div id="messic-album-comments-edit" class="messic-album-editbutton" onclick="albumCommentsEdit()"></div>
					${messic:escapeHTML(album.comments)}&nbsp;
				</div>
				
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
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songtrack">${messic:escapeHTML(song.track)}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songname">${messic:escapeHTML(song.name)}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-songaction">
									<div title="Add song to the playlist" class="messic-album-songs-body-songaction-play" onclick="addSong('${messic:escapeAll(song.name)}','${messic:escapeAll(album.author.name)}','${messic:escapeAll(album.sid)}','${messic:escapeAll(album.name)}','${messic:escapeAll(song.sid)}','${messic:escapeAll(song.name)}')"></div>
									<div title="Edit Song" class="messic-album-songs-body-songaction-edit" onclick="albumEditSong(${messic:escapeAll(song.sid)},'${messic:escapeAll(song.track)}','${messic:escapeAll(song.name)}','${messic:escapeAll(song.album.author.name)}','${messic:escapeAll(song.album.name)}',this)"></div>
									<div title="Download Song" class="messic-album-songs-body-songaction-download" onclick="albumDownloadSong(${messic:escapeAll(song.sid)})"></div>
									<div title="Delete song" class="messic-album-songs-body-songaction-remove" onclick="albumRemoveSong('${messic:escapeAll(song.sid)}','${messic:escapeAll(song.track)}-${messic:escapeAll(song.name)}',$(this).parent().parent())"></div>
								</div>
								<div class="divclearer"></div>
							</div>
						</c:forEach>				
						<c:forEach var="artwork" items="${album.artworks}">
							<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-artwork">
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artwork"><img src="services/albums/${messic:escapeHTML(artwork.sid)}/resource" onclick="albumShowArtwork('${messic:escapeAll(artwork.sid)}')"/></div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkname">${messic:escapeHTML(artwork.fileName)}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-artworkaction">
									<div title="Show artwork" class="messic-album-songs-body-songaction-show" onclick="albumShowArtwork('${messic:escapeAll(artwork.sid)}')"></div>
									<div title="Edit Artwork" class="messic-album-songs-body-songaction-edit" onclick="albumEditArtwork(${messic:escapeAll(artwork.sid)},'${messic:escapeAll(artwork.fileName)}',this)"></div>
									<div title="Remove Artwork" class="messic-album-songs-body-songaction-remove" onclick="albumRemoveResource(${messic:escapeAll(artwork.sid)},$(this).parent().parent())"></div>
								</div>
								<div class="divclearer"></div>
							</div>
						</c:forEach>
						<c:forEach var="other" items="${album.others}">
							<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-other">
								<div class="messic-album-songs-bodyfield messic-album-songs-body-otherfile">..</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-othername">${messic:escapeHTML(other.fileName)}</div>
								<div class="messic-album-songs-bodyfield messic-album-songs-body-otheraction">
									<div title="Edit Resource" class="messic-album-songs-body-songaction-edit" onclick="albumEditOther(${messic:escapeAll(other.sid)},'${messic:escapeAll(other.fileName)}',this)"></div>
									<div title="Download Resource" class="messic-album-songs-body-songaction-download" onclick="albumDownloadResource(${messic:escapeAll(other.sid)})"></div>
									<div class="messic-album-songs-body-songaction-remove" onclick="albumRemoveResource(${messic:escapeAll(other.sid)},$(this).parent().parent())"></div>
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
					<li title="Get Information from ${messic:escapeHTML(plugin.getProviderName())}" class="messic-album-plugincontainer-menuitem" onclick="albumShowMusicInfo('${messic:escapeAll(album.author.name)}','${messic:escapeAll(album.name)}','${messic:escapeAll(plugin.getName())}',this)">
						<img class="messic-album-musicinfoplugin-icon" src="services/musicinfo/providericon?pluginName=${messic:escapeHTML(plugin.getName())}"/>
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
