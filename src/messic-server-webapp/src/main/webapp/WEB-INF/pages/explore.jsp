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
			<p class="messic-explore-orderbytitle"><fmt:message key="explore-order-title" bundle="${message}"/></p>
			<div class="messic-explore-orderby messic-explore-orderby-selected" data-type="author"><fmt:message key="explore-order-author" bundle="${message}"/></div>
			<div class="messic-explore-orderby" data-type="album"><fmt:message key="explore-order-album" bundle="${message}"/></div>
			
			<div class="messic-explore-words">
			</div>
			<div class="messic-explore-values">
			</div>
		</div>
    </body>
</html>
