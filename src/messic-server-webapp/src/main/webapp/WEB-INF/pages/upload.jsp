<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

        <html>

        <head>

        </head>

        <body>

            <c:if test="${ message == null }">
                <fmt:setBundle basename="org.messic.jsp.resourcebundles.ResourceBundle" var="message" scope="session" />
            </c:if>

            <div id="content">
                <div id="messic-upload-content">
                    <form method="get" action="" id="messic-upload-album-container" class="messic-upload-container-border" data-first="${firstTime}">
                        <div class="messic-upload-container">
                            <h3><fmt:message key="upload-album-head" bundle="${message}"/></h3>
                            <div id="messic-upload-album-editnew">
                                <fmt:message key="upload-album-new" bundle="${message}" />
                            </div>
                            <div class="messic-upload-album-new-warning">
                                <fmt:message key="upload-album-new-warning" bundle="${message}" />
                            </div>
                            <div id="messic-upload-album-cover"></div>
                            <ul>
                                <li>
                                    <label for="messic-upload-album-author" class="required">
                                        <fmt:message key="upload-author" bundle="${message}" />
                                    </label>
                                    <input type="text" id="messic-upload-album-author" name="Author" required />
                                </li>
                                <li>
                                    <label for="messic-upload-album-title" class="required">
                                        <fmt:message key="upload-title" bundle="${message}" />
                                    </label>
                                    <input type="text" id="messic-upload-album-title" name="Title" required/>
                                </li>
                                <li>
                                    <label for="messic-upload-album-year">
                                        <fmt:message key="upload-year" bundle="${message}" />
                                    </label>
                                    <input id="messic-upload-album-year" name="Year" type="text" value="1990" min="1500" max="2500" />
                                </li>
                                <li>
                                    <label for="messic-upload-album-genre">
                                        <fmt:message key="upload-genre" bundle="${message}" />
                                    </label>
                                    <input type="text" id="messic-upload-album-genre" name="Genre" />
                                </li>
                                <li>
                                    <label for="messic-upload-album-volumes">Volumes</label>
                                    <input id="messic-upload-album-volumes" name="Volumes" type="text" value="1" min="1" max="100" />
                                </li>
                                <li>
                                    <label id="messic-upload-album-comments-label" for="messic-upload-album-comments">
                                        <fmt:message key="upload-comments" bundle="${message}" /> </label>
                                    <textarea maxlength="255" id="messic-upload-album-comments" name="Comments"></textarea>
                                </li>
                                <li class="status">
                                </li>
                            </ul>
                            <div class="messic-upload-song-separator"></div>

                            <button id="messic-upload-album-send" class="button play">
                                <fmt:message key="upload-button-send" bundle="${message}" />
                            </button>
                            <button id="messic-upload-album-wizard" class="button hot_drink">
                                <fmt:message key="upload-button-wizard" bundle="${message}" />
                            </button>
                            <div class="divclearer"></div>
                        </div>
                    </form>




                    <div id="messic-upload-album-songs" class="messic-upload-container-border messic-upload-album-songs">
                        <ul id="messic-upload-container-tabs">
                            <li><a href="#messic-upload-container-tab1">Vol.1</a></li>
                        </ul>
                        <div id="messic-upload-container-tab1" class="messic-upload-container messic-upload-container-songtab">
                            <div class="messic-upload-volume" data-volume="1">1</div>
                            <h3><fmt:message key="upload-songs-title1" bundle="${message}"/></h3>
                            <h4><fmt:message key="upload-songs-title2" bundle="${message}"/></h4>
                            <a href="#" class="messic-upload-song-addbutton"></a>
                            <input class="messic-upload-song-addinput" type="file" multiple>
                            <div class="messic-upload-song-separator"></div>
                            <div class="messic-upload-song-content-header">
                                <div class="messic-upload-song-content-header-track">
                                    <fmt:message key="upload-songs-track" bundle="${message}" />
                                </div>
                                <div class="messic-upload-song-content-header-filename">
                                    <fmt:message key="upload-songs-name" bundle="${message}" />
                                </div>
                                <div class="messic-upload-song-content-header-fileaction">&nbsp;</div>
                                <div class="messic-upload-song-content-header-filesize">
                                    <fmt:message key="upload-songs-size" bundle="${message}" />
                                </div>
                            </div>
                            <div class="messic-upload-song-separator"></div>

                            <ul class="messic-upload-song-content-songs" ondragover="return false">
                                <span><fmt:message key="upload-songs-drop" bundle="${message}"/></span>
                            </ul>

                            <div class="messic-upload-song-separator"></div>
                            <div class="messic-upload-song-content-footer">
                                <div class="messic-upload-song-content-header-filename">
                                    <span></span>
                                </div>
                                <div class="messic-upload-song-content-header-fileaction"></div>
                                <div class="messic-upload-song-content-header-filesize"><span class="messic-upload-zone-filelist-totalfilesize">0 b</span></div>
                                <div class="messic-upload-song-content-footer-progress">
                                    <div class="messic-upload-song-content-footer-progress-container">
                                        <div class="messic-upload-zone-progress-bar"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="messic-upload-song-separator"></div>
                        </div>
                    </div>







                    <div class="divclearer"></div>
                </div>
            </div>
        </body>

        </html>