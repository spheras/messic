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
	        <div id="messic-background"></div>
	        <div id="messic-decorator1"></div>
	        <div id="messic-decorator2"></div>

            <div id="messic-top">
            	<div id="messic-top-background">
					<form id="messic-search-form">
		    			<input type="text" id="messic-search-text" placeholder="<fmt:message key="main-search" bundle="${ message }"/>"></input>
		    			<a href="#" id="messic-search-do"></a>
		    		</form>
	    			<div id="messic-top-logo"></div>
            	</div>
				<lu id="messic-menu">
					<li id="messic-menu-home" class="messic-menu-icon"><div><fmt:message key="main-button-startpage" bundle="${ message }"/></div></li>
					<li id="messic-menu-explore" class="messic-menu-icon"><div><fmt:message key="main-button-exploremusic" bundle="${ message }"/></div></li>
					<li id="messic-menu-playlist" class="messic-menu-icon"><div><fmt:message key="main-button-playlist" bundle="${ message }"/></div></li>
					<li id="messic-menu-radio" class="messic-menu-icon"><div><fmt:message key="main-button-radioplayer" bundle="${ message }"/></div></li>
					<li id="messic-menu-settings" class="messic-menu-icon"><div><fmt:message key="main-button-settings" bundle="${ message }"/></div></li>
					<li id="messic-menu-upload" class="messic-menu-icon"><div><fmt:message key="main-button-uploadmusic" bundle="${ message }"/></div></li>
				</lu>
            </div>



            <div id="messic-page-border">
                <div id="messic-page-content">
        			<button value="click to add song" onclick="addsong()" style="height: 30px; width: 150px; z-index: 5; position: absolute;">Add Test Song!</button>	
                </div>
            </div>


			<div id="messic-playlist-background">

			<div id="messic-playlist-actions">
		    	<div id="messic-playlist-action-clear" class="messic-playlist-action"></div>
		    	<div id="messic-playlist-action-pin" class="messic-playlist-action"></div>
	    	</div>


			<div id="jquery_jplayer" class="jp-jplayer"></div>
			<div class="jp-audio" id="jquery_jplayer_content">
			    <div class="jp-type-playlist">
			      <div class="jp-gui jp-interface">
			        <ul class="jp-controls">
			          <li><a tabindex="1" class="jp-play" href="javascript:;">play</a></li>
			          <li><a tabindex="1" class="jp-pause" href="javascript:;" style="display: none;">pause</a></li>
			          <li><a tabindex="1" class="jp-previous" href="javascript:;">previous</a></li>
			          <li><a tabindex="1" class="jp-next" href="javascript:;">next</a></li>
			        </ul>
			        <div class="jp-progress">
			          <div class="jp-seek-bar" style="width: 100%;">
			            <div class="jp-play-bar" style="width: 0%;">
			              <div class="jp-volume-marker"></div>
			            </div>
			          </div>
			        </div>
			        <div class="jp-volume-bar">
			          <div class="jp-volume-bar-line">
			            <div class="jp-volume-bar-value" style="width: 80%;"><div class="jp-volume-marker"></div></div>
			          </div>
			        </div>
			        <div class="jp-time-holder"> <span class="jp-current-time">00:00</span>&nbsp;/&nbsp;<span class="jp-duration">04:27</span> </div>
			        <ul class="jp-toggles">
			          <li><a title="random on" tabindex="1" class="jp-shuffle" href="javascript:;">random on</a></li>
			          <li><a title="random off" tabindex="1" class="jp-shuffle-off" href="javascript:;" style="display: none;">random off</a></li>
			          <li><a title="repeat on" tabindex="1" class="jp-repeat" href="javascript:;">repeat on</a></li>
			          <li><a title="repeat off" tabindex="1" class="jp-repeat-off" href="javascript:;" style="display: none;">repeat off</a></li>
			        </ul>
			      </div>
			      
			      <div class="jp-no-solution" style="display: none;"> <span>Update Required</span> To play the media you will need to either update your browser to a recent version or update your <a target="_blank" href="http://get.adobe.com/flashplayer/">Flash plugin</a>. </div>
			    </div>
			  </div>


			    <div id="messic-playlist" class="jp-playlist">
					<div class="viewport">
						<ul id="messic-playlist-container" class="overview"><li></li></ul>
						<div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>
					</div>
			    </div>
			</div>
			


		</div>
    </body>
</html>
