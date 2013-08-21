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

			<div class="messic-upload-info-before">
				<div class="checkboxFive">
			  		<input type="checkbox" value="1" id="messic-upload-automtatic" name="" />
				  	<label for="messic-upload-automtatic" class="checkboxFiveTick"></label>
				  	<label for="messic-upload-automtatic" class="checkboxFiveLabel">Messic IA</label>
			  	</div>
			
				<fieldset id="messic-upload-manual-fieldset">
					<legend>Manual</legend>

					<label>Album</label>
					<input type="text" id="messic-upload-album"></input>
					<label>Author</label>
					<input type="text" id="messic-upload-author"></input>
					<label>Year</label>
					<input type="text" id="messic-upload-author"></input>
					<label>Comments</label>
					<input type="text" id="messic-upload-comments"></input>
					<label>Genre</label>
					<input type="text" id="messic-upload-genre"></input>
				</fieldset>
			</div>

			<p><a href="#">Select Files to Upload</a> or drop inside the Box</p>
			<div id="messic-upload-zone" ondragover="return false"><!-- ondragover for firefox -->- Drop Files Here -</div>


			<div class="messic-content-last-fill"></div>

		</div>
    </body>
</html>
