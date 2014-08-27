<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
	</head>
	
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="org.messic.jsp.resourcebundles.ResourceBundle" var="message" scope="session"/>
		</c:if>

		<div id="content">
			<div id="messic-playlist-content">
				<div id="messic-playlist-content-list">
					<h2>Select a Playlist
		  				<div id="messic-playlist-menu">
							<div onclick="playlistSaveChanges();" class="messic-playlist-menuoption" id="messic-playlist-menuoption-save"></div>
							<div onclick="playlistDiscardChanges();" class="messic-playlist-menuoption" id="messic-playlist-menuoption-discard"></div>
						</div>
					</h2>
					<c:forEach var="playlist" items="${playlists}">
						<div class="messic-playlist-content-list-playlist" data-sid="${playlist.sid}" onclick="playlistShow(${playlist.sid},this)">
							<div class="messic-playlist-content-list-playlist-name" title="${playlist.name}">${playlist.name}</div>
							<div class="messic-playlist-content-list-playlist-actions">
								<div class="messic-playlist-content-list-playlist-actions-remove" onclick="event.preventDefault();event.stopPropagation();playlistRemove(${playlist.sid},this)"></div>
								<div class="messic-playlist-content-list-playlist-actions-rename" onclick="event.preventDefault();event.stopPropagation();playlistEdit(this);"></div>
								<div class="messic-playlist-content-list-playlist-actions-download" onclick="event.preventDefault();event.stopPropagation();playlistDownload(${playlist.sid})"></div>
								<div class="messic-playlist-content-list-playlist-actions-play" onclick="event.preventDefault();event.stopPropagation();playlistPlay(${playlist.sid})"></div>
							</div>
						</div>
					</c:forEach>
				</div>
				<div id="messic-playlist-content-selected">
				</div>
				<div class="divclearer"></div>
			</div>
		</div>
    </body>
</html>
