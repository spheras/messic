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
			<p class="messic-explore-orderbytitle">This is all your library ordered by </p>
			<div class="messic-explore-orderby messic-explore-orderby-selected" data-type="author">Author</div>
			<div class="messic-explore-orderby" data-type="album">Album</div>
			
			<div class="messic-explore-words">
			</div>
			<div class="messic-explore-values">
			</div>
		</div>
    </body>
</html>
