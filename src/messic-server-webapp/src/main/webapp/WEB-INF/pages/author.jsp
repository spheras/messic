<%@page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="messic" uri="/WEB-INF/functions.tld"%>

<html>

<head>
</head>

<body>

	<c:if test="${ message == null }">
		<fmt:setBundle basename="ResourceBundle" var="message" scope="application" />
	</c:if>

	<div id="content">
		<div id="messic-author-title" class="messic-H1">
			<div class="messic-author-title">${messic:escapeHTML(author.name)}</div>
			<div id="messic-author-menuoption-remove"
				class="messic-author-menuoption"
				title="<fmt:message key="author-removeauthor-title" bundle="${message}"/>"
				onclick="authorRemove(${messic:escapeAll(author.sid)})"></div>
			<div class="divclearer"></div>
		</div>

		<div id="messic-author-container">
			<c:forEach var="album" items="${author.albums}">
				<div id="messic-author-album-cover">
					<div class="messic-author-album-covercontainer">
						<div class="messic-author-album-add"
							onclick="addAlbum(${messic:escapeAll(album.sid)})"
							title="<fmt:message key="album-addalbum-title" bundle="${message}"/>"></div>
						<img
							src="services/albums/${album.sid}/cover?messic_token=${token}&<%=new Date().getTime()%>"
							onclick="albumShowCover('${messic:escapeAll(album.sid)}')" />
						<div class="messic-author-album-vinyl"></div>
					</div>
				</div>

				<div id="messic-author-songs-container">
					<div class="messic-author-album-title"
						onclick="exploreEditAlbum(${album.sid})">${album.name}</div>
					<c:forEach var="song" items="${album.songs}">
						<div class="messic-author-songs-bodyrow"
							onclick="addSong('${messic:escapeAll(song.name)}','${messic:escapeAll(album.author.name)}','${messic:escapeAll(album.sid)}','${messic:escapeAll(album.name)}','${messic:escapeAll(song.sid)}','${messic:escapeAll(song.name)}',${messic:escapeAll(song.rate)})">
							<div
								class="messic-author-songs-bodyfield messic-author-songs-body-songtrack">${messic:escapeHTML(song.track)}</div>
							<div
								class="messic-author-songs-bodyfield messic-author-songs-body-songname">${messic:escapeHTML(song.name)}</div>
							<div class="divclearer"></div>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
		<div id="messic-author-plugincontainer">
			<ul class="messic-author-plugincontainer-menu">
				<c:forEach var="plugin" items="${plugins}">
					<li
						title="<fmt:message key="album-plugincontainer-plugintitle" bundle="${message}"/> ${messic:escapeHTML(plugin.getProviderName())}"
						class="messic-author-plugincontainer-menuitem"
						onclick="authorShowAuthorInfo('${messic:escapeAll(author.name)}','${messic:escapeAll(plugin.getName())}',this)">
						<img class="messic-author-musicinfoplugin-icon"
						src="services/musicinfo/providericon?pluginName=${messic:escapeHTML(plugin.getName())}&messic_token=${token}" />
					</li>
				</c:forEach>
			</ul>
			<div class="messic-author-plugincontainer-content">
				<div class="messic-author-plugincontainer-content-message">
					<fmt:message key="album-plugincontainer-title" bundle="${message}" />
				</div>
			</div>
		</div>
		<div class="divclearer"></div>
	</div>
</body>

</html>
