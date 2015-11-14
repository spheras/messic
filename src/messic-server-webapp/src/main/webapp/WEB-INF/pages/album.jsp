<%@page import="java.util.Date"%>
<%@ page import="org.messic.server.api.datamodel.Album" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="messic" uri="/WEB-INF/functions.tld" %>

<html>
	<head>
	</head>
	
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="org.messic.jsp.resourcebundles.ResourceBundle" var="message" scope="session"/>
		</c:if>
		
		<div id="content">
			<div id="messic-album-container">
				<div id="messic-album-cover">
					<div class="messic-album-covercontainer">
			        	<div class="messic-album-add" onclick="addAlbum(${messic:escapeAll(album.sid)})" title="<fmt:message key="album-addalbum-title" bundle="${message}"/>"></div>
						<img src="services/albums/${album.sid}/cover?messic_token=${token}&<%=new Date().getTime()%>" onclick="albumShowCover('${messic:escapeAll(album.sid)}')"/>
			        	<div class="messic-album-vinyl"></div>
					</div>
		        </div>
				
				<div id="messic-album-author" class="messic-H1 messic-album-editable" onclick="if(! $(this).hasClass('messic-album-editing')){showAuthorPage(${album.author.sid});}">
					<div id="messic-album-author-edit" class="messic-album-editbutton" onclick="event.stopPropagation();albumAuthorEdit()"></div>
					${messic:escapeHTML(album.author.name)}
				</div>
				<div id="messic-album-name" class="messic-H2 messic-album-editable">
					<div id="messic-album-name-edit" class="messic-album-editbutton" onclick="albumTitleEdit()"></div>
					${messic:escapeHTML(album.name)}
				</div>
				
				<div id="messic-album-menu">
					<div id="messic-album-menuoption-save" class="messic-album-menuoption" title="<fmt:message key="album-savealbum-title" bundle="${message}"/>" onclick="albumSaveChanges(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-discard" class="messic-album-menuoption" title="<fmt:message key="album-discardalbum-title" bundle="${message}"/>" onclick="albumDiscardChanges(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-remove" class="messic-album-menuoption" title="<fmt:message key="album-removealbum-title" bundle="${message}"/>" onclick="albumRemove(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-download" class="messic-album-menuoption" title="<fmt:message key="album-downloadalbum-title" bundle="${message}"/>" onclick="albumDownload(${messic:escapeAll(album.sid)})"></div>
					<div id="messic-album-menuoption-addplaylist" class="messic-album-menuoption" title="<fmt:message key="album-addplaylist-title" bundle="${message}"/>" onclick="albumAddToPlaylist(${messic:escapeAll(album.sid)})"></div>
					<div class="divclearer"></div>
				</div>
				<div id="messic-album-year" class="messic-album-predata messic-album-editable">
					<div><fmt:message key="album-year-title" bundle="${message}"/></div>
					<div id="messic-album-year-edit" class="messic-album-editbutton" onclick="albumYearEdit()"></div>
					${messic:escapeHTML(album.year)}&nbsp;
				</div>
				<div id="messic-album-genre" class="messic-album-predata messic-album-editable">
					<div><fmt:message key="album-genre-title" bundle="${message}"/></div>
					<div id="messic-album-genre-edit" class="messic-album-editbutton" onclick="albumGenreEdit()"></div>
					${messic:escapeHTML(album.genre.name)}&nbsp;
				</div>
				<div id="messic-album-volumes" class="messic-album-predata messic-album-editable">
					<div><fmt:message key="album-volumes-title" bundle="${message}"/></div>
					<div id="messic-album-volumes-edit" class="messic-album-editbutton" onclick="albumVolumesEdit()"></div>
					${messic:escapeHTML(album.volumes)}&nbsp;
				</div>
				
				<div id="messic-album-comments" class="messic-album-predata messic-album-editable">
					<div><fmt:message key="album-comments-title" bundle="${message}"/></div>
					<div id="messic-album-comments-edit" class="messic-album-editbutton" onclick="albumCommentsEdit()"></div>
					${messic:escapeHTML(album.comments)}&nbsp;
				</div>


				
			<div id="messic-album-songs-container-tabs">
                <ul id="messic-album-songs-tabs">
				<c:forEach var="volume" begin="1" end="${album.volumes}">
                    <li><a href="#messic-album-songs-container-tab${volume}">Vol.${volume}</a></li>
                </c:forEach>
                </ul>
				<c:forEach var="volume" begin="1" end="${album.volumes}">
				<div id="messic-album-songs-container-tab${volume}" class="messic-album-songs-container" data-volume="${volume}">
					<div id="messic-album-songs-head">
						<div id="messic-album-songs-head-songtrack" class="messic-album-songs-headfield"><fmt:message key="album-songtrack-title" bundle="${message}"/></div>
						<div id="messic-album-songs-head-songname" class="messic-album-songs-headfield"><fmt:message key="album-songname-title" bundle="${message}"/></div>
						<div id="messic-album-songs-head-songaction" class="messic-album-songs-headfield">
							<div class="messic-album-songs-head-songaction-add" onaction="albumAddSong()"></div>
							<input class="messic-album-songs-head-songaction-addinput" type="file" multiple>
						</div>
						<div class="divclearer"></div>
					</div>
					<div id="messic-album-songs-body">
						
						<c:forEach var="song" items="${album.songs}">
							<c:if test="${song.volume==volume}">
								<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-rate${song.rate}" onclick="albumRowSelected(this);">
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-first messic-album-songs-body-songtrack">${messic:escapeHTML(song.track)}</div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-second messic-album-songs-body-songname">${messic:escapeHTML(song.name)}</div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-third messic-album-songs-body-songaction">
										<div title="<fmt:message key="album-addplaylist-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-addtoplaylist" onclick="albumAddSongToPlaylist('${messic:escapeAll(song.sid)}')"></div>
	                                    
										<div title="<fmt:message key="album-songplay-title" bundle="${message}"/>" data-authorsid="${messic:escapeHTML(album.author.sid)}" data-albumsid="${messic:escapeHTML(album.sid)}" data-songsid="${messic:escapeHTML(song.sid)}" data-songname="${messic:escapeHTML(song.name)}" data-albumname="${messic:escapeHTML(album.name)}" data-authorname="${messic:escapeHTML(album.author.name)}" data-songrate="${messic:escapeHTML(song.rate)}" class="messic-album-songs-body-songaction-play"></div>
	                                    
										<div title="<fmt:message key="album-songedit-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-edit" onclick="albumEditSong(${messic:escapeAll(song.sid)},'${messic:escapeAll(song.track)}','${messic:escapeAll(song.name)}','${messic:escapeAll(song.album.author.name)}','${messic:escapeAll(song.album.name)}',this,${messic:escapeAll(song.rate)})"></div>
										<div title="<fmt:message key="album-songdownload-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-download" onclick="albumDownloadSong(${messic:escapeAll(song.sid)})"></div>
										<div title="<fmt:message key="album-songdelete-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-remove" onclick="albumRemoveSong('${messic:escapeAll(song.sid)}','${messic:escapeAll(song.track)}-${messic:escapeAll(song.name)}',$(this).parent().parent())"></div>
									</div>
									<div class="divclearer"></div>
								</div>
							</c:if>
						</c:forEach>				
						<c:forEach var="artwork" items="${album.artworks}">
							<c:if test="${artwork.volume==volume}">
								<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-artwork" onclick="albumRowSelected(this);">
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-first messic-album-songs-body-artwork"><img src="services/albums/${messic:escapeHTML(artwork.sid)}/resource?messic_token=${token}" onclick="albumShowArtwork('${messic:escapeAll(album.sid)}','${messic:escapeAll(artwork.sid)}')"/></div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-second messic-album-songs-body-artworkname">${messic:escapeHTML(artwork.fileName)}</div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-third messic-album-songs-body-artworkaction">
										<div title="<fmt:message key="album-artworkcover-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-cover" onclick="albumCoverArtwork('${messic:escapeAll(album.sid)}','${messic:escapeAll(artwork.sid)}', $(this).parent().parent().find('img'))"></div>
										<div title="<fmt:message key="album-artworkshow-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-show" onclick="albumShowArtwork('${messic:escapeAll(album.sid)}','${messic:escapeAll(artwork.sid)}')"></div>
										<div title="<fmt:message key="album-artworkedit-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-edit" onclick="albumEditArtwork(${messic:escapeAll(artwork.sid)},'${messic:escapeAll(artwork.fileName)}',this)"></div>
										<div title="<fmt:message key="album-artworkremove-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-remove" onclick="albumRemoveResource(${messic:escapeAll(artwork.sid)},$(this).parent().parent())"></div>
									</div>
									<div class="divclearer"></div>
								</div>
							</c:if>
						</c:forEach>
						<c:forEach var="other" items="${album.others}">
							<c:if test="${other.volume==volume}">
								<div class="messic-album-songs-bodyrow messic-album-songs-bodyrow-other" onclick="albumRowSelected(this);">
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-first messic-album-songs-body-otherfile">..</div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-second messic-album-songs-body-othername">${messic:escapeHTML(other.fileName)}</div>
									<div class="messic-album-songs-bodyfield messic-album-songs-bodyfield-third messic-album-songs-body-otheraction">
										<div title="<fmt:message key="album-otheredit-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-edit" onclick="albumEditOther(${messic:escapeAll(other.sid)},'${messic:escapeAll(other.fileName)}',this)"></div>
										<div title="<fmt:message key="album-otherdownload-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-download" onclick="albumDownloadResource(${messic:escapeAll(other.sid)})"></div>
										<div title="<fmt:message key="album-otherremove-title" bundle="${message}"/>" class="messic-album-songs-body-songaction-remove" onclick="albumRemoveResource(${messic:escapeAll(other.sid)},$(this).parent().parent())"></div>
									</div>
									<div class="divclearer"></div>
								</div>
							</c:if>
						</c:forEach>				
					</div>
				</div>
			</c:forEach>
		</div>
			
			</div>
					
			<div id="messic-album-plugincontainer">
				<ul class="messic-album-plugincontainer-menu">
					<c:forEach var="plugin" items="${plugins}">
					<li title="<fmt:message key="album-plugincontainer-plugintitle" bundle="${message}"/> ${messic:escapeHTML(plugin.getProviderName())}" class="messic-album-plugincontainer-menuitem" onclick="albumShowMusicInfo('${messic:escapeAll(album.author.name)}','${messic:escapeAll(album.name)}','${messic:escapeAll(plugin.getName())}',this)">
						<img class="messic-album-musicinfoplugin-icon" src="services/musicinfo/providericon?pluginName=${messic:escapeHTML(plugin.getName())}&messic_token=${token}"/>
					</li>
					</c:forEach>
				</ul>
				<div class="messic-album-plugincontainer-content">
					<div class="messic-album-plugincontainer-content-message"><fmt:message key="album-plugincontainer-title" bundle="${message}"/></div>
				</div>
			</div>
			<div class="divclearer"></div>
		</div>
    </body>
</html>
