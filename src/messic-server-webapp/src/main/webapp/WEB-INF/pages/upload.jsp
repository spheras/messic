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
			<!--
			<h1><fmt:message key="upload-head" bundle="${message}"/></h1>
			-->

			<div id="messic-upload-album-container" class="messic-upload-container-border">
				<div class="messic-upload-container">
					<!-- 
					<input id="messic-upload-album-title" type="text"/>
					<label><fmt:message key="upload-artist" bundle="${message}"/></label>

					<input id="messic-upload-album-artist" type="text" />
					<label><fmt:message key="upload-year" bundle="${message}"/></label>

					<input id="messic-upload-album-year" type="text" />
					<label><fmt:message key="upload-genre" bundle="${message}"/></label>

					<input id="messic-upload-album-genre" type="text"/>
					<label><fmt:message key="upload-comments" bundle="${message}"/></label>

					<input id="messic-upload-album-comment" type="text" />
					<div id="messic-upload-album-cover"></div>
	 				-->
					<h3><fmt:message key="upload-album-head" bundle="${message}"/></h3>
		            <ul>
		                <li>
							<label for="messic-upload-album-title" class="required"><fmt:message key="upload-title" bundle="${message}"/></label>
		                    <input type="text" id="messic-upload-album-title" name="messic-upload-album-title" class="k-textbox" placeholder="Album Title" required validationMessage="Please enter {0}" />
		                </li>
		                <li>
							<label for="messic-upload-album-author" class="required"><fmt:message key="upload-author" bundle="${message}"/></label>
		                    <input type="text" id="messic-upload-album-author" name="messic-upload-album-author" class="k-textbox" placeholder="Album Author" required validationMessage="Please enter {0}" />
		                </li>
		                <li>
		                    <label for="messic-upload-album-year">Year</label>
		                    <input id="messic-upload-album-year" name="messic-upload-album-year" type="text" min="1500" max="2100" value="1990" required data-max-msg="Enter value between 1500 and 2100" />
		                    <span class="k-invalid-msg" data-for="messic-upload-album-year"></span>
		                </li>
		                <li>
		                    <label for="messic-upload-album-genre">Genre</label>
		                    <select name="messic-upload-album-genre" id="messic-upload-album-genre" required data-required-msg="Select genre">
		                        <option>RAP</option>
		                        <option>Rock</option>
		                        <option>Soul</option>
		                        <option>Classical</option>
		                    </select>
		                    <span class="k-invalid-msg" data-for="messic-upload-album-genre"></span>
		                </li>
		                <li>
							<label for="messic-upload-album-comments" class="required"><fmt:message key="upload-comments" bundle="${message}"/></label>
		                    <input type="text" id="messic-upload-album-comments" name="messic-upload-album-comments" class="k-textbox" placeholder="Album Title" required validationMessage="Please enter {0}" />
		                </li>
		                <li class="status">
		                </li>
		            </ul>
		        </div>
			</div>
			
			
			<div id="messic-upload-song-container" class="messic-upload-container-border">
				<div class="messic-upload-container">
					<h3><fmt:message key="upload-songs-title1" bundle="${message}"/></h3>
					<h4><fmt:message key="upload-songs-title2" bundle="${message}"/></h4>
					<a href="#" id="messic-upload-song-addbutton"><a>
					<input id="messic-upload-song-addinput" type="file" multiple>
					<div class="messic-upload-song-separator"></div>
						<div class="messic-upload-song-content-header">
							<div class="messic-upload-song-content-header-track"><fmt:message key="upload-songs-track" bundle="${message}"/></div>
							<div class="messic-upload-song-content-header-filename"><fmt:message key="upload-songs-name" bundle="${message}"/></div>
							<div class="messic-upload-song-content-header-fileaction">&nbsp;</div>
							<div class="messic-upload-song-content-header-filestatus"><span><fmt:message key="upload-songs-status" bundle="${message}"/></span></div>
							<div class="messic-upload-song-content-header-filesize"><fmt:message key="upload-songs-size" bundle="${message}"/></div>
						</div>
					<div class="messic-upload-song-separator"></div>
					<ul id="messic-upload-song-content-songs" ondragover="return false"><span><fmt:message key="upload-songs-drop" bundle="${message}"/></span></ul>
					<div class="messic-upload-song-separator"></div>
						<div class="messic-upload-song-content-footer">
							<div class="messic-upload-song-content-header-filename">
								<span></span>
							</div>
							<div class="messic-upload-song-content-header-fileaction"></div>
							<div class="messic-upload-song-content-header-filestatus"><span class="messic-upload-song-filelist-totalstatus">0%</span></div>
							<div class="messic-upload-song-content-header-filesize"><span class="messic-upload-zone-filelist-totalfilesize">0 b</span></div>
							<div class="messic-upload-song-content-footer-progress">
								<div class="messic-upload-song-content-footer-progress-container">
									<div class="messic-upload-zone-progress-bar"></div>
								</div>
							</div>
						</div>
					<div class="messic-upload-song-separator"></div>

					<!--
					<div id="messic-upload-song-content">
						<div class="messic-upload-song-content-header">
							<div class="messic-upload-song-content-header-track"><fmt:message key="upload-songs-track" bundle="${message}"/></div>
							<div class="messic-upload-song-content-header-filename"><fmt:message key="upload-songs-name" bundle="${message}"/></div>
							<div class="messic-upload-song-content-header-fileaction">&nbsp;</div>
							<div class="messic-upload-song-content-header-filestatus"><span><fmt:message key="upload-songs-status" bundle="${message}"/></span></div>
							<div class="messic-upload-song-content-header-filesize"><fmt:message key="upload-songs-size" bundle="${message}"/></div>
							<div class="messic-upload-song-content-header-clearer">&nbsp;</div>
						</div>
						<ul id="messic-upload-song-content-songs" ondragover="return false">
							<fmt:message key="upload-songs-drop" bundle="${message}"/>
						</ul>
						<div class="messic-upload-song-content-footer">
							<div class="messic-upload-song-content-header-filename">
								<span></span>
							</div>
							<div class="messic-upload-song-content-header-fileaction"></div>
							<div class="messic-upload-song-content-header-filestatus"><span class="messic-upload-song-filelist-totalstatus">0%</span></div>
							<div class="messic-upload-song-content-header-filesize"><span class="messic-upload-zone-filelist-totalfilesize">0 b</span></div>
							<div class="messic-upload-song-content-footer-progress">
								<div class="messic-upload-song-content-footer-progress-container">
									<div class="messic-upload-zone-progress-bar"></div>
								</div>
							</div>
							<div class="divclearer"></div>
						</div>
					</div>
					-->
				</div>
			</div>
		</div>
    </body>
</html>
