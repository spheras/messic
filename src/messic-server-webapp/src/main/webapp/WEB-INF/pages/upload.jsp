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
			<h1>Upload</h1>
			<div id="messic-upload-zone">- Drop Files Here -</div>

			<div class="checkboxFive">
		  		<input type="checkbox" value="1" id="messic-upload-automtatic" name="" />
			  	<label for="messic-upload-automtatic" class="checkboxFiveTick"></label>
			  	<label for="messic-upload-automtatic" class="checkboxFiveLabel">Messic IA</label>
		  	</div>
			

			<fieldset>
				<legend>Manual</legend>

				<label>Album</label>
				<input type="text" id="messic-album-manual"></input>
				<label>Author</label>
				<input type="text" id="messic-author-manual"></input>
				<label>Year</label>
				<input type="text" id="messic-year-manual"></input>
				<label>Comments</label>
				<input type="text" id="messic-comments-manual"></input>
				<label>Genre</label>
				<input type="text" id="messic-genre-manual"></input>
			</fieldset>
		</div>
    </body>
</html>
